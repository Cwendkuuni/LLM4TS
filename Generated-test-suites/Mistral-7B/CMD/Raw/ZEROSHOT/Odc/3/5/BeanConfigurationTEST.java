package com.oceanbase.odc.config;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerImpl;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Autowired
    private AnnotationConfigApplicationContext context;

    @InjectMocks
    private BeanConfiguration config;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        BCryptPasswordEncoder expectedPasswordEncoder = new BCryptPasswordEncoder();
        assertEquals(expectedPasswordEncoder, passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        CustomOutputSerializer customOutputSerializer = Mockito.spy(new CustomOutputSerializer());
        I18nOutputSerializer i18nOutputSerializer = Mockito.spy(new I18nOutputSerializer());
        Mockito.doReturn(i18nOutputSerializer).when(customOutputSerializer).getSerializer(Internationalizable.class);
        JacksonModules jacksonModules = Mockito.spy(new JacksonModules());
        Mockito.doReturn(jacksonModules).when(config).getJacksonModules();
        Mockito.doReturn(customOutputSerializer).when(jacksonModules).customOutputHandling();
        ObjectMapper expectedObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(Mockito.mock(SensitivePropertyHandler.class)::decrypt))
                .registerModule(jacksonModules);
        assertEquals(expectedObjectMapper, objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = context.getBean(HttpFirewall.class);
        DefaultHttpFirewall expectedDefaultHttpFirewall = new DefaultHttpFirewall();
        expectedDefaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertEquals(expectedDefaultHttpFirewall, defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
        NullCloudMetadataClient expectedNullCloudMetadataClient = new NullCloudMetadataClient();
        assertEquals(expectedNullCloudMetadataClient, cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = context.getBean(CookieSerializer.class);
        DefaultCookieSerializer expectedDefaultCookieSerializer = new DefaultCookieSerializer();
        assertEquals(expectedDefaultCookieSerializer, cookieSerializer);
    }

    @Test
    public void testBeanDefinitionExists() {
        BeanDefinition beanDefinition = context.getBeanDefinition("passwordEncoder");
        assertNotNull(beanDefinition);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBeanWithNullName() {
        context.getBean(null);
    }

    @Test
    public void testGetBeanWithNonExistentName() {
        assertNull(context.getBean("nonExistentBean"));
    }

}