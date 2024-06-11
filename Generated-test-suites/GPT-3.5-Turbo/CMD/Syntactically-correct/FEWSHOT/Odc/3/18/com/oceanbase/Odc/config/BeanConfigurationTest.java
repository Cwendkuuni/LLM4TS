package com.oceanbase.odc.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mockito.Mock;
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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new SensitivePropertyHandler();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        ((DefaultCookieSerializer) cookieSerializer).setCookieName("JSESSIONID");
        assertNotNull(cookieSerializer);
    }
}