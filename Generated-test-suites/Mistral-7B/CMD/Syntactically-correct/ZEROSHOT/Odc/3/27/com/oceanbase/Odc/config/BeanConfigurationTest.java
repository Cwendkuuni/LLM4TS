package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.config.BeanConfiguration.NullCloudMetadataClient;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testPasswordEncoderBean() {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapperBean() {
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        assertNotNull(objectMapper);

        SensitivePropertyHandler sensitivePropertyHandler = Mockito.mock(SensitivePropertyHandler.class);
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper actualObjectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(actualObjectMapper);
        assertNotNull(actualObjectMapper.getSerializerProvider().getSerializers());
        assertNotNull(actualObjectMapper.getDeserializerProvider().getDeserializers());
    }

    @Test
    public void testDefaultHttpFirewallBean() {
        HttpFirewall httpFirewall = context.getBean(HttpFirewall.class);
        assertNotNull(httpFirewall);
    }

    @Test
    public void testCloudMetadataClientBean() {
        CloudMetadataClient cloudMetadataClient = context.getBean(CloudMetadataClient.class);
        assertNotNull(cloudMetadataClient);
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializerBean() {
        CookieSerializer cookieSerializer = context.getBean(CookieSerializer.class);
        assertNotNull(cookieSerializer);
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
    }

}