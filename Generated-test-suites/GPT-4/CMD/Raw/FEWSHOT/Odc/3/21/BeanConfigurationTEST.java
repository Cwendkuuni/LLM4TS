package com.oceanbase.odc.config;

import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.util.List;

public class BeanConfigurationTest {

    private BeanConfiguration target;

    @Mock
    private BeanConfiguration.NullCloudMetadataClient cloudMetadataClientMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        target = new BeanConfiguration();
    }

    @Test
    public void testPasswordEncoder() throws Exception {
        PasswordEncoder passwordEncoder = target.passwordEncoder();
        Assert.assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testDefaultHttpFirewall() throws Exception {
        HttpFirewall firewall = target.defaultHttpFirewall();
        Assert.assertTrue(firewall instanceof DefaultHttpFirewall);
        Assert.assertTrue(((DefaultHttpFirewall) firewall).isAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() throws Exception {
        BeanConfiguration.NullCloudMetadataClient client = (BeanConfiguration.NullCloudMetadataClient) target.cloudMetadataClient();
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, client.oceanbaseAccessMode());
        Assert.assertFalse(client.needsOBTenantName());
    }

    @Test
    public void testCookieSerializer() throws Exception {
        DefaultCookieSerializer serializer = (DefaultCookieSerializer) target.cookieSerializer();
        Assert.assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListInstances() {
        List<OBInstance> result = cloudMetadataClientMock.listInstances();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListTenants() {
        List<OBTenant> result = cloudMetadataClientMock.listTenants("test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTenant() {
        OBTenant result = cloudMetadataClientMock.getTenant("test", "test");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListDatabaseUsers() {
        List<OBDatabaseUser> result = cloudMetadataClientMock.listDatabaseUsers("test", "test");
    }

    @Test
    public void testGetTenantEndpoint() {
        OBTenantEndpoint result = cloudMetadataClientMock.getTenantEndpoint("test", "test");
        Assert.assertEquals(OceanBaseAccessMode.DIRECT, result.getAccessMode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetSysTenantUser() {
        OBDatabaseUser result = cloudMetadataClientMock.getSysTenantUser("test");
    }
}