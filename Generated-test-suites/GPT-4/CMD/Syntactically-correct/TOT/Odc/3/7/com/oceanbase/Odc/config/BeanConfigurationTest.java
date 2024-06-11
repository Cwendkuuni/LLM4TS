package com.oceanbase.odc.config;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class BeanConfigurationTest {

    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void passwordEncoder() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void objectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler= new SensitivePropertyHandlerStub();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);
    }

    @Test
    public void defaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(firewall);
    }

    @Test
    public void cloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        assertNotNull(client);
    }

    @Test
    public void cookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertNotNull(serializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer)serializer).getCookieName());
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupportedCloudMetadataMethods() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        client.listInstances();
        client.listTenants(null);
        client.getTenant(null, null);
        client.listDatabaseUsers(null, null);
        client.getSysTenantUser(null);
    }
}

class SensitivePropertyHandlerStub implements SensitivePropertyHandler {
    @Override
    public String decrypt(String encryptedText) {
        return encryptedText;
    }

    @Override
    public String encrypt(String plainText) {
        return plainText;
    }
}