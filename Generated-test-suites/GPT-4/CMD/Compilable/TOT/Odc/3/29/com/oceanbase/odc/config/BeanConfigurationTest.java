package com.oceanbase.odc.config;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BeanConfigurationTest {

    BeanConfiguration config = new BeanConfiguration();

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    // More tests here...
}
