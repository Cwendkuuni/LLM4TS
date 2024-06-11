
package com.oceanbase.odc.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        BeanConfiguration config = new BeanConfiguration();
        Assert.assertNotNull(config.passwordEncoder());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectMapperNull() {
        BeanConfiguration config = new BeanConfiguration();
        config.objectMapper(null);
    }

    @Test
    public void testObjectMapper() {
        BeanConfiguration config = new BeanConfiguration();
        SensitivePropertyHandler handler = new SensitivePropertyHandler();
        Assert.assertNotNull(config.objectMapper(handler));
    }
}
