package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@Slf4j
public class BeanConfigurationTest {

    private SensitivePropertyHandler sensitivePropertyHandler;
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        beanConfiguration = new BeanConfiguration();
        beanConfiguration.setSensitivePropertyHandler(sensitivePropertyHandler);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String encryptedPassword = passwordEncoder.encode("password");
        assertTrue(passwordEncoder.matches("password", encryptedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        // Add test cases for objectMapper methods here
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        // Add test cases for defaultHttpFirewall methods here
    }

    @Test
    public void testCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        // Add test cases for cloudMetadataClient methods here

        verify(sensitivePropertyHandler, times(0)).decrypt(any());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        // Add test cases for NullCloudMetadataClient methods here

        assertTrue(nullCloudMetadataClient instanceof NullCloudMetadataClient);
        verify(sensitivePropertyHandler, times(0)).decrypt(any());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        // Add test cases for cookieSerializer methods here
    }
}