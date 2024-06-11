package com.oceanbase.odc.config;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class BeanConfigurationTest {

    private static BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient;

    @BeforeClass
    public static void setUp() {
        nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
    }

    @AfterClass
    public static void tearDown() {
        nullCloudMetadataClient = null;
    }

    @Test
    public void supportsCloudMetadata(){
        assertEquals(false,nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void supportsTenantInstance(){
        assertEquals(false,nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void needsOBTenantName(){
        assertEquals(false,nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void needsSysTenantUser(){
        assertEquals(false,nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void includeClusterNameForJdbcUsername(){
        assertEquals(true,nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test(expected = UnsupportedException.class)
    public void listInstances(){
        nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void listTenants(){
        nullCloudMetadataClient.listTenants("InstanceID_01");
    }

    @Test(expected = UnsupportedException.class)
    public void getTenant(){
        nullCloudMetadataClient.getTenant("InstanceID_01","TenantID_01");
    }

    @Test(expected = UnsupportedException.class)
    public void listDatabaseUsers(){
        nullCloudMetadataClient.listDatabaseUsers("InstanceID_01","TenantID_01");
    }

    @Test(expected = UnsupportedException.class)
    public void getSysTenantUser(){
        nullCloudMetadataClient.getSysTenantUser("InstanceID_01");
    }
}
