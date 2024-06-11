package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

import com.oceanbase.odc.config.BeanConfiguration;

/**
 * Tests for the BeanConfiguration class
 *
 * @author yizhou.xw
 */
public class BeanConfigurationTest {
    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @Before
    public void setup() {
        sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        cloudMetadataClient = mock(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = BeanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = BeanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = Mockito.spy(new CustomOutputSerializer());
        when(customOutputSerializer.addSerializer(Mockito.any(), Mockito.any())).thenReturn(customOutputSerializer);
        when(sensitivePropertyHandler.decrypt("test")).thenReturn("decrypted_test");

        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        assertEquals(expectedObjectMapper, objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = BeanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration configuration = new BeanConfiguration();
        CloudMetadataClient actualCloudMetadataClient = configuration.cloudMetadataClient();
        assertEquals(NullCloudMetadataClient.class, actualCloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = BeanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient actualCloudMetadataClient = BeanConfiguration.nullCloudMetadataClient();
        assertNotNull(actualCloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, actualCloudMetadataClient.getClass());
    }

    @Test
    public void testGetTenantEndpoint() {
        when(cloudMetadataClient.getTenantEndpoint("testInstance", "testTenant"))
                .thenReturn(createTenantEndpoint());

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        OBTenantEndpoint actualTenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("testInstance", "testTenant");

        assertEquals(createTenantEndpoint(), actualTenantEndpoint);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        when(cloudMetadataClient.getTenant("testInstance", "testTenant"))
                .thenThrow(new UnsupportedException("test"));

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("testInstance", "testTenant");
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        when(cloudMetadataClient.listTenants("testInstance"))
                .thenThrow(new UnsupportedException("test"));

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        nullCloudMetadataClient.listTenants("testInstance");
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        when(cloudMetadataClient.listInstances())
                .thenThrow(new UnsupportedException("test"));

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        nullCloudMetadataClient.listInstances();
    }

    @Test
    public void testListDatabaseUsers() {
        when(cloudMetadataClient.listDatabaseUsers("testInstance", "testTenant"))
                .thenReturn(createDatabaseUsers());

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        List<OBDatabaseUser> actualDatabaseUsers = nullCloudMetadataClient.listDatabaseUsers("testInstance", "testTenant");

        assertEquals(createDatabaseUsers(), actualDatabaseUsers);
    }

    @Test
    public void testGetSysTenantUser() {
        when(cloudMetadataClient.getSysTenantUser("testInstance"))
                .thenReturn(createSysTenantUser());

        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        OBDatabaseUser actualSysTenantUser = nullCloudMetadataClient.getSysTenantUser("testInstance");

        assertEquals(createSysTenantUser(), actualSysTenantUser);
    }

    private OBTenantEndpoint createTenantEndpoint() {
        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        tenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        return tenantEndpoint;
    }

    private List<OBDatabaseUser> createDatabaseUsers() {
        List<OBDatabaseUser> databaseUsers = new ArrayList<>();
        OBDatabaseUser databaseUser = new OBDatabaseUser();
        databaseUser.setUsername("testUsername");
        databaseUser.setPassword("testPassword");
        databaseUsers.add(databaseUser);
        return databaseUsers;
    }

    private OBDatabaseUser createSysTenantUser() {
        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        sysTenantUser.setUsername("testUsername");
        sysTenantUser.setPassword("testPassword");
        return sysTenantUser;
    }
}