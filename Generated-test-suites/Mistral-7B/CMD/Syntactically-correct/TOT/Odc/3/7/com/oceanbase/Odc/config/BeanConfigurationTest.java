package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Test class for BeanConfiguration.
 *
 * @author yizhou.xw
 * @version : BeanConfigurationTest.java, v 0.1 2023-03-18 10:27
 */
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CookieSerializer cookieSerializer;

    @Mock
    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(beanConfiguration.objectMapper(sensitivePropertyHandler)).thenReturn(objectMapper);
        when(beanConfiguration.cookieSerializer()).thenReturn(cookieSerializer);
        when(applicationContext.getBean(ObjectMapper.class)).thenReturn(objectMapper);
    }

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        CustomOutputSerializer customOutputSerializer = mock(CustomOutputSerializer.class);
        when(objectMapper.registerModule(JacksonModules.customOutputHandling(customOutputSerializer))).thenReturn(objectMapper);
        ObjectMapper returnedObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertEquals(objectMapper, returnedObjectMapper);
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantEndpointForNullInstanceAndTenantId() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = beanConfiguration.getTenantEndpoint(null, "tenantId");
        verify(cloudMetadataClient).getTenantEndpoint(eq(null), eq("tenantId"));
        fail("Expected UnsupportedException to be thrown");
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantForNullInstanceAndTenantId() {
        NullCloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenant tenant = beanConfiguration.getTenant(null, "tenantId");
        verify(cloudMetadataClient).getTenant(eq(null), eq("tenantId"));
        fail("Expected UnsupportedException to be thrown");
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstancesForNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = null;
        List<OBInstance> instances = beanConfiguration.listInstances(cloudMetadataClient);
        verify(cloudMetadataClient).listInstances();
        fail("Expected UnsupportedException to be thrown");
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenantsForNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = null;
        List<OBTenant> tenants = beanConfiguration.listTenants("instanceId");
        verify(cloudMetadataClient).listTenants("instanceId");
        fail("Expected UnsupportedException to be thrown");
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsersForNullCloudMetadataClient() {
        NullCloudMetadataClient cloudMetadataClient = null;
        List<OBDatabaseUser> databaseUsers = beanConfiguration.listDatabaseUsers("instanceId", "tenantId");
        verify(cloudMetadataClient).listDatabaseUsers("instanceId", "tenantId");
        fail("Expected UnsupportedException to be thrown");
    }

    @Test
    public void testBeanConfigurationInitialization() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BeanConfiguration.class);
        context.refresh();
        assertTrue(context.containsBean("passwordEncoder"));
        assertTrue(context.containsBean("objectMapper"));
        assertTrue(context.containsBean("defaultHttpFirewall"));
        assertTrue(context.containsBean("cloudMetadataClient"));
        assertTrue(context.containsBean("cookieSerializer"));
    }
}