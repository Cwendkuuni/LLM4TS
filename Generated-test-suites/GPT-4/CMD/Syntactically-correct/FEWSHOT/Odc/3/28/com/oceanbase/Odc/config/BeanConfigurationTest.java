
package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        PasswordEncoder encoder = config.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testDefaultHttpFirewall() {
        BeanConfiguration config = new BeanConfiguration();
        HttpFirewall firewall = config.defaultHttpFirewall();
        assertTrue(firewall instanceof DefaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        BeanConfiguration config = new BeanConfiguration();
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) config.cloudMetadataClient();
        assertEquals(client.supportsCloudMetadata(), false);
        assertEquals(client.supportsTenantInstance(), false);
        assertEquals(client.needsOBTenantName(), false);
        assertEquals(client.needsSysTenantUser(), false);
        assertEquals(client.includeClusterNameForJdbcUsername(), true);
        assertEquals(client.oceanbaseAccessMode(), OceanBaseAccessMode.DIRECT);
        assertThrows(UnsupportedException.class, () -> client.listInstances());
        assertThrows(UnsupportedException.class, () -> client.listTenants("dummyInstanceId"));
        assertThrows(UnsupportedException.class, () -> client.getTenant("dummyInstanceId", "dummyTenantId"));
        assertThrows(UnsupportedException.class, () -> client.listDatabaseUsers("dummyInstanceId", "dummyTenantId"));
        assertThrows(UnsupportedException.class, () -> client.getSysTenantUser("dummyInstanceId"));
    }

    @Test
    public void testCookieSerializer() {
        BeanConfiguration config = new BeanConfiguration();
        CookieSerializer serializer = config.cookieSerializer();
        assertTrue(serializer instanceof DefaultCookieSerializer);
    }

}
