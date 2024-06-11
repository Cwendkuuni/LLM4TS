package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void objectMapperTest() {
        assertNotNull(beanConfiguration.objectMapper(sensitivePropertyHandler));
    }

    @Test
    public void defaultHttpFirewallTest() {
        assertNotNull(beanConfiguration.defaultHttpFirewall());
    }

    @Test
    public void cloudMetadataClientTest() {
        try {
            assertNotNull(beanConfiguration.cloudMetadataClient());
        } catch (NoUniqueBeanDefinitionException e) {
            // expected
        }
    }

    @Test
    public void cookieSerializerTest() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", ((DefaultCookieSerializer) cookieSerializer).getCookieName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullCloudMetadataClient() {
        BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        nullCloudMetadataClient.listInstances();
        nullCloudMetadataClient.listTenants("");
        nullCloudMetadataClient.getTenant("", "");

        try {
            nullCloudMetadataClient.listDatabaseUsers("", "");
        } catch (UnsupportedOperationException e){
            //expected
        }

        OBDatabaseUser obDatabaseUser = nullCloudMetadataClient.getSysTenantUser("");
    }
}