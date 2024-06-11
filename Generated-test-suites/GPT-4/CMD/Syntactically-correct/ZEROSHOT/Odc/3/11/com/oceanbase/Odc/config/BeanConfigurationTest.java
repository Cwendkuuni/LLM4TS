package com.oceanbase.odc.config;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;

public class BeanConfigurationTest {
    
    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp(){
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
        boolean hasSensitiveModule = false;
        for (Module module : objectMapper.getRegisteredModuleIds()) {
            if (module.getModuleName().equals("JacksonModules.sensitiveInputHandling")) {
                hasSensitiveModule = true;
                break;
            }
        }
        assertTrue(hasSensitiveModule);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertTrue(client instanceof NullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupported() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        nullCloudMetadataClient.listInstances();
        nullCloudMetadataClient.listTenants("");
        nullCloudMetadataClient.getTenant("", "");
        nullCloudMetadataClient.listDatabaseUsers("", "");
        nullCloudMetadataClient.getSysTenantUser("");
    }
}    
