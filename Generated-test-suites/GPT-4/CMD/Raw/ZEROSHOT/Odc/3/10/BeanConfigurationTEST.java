
package com.oceanbase.odc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.*;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class BeanConfigurationTest {
    private BeanConfiguration beanConfiguration;

    @Before
    public void setUp() {
        this.beanConfiguration = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = beanConfiguration.passwordEncoder();
        Assert.assertNotNull(encoder);
    }

    @Test
    public void testObjectMapperNotNull() {
        ObjectMapper mapper = beanConfiguration.objectMapper(string -> string);
        Assert.assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall firewall = beanConfiguration.defaultHttpFirewall();
        Assert.assertTrue(firewall.checkRequest(new MockHttpServletRequest()).isAllowed());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();
        Assert.assertNotNull(client);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        Assert.assertNotNull(serializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testUnsupportedOperations() {
        CloudMetadataClient client = beanConfiguration.cloudMetadataClient();

        List<OBInstance> instances = client.listInstances();
        List<OBTenant> tenants = client.listTenants("id");
        List<OBDatabaseUser> databaseUsers = client.listDatabaseUsers("id1", "id2");
        OBDatabaseUser sysTenantUser = client.getSysTenantUser("id");
        OBTenant tenant = client.getTenant("id1", "id2");
    }
}

