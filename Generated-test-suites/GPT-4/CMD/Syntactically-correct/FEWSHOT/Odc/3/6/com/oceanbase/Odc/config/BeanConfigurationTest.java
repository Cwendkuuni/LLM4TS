
package com.oceanbase.odc.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    private final BeanConfiguration beanConfiguration = new BeanConfiguration();

    @Test
    public void passwordEncoder() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();

        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void objectMapper() {
        SensitivePropertyHandler handler = mock(SensitivePropertyHandler.class);
        ObjectMapper mapper = beanConfiguration.objectMapper(handler);

        assertNotNull(mapper);
    }

    @Test
    public void defaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();

        assertNotNull(firewall);
    }

    @Test
    public void cloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();

        assertFalse(client.supportsCloudMetadata());
    }

    @Test
    public void cookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();

        assertNotNull(serializer);
    }
}
