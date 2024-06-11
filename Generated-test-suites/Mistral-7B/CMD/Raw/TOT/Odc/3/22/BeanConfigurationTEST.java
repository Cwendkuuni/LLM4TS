import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.NullCloudMetadataClient;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@SpringJUnitConfig(classes = BeanConfiguration.class)
@WebAppConfiguration
@SpringBootTest
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    private SensitivePropertyHandler sensitivePropertyHandler;
    private CloudMetadataClient cloudMetadataClient;

    @BeforeEach
    public void setup() {
        sensitivePropertyHandler = Mockito.spy(applicationContext.getBean(SensitivePropertyHandler.class));
        cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        Assertions.assertNotNull(encodedPassword);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        Assertions.assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);
        String testString = "{\"name\":\"John\",\"age\":30}";
        Internationalizable testObject = new TestInternationalizable("John", 30);
        String jsonString = objectMapper.writeValueAsString(testObject);
        Assertions.assertEquals(testString, jsonString);
        TestInternationalizable deserializedObject = objectMapper.readValue(testString, TestInternationalizable.class);
        Assertions.assertEquals(testObject.getName(), deserializedObject.getName());
        Assertions.assertEquals(testObject.getAge(), deserializedObject.getAge());
    }

    @Test
    public void testDefaultHttpFirewall() {
        //TODO: Add test cases for special characters in URL
    }

    @Test
    public void testNullCloudMetadataClient() {
        Assertions.assertFalse(cloudMetadataClient.supportsCloudMetadata());
        Assertions.assertFalse(cloudMetadataClient.supportsTenantInstance());
        Assertions.assertFalse(cloudMetadataClient.needsOBTenantName());
        Assertions.assertFalse(cloudMetadataClient.needsSysTenantUser());
        Assertions.assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());
        OceanBaseAccessMode accessMode = cloudMetadataClient.oceanbaseAccessMode();
        Assertions.assertEquals(OceanBaseAccessMode.DIRECT, accessMode);
        Assertions.assertNull(cloudMetadataClient.listInstances());
        Assertions.assertNull(cloudMetadataClient.listTenants(""));
        Assertions.assertNull(cloudMetadataClient.getTenant("", ""));
        Assertions.assertNull(cloudMetadataClient.listDatabaseUsers("", ""));
        Assertions.assertNull(cloudMetadataClient.getSysTenantUser(""));
    }
}

class TestInternationalizable implements Internationalizable {
    private String name;
    private int age;

    public TestInternationalizable(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }
}

@Configuration
public static class TestConfiguration {

    @Bean
    public SensitivePropertyHandler sensitivePropertyHandler() {
        return Mockito.mock(SensitivePropertyHandler.class);
    }
}