package com.oceanbase.odc.config;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.common.json.ObjectMapperTestUtil;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerTest;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandlerTest.TestSensitivePropertyHandler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
public class BeanConfigurationTest {

    @Autowired
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = new TestSensitivePropertyHandler();
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        ObjectMapperTestUtil.assertJacksonMapper(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).listInstances();
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).listTenants(Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).getTenant(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).listDatabaseUsers(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).getTenantEndpoint(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(cloudMetadataClient, Mockito.times(0)).getSysTenantUser(Mockito.anyString());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).listInstances();
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).listTenants(Mockito.anyString());
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).getTenant(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).listDatabaseUsers(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).getTenantEndpoint(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(nullCloudMetadataClient, Mockito.times(0)).getSysTenantUser(Mockito.anyString());
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        nullCloudMetadataClient.getTenant(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        OBTenantEndpoint tenantEndpoint = nullCloudMetadataClient.getTenantEndpoint(Mockito.anyString(), Mockito.anyString());
        assertNotNull(tenantEndpoint);
        Mockito.verify(tenantEndpoint, Mockito.times(0)).setAccessMode(Mockito.any(OceanBaseAccessMode.class));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.nullCloudMetadataClient();
        List<OBDatabaseUser> databaseUsers = nullCloudMetadataClient.listDatabaseUsers(Mockito.anyString(), Mockito.anyString());
        assertNotNull(databaseUsers);
        Mockito.verify(databaseUsers, Mockito.times(0)).setInstanceId(Mockito.anyString());
        Mockito.verify(databaseUsers, Mockito.times(0)).setTenantId(Mockito.anyString());
        Mockito.verify(databaseUsers, Mockito.times(0)).setUsername(Mockito.anyString());
        Mockito.verify(databaseUsers, Mockito.times(0)).setPassword(Mockito.anyString());
        Mockito.verify(databaseUsers, Mockito.times(0)).setRole(Mockito.anyString());
        Mockito.verify(databaseUsers, Mockito.times(0)).setAccessMode(Mockito.any(OceanBaseAccessMode.class));
    }

    @Test
    public void testObjectMapperCustomOutputSerializer() {
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
        ObjectMapper objectMapperCustomOutputSerializer = objectMapper
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        ObjectMapperTestUtil.assertJacksonMapper(objectMapperCustomOutputSerializer);
        Mockito.verify(customOutputSerializer, Mockito.times(0)).serialize(Mockito.anyObject(), Mockito.any());
    }

    @Test
    public void testObjectMapperCustomOutputSerializerI18nOutputSerializer() {
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper();
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer();
        ObjectMapper objectMapperCustomOutputSerializer = objectMapper
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer))
                .registerModule(new I18nOutputSerializer());
        ObjectMapperTestUtil.assertJacksonMapper(objectMapperCustomOutputSerializer);
        Mockito.verify(customOutputSerializer, Mockito.times(0)).serialize(Mockito.anyObject(), Mockito.any());
        Mockito.verify(I18nOutputSerializer.instance, Mockito.times(1)).serialize(Mockito.anyObject(), Mockito.any());
    }

    @Configuration
    static class TestConfig {

        @Bean
        public SensitivePropertyHandler sensitivePropertyHandler() {
            return new TestSensitivePropertyHandler();
        }

        @Bean
        public ObjectMapper objectMapper(SensitivePropertyHandler sensitivePropertyHandler) {
            return beanConfiguration.objectMapper(sensitivePropertyHandler);
        }

        @Bean
        public DefaultHttpFirewall defaultHttpFirewall() {
            return beanConfiguration.defaultHttpFirewall();
        }

        @Bean
        public CloudMetadataClient cloudMetadataClient() {
            return Mockito.mock(CloudMetadataClient.class);
        }

        @Bean
        public NullCloudMetadataClient nullCloudMetadataClient() {
            return beanConfiguration.nullCloudMetadataClient();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return beanConfiguration.passwordEncoder();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public CookieSerializer cookieSerializer() {
            return beanConfiguration.cookieSerializer();
        }

        @Bean
        public DefaultCookieSerializer defaultCookieSerializer() {
            return new DefaultCookieSerializer();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return JacksonFactory.unsafeJsonMapper();
        }

        @Bean
        public ObjectMapper objectMapperCustomOutputSerializer() {
            return JacksonFactory.unsafeJsonMapper()
                    .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()));
        }

        @Bean
        public ObjectMapper objectMapperI18nOutputSerializer() {
            return JacksonFactory.unsafeJsonMapper()
                    .registerModule(JacksonModules.customOutputHandling(new I18nOutputSerializer()));
        }

        @Bean
        public ObjectMapper objectMapperSensitiveInputHandling() {
            return JacksonFactory.unsafeJsonMapper()
                    .registerModule(JacksonModules.sensitiveInputHandling(new TestSensitivePropertyHandler()));
        }
    }
}

class TestSensitivePropertyHandler implements SensitivePropertyHandler {

    @Override
    public String decrypt(String encrypted) {
        return encrypted;
    }
}