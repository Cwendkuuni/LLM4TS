package com.oceanbase.odc.service.session.factory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    
    private OBConsoleDataSourceFactory testOVConsoleDataSourceFactory;
    private ConnectionConfig testConnectionConfig;

    @Before
    public void setup() {
        testConnectionConfig = new ConnectionConfig();
        testConnectionConfig.setUsername("testUser");
        // similarly set up other properties of testConnectionConfig as needed
        
        // Create the Object under test
        testOVConsoleDataSourceFactory = new OBConsoleDataSourceFactory(testConnectionConfig, true);
    }
    
    @Test
    public void testGetUsername() {
        String expectedUsername = "testUser";
        Assert.assertEquals(expectedUsername, OBConsoleDataSourceFactory.getUsername(testConnectionConfig));
    }

    // ...Similarly setup tests for other methods
}