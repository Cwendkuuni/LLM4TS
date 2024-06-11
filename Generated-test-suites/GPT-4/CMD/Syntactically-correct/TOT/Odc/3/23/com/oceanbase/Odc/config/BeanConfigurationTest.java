package com.oceanbase.odc.config;
import com.oceanbase.odc.config.BeanConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testPasswordEncoderBeanExistence() {
        assertTrue(context.containsBean("passwordEncoder"));
        assertTrue(context.getBean("passwordEncoder") instanceof PasswordEncoder);
    }

    @Test
    public void testObjectMapperBeanExistence() {
        assertTrue(context.containsBean("objectMapper"));
    }
}