package com.emptylogic;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
 
public class AwsEc2Info
{
    public static void main(String[] args) throws Exception
    {
	    AWSCredentials credentials = null;
        try 
        {
        	//credentials = new BasicAWSCredentials("<ACCESS-KEY>", "<SECRET-KEY> - the longer one");
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } 
        catch (Exception e) 
        {
            throw new AmazonClientException("Could not create credentials.", e);
        }
        
        AmazonEC2 ec2 = new AmazonEC2Client(credentials);
        int count = 1;
        for (Regions regions : Regions.values())
        {
            Region region = Region.getRegion(Regions.fromName(regions.getName()));
            ec2.setRegion(region);
            try 
            {
                DescribeInstancesResult instanceResult = ec2.describeInstances();
                
                for (Reservation reservation : instanceResult.getReservations())
                {
                    for(Instance instance : reservation.getInstances())
                    {
                        System.out.println("Instance # " + count++
                            + "\n InstanceId: " + instance.getInstanceId()
                            + "\n InstanceType: " + instance.getInstanceType()
                            + "\n Public IP: " + instance.getPublicIpAddress()
                            + "\n Public DNS: " + instance.getPublicDnsName()
                            + "\n");
                    }
                }
            }
            catch(AmazonEC2Exception e) 
            {
            	//Not authorized so ignore this region
            }
        }
    }
}