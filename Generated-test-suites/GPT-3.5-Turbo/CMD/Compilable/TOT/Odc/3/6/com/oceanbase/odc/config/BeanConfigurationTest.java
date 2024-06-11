package com.oceanbase.odc.config;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BeanConfiguration().passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

}