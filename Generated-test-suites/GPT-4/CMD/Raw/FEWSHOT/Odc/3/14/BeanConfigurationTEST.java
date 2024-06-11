package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OBInstance;

import org.junit.Test;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.List;

public class BeanConfigurationTest {

    private BeanConfiguration config = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = config.passwordEncoder();
        assertTrue(passwordEncoder.encode("test").contains("$2a$"));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = config.objectMapper( string -> string );
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = config.defaultHttpFirewall();
        assertNotNull(httpFirewall.getFirewalledRequest(new MockHttpServletRequest()));
    }

    @Test
    public void testTheCloudMetadataClient() {
        CloudMetadataClient cnc = config.cloudMetadataClient();
        assertNotNull(cnc);
        assertFalse(cnc.supportsCloudMetadata());
        assertFalse(cnc.supportsTenantInstance());
        assertFalse(cnc.needsOBTenantName());
        assertFalse(cnc.needsSysTenantUser());
        assertTrue(cnc.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, cnc.oceanbaseAccessMode());
    }
    
    @Test(expected = UnsupportedException.class)
    public void testUnsupportedFunctions() {
        CloudMetadataClient cnc = config.cloudMetadataClient();
        List<OBInstance> obiList = cnc.listInstances();
        List<OBTenant> obtList = cnc.listTenants("instanceId");
        OBTenant obt = cnc.getTenant("instanceId", "tenantId");
        List<OBDatabaseUser> obdUserList = cnc.listDatabaseUsers("instanceId", "tenantId");
        OBDatabaseUser obdUser = cnc.getSysTenantUser("instanceId");    
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = config.cookieSerializer();
        assertNotNull(cookieSerializer);
    }
}