package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

/**
 * Test class for BeanConfiguration
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-15 10:30
 */
@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;
    private NullCloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        cloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
    }

    /**
     * Test passwordEncoder method
     */
    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String originalPassword = "password";
        String encodedPassword = passwordEncoder.encode(originalPassword);
        assertTrue(passwordEncoder.matches(originalPassword, encodedPassword));
    }

    /**
     * Test objectMapper method
     */
    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
    }

    /**
     * Test defaultHttpFirewall method
     */
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        // Add tests for setAllowUrlEncodedSlash(true) if necessary
    }

    /**
     * Test cloudMetadataClient method with mocked CloudMetadataClient
     */
    @Test
    public void testCloudMetadataClient() {
        assertNotNull(beanConfiguration.cloudMetadataClient());
    }

    /**
     * Test cloudMetadataClient method with NullCloudMetadataClient
     */
    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertInstanceOf(NullCloudMetadataClient.class, beanConfiguration.cloudMetadataClient());
    }

    /**
     * Test cloudMetadataClient methods with exceptions
     */
    @Test(expected = UnsupportedException.class)
    public void testCloudMetadataClientException() {
        cloudMetadataClient.listInstances();
        cloudMetadataClient.listTenants("");
        cloudMetadataClient.getTenant("", "");
        cloudMetadataClient.listDatabaseUsers("", "");
    }

}