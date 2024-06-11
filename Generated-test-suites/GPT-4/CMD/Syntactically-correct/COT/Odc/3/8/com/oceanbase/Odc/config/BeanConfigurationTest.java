package com.oceanbase.odc.config;

import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
    
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfiguration;
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup() {
        beanConfiguration = new BeanConfiguration();
        sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
    }

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        assertNotNull(passwordEncoder);
        assertThat(passwordEncoder, instanceOf(BCryptPasswordEncoder.class));
    }

    @Test
    public void objectMapperTest() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void defaultHttpFirewallTest() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();

        assertNotNull(httpFirewall);
        assertThat(httpFirewall.getClass().getName(), is("org.springframework.security.web.firewall.DefaultHttpFirewall"));
    }

    @Test
    public void cloudMetadataClientTest() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();

        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
    }

    @Test
    public void cookieSerializerTest() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();

        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    /* Edge cases and exception handling scenarios not applicable because all the methods
     * in BeanConfiguration class are creating new object instances or returning new object
     * concrete types without any conditional logic or exceptions throws.
     */
}
