package com.oceanbase.odc.config;
import static org.junit.Assert.*;

import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();

        String password = "test";
        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(null);

        assertNotNull(objectMapper);
    }

    // Add additional tests here...
}