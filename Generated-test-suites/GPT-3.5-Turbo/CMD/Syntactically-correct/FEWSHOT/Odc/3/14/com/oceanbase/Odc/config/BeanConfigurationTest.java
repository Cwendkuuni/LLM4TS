package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        ObjectMapper mapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer()));
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        assertNotNull(firewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = new BeanConfiguration().new NullCloudMetadataClient();
        assertEquals(false, client.supportsCloudMetadata());
        assertEquals(false, client.supportsTenantInstance());
        assertEquals(false, client.needsOBTenantName());
        assertEquals(false, client.needsSysTenantUser());
        assertEquals(true, client.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());

        UnsupportedException e = null;
        try {
            client.listInstances();
        } catch (UnsupportedException ex) {
            e = ex;
        }
        assertNotNull(e);

        e = null;
        try {
            client.listTenants("instanceId");
        } catch (UnsupportedException ex) {
            e = ex;
        }
        assertNotNull(e);

        e = null;
        try {
            client.getTenant("instanceId", "tenantId");
        } catch (UnsupportedException ex) {
            e = ex;
        }
        assertNotNull(e);

        e = null;
        try {
            client.listDatabaseUsers("instanceId", "tenantId");
        } catch (UnsupportedException ex) {
            e = ex;
        }
        assertNotNull(e);

        OBTenantEndpoint endpoint = client.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(endpoint);

        e = null;
        try {
            client.getSysTenantUser("instanceId");
        } catch (UnsupportedException ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        assertNotNull(serializer);
    }

}