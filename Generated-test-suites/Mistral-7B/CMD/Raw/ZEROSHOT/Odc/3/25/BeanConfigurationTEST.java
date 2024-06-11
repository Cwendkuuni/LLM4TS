package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;
    @InjectMocks
    private SensitivePropertyHandler sensitivePropertyHandler;
    @InjectMocks
    private I18nOutputSerializer i18nOutputSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBeanWithSensitivePropertyHandler() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void testHttpFirewallBean() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClientInitMethod() {
        beanConfiguration.new NullCloudMetadataClient();
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenant tenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNotNull(tenant);
    }
}