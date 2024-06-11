
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class BeanConfigurationTest {
    
    @Mock
    BeanConfiguration beanConfiguration;
    
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    
    @Mock
    DefaultHttpFirewall defaultHttpFirewall;
    
    @Mock
    NullCloudMetadataClient nullCloudMetadataClient;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder pe = beanConfiguration.passwordEncoder();
        assertNotNull(pe);
        assertTrue(pe instanceof BCryptPasswordEncoder);
    }
    
    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall hfw = beanConfiguration.defaultHttpFirewall();
        assertNotNull(hfw);
        assertTrue(hfw instanceof DefaultHttpFirewall);
    }
    
    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClient() {
        assertNull(nullCloudMetadataClient.listInstances());
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        assertNull(nullCloudMetadataClient.listTenants("dummyId"));
    }
    
    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        assertNull(nullCloudMetadataClient.getTenant("dummyId", "dummyTenantId"));
    }
    
    @Test
    public void testIncludeClusterNameForJdbcUsername() {
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }
    
    @Test
    public void testOceanbaseAccessMode() {
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }
    
    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        assertNull(nullCloudMetadataClient.listDatabaseUsers("dummyId", "dummyTenantId"));
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
    }

}
