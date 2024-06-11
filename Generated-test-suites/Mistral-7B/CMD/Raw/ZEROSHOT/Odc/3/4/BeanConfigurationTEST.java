package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String jsonString = "{\"name\":\"John Doe\",\"age\":30,\"address\":{\"city\":\"New York\"}}";
        OBInstance obj = objectMapper.readValue(jsonString, OBInstance.class);
        assertEquals("John Doe", obj.getName());
        assertEquals(30, obj.getAge());
        assertNotNull(obj.getAddress());
        assertEquals("New York", obj.getAddress().getCity());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        String url = "http://%65%6e%63%6F%64%65%64%2E%74%68%69%73%70%65%2Ftest";
        assertTrue(defaultHttpFirewall.isSafe(url));
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertInstanceOf(NullCloudMetadataClient.class, cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        String cookieName = cookieSerializer.getName();
        assertEquals("JSESSIONID", cookieName);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
        assertNull(nullCloudMetadataClient.listInstances());
        assertNull(nullCloudMetadataClient.listTenants(""));
        assertNull(nullCloudMetadataClient.getTenant("", ""));
        assertNull(nullCloudMetadataClient.listDatabaseUsers("", ""));
        assertNull(nullCloudMetadataClient.getTenantEndpoint("", ""));
        assertNull(nullCloudMetadataClient.getSysTenantUser(""));
    }
}