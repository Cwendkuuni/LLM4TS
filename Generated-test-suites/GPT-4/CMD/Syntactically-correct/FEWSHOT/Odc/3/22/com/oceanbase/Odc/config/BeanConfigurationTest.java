
package com.oceanbase.odc.config;

import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Before
    public void setup(){
        //Any setup code goes here
    }

    @Test
    public void passwordEncoderTest() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void objectMapperTest() {
        assertEquals(beanConfiguration.objectMapper(sensitivePropertyHandler).getClass(), org.codehaus.jackson.map.ObjectMapper.class);
        assertNotNull(JacksonModules.CustomOutputSerializer.addSerializer(Internationalizable.class, new I18nOutputSerializer()));
    }

    @Test
    public void defaultHttpFirewallTest() {
        assertTrue(beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
    }

    @Test
    public void cloudMetadataClientTest() {
        assertTrue(beanConfiguration.cloudMetadataClient() instanceof CloudMetadataClient);
    }
}
