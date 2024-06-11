import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testPasswordEncoderMethod() {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);
        String decodedPassword = passwordEncoder.decode(encodedPassword);
        Assertions.assertEquals(password, decodedPassword);
    }

    @Test
    public void testObjectMapperMethod() {
        SensitivePropertyHandler sensitivePropertyHandler = applicationContext.getBean(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

        String sensitiveData = "sensitive data";
        String decryptedData = sensitivePropertyHandler.decrypt(sensitiveData);

        MyTestClass myTestClass = new MyTestClass();
        myTestClass.setSensitiveField(sensitiveData);

        String jsonString = objectMapper.writeValueAsString(myTestClass);
        MyTestClass deserializedObject = objectMapper.readValue(jsonString, MyTestClass.class);
        Assertions.assertEquals(decryptedData, deserializedObject.getSensitiveField());
    }

    @Test
    public void testDefaultHttpFirewallMethod() {
        HttpFirewall defaultHttpFirewall = applicationContext.getBean(HttpFirewall.class);

        String urlWithSpecialChars = "http://example.com/%25";
        boolean allowed = defaultHttpFirewall.isSanitized(urlWithSpecialChars);
        Assertions.assertTrue(allowed);
    }

    @Test
    public void testCloudMetadataClientMethod_NullCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        Assertions.assertFalse(cloudMetadataClient instanceof CloudMetadataClient);
        Assertions.assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
    }

    @Test
    public void testCloudMetadataClientMethod_GetTenant() {
        CloudMetadataClient cloudMetadataClient = applicationContext.getBean(CloudMetadataClient.class);
        String instanceId = "instanceId";
        String tenantId = "tenantId";
        OBTenant expectedTenant = new OBTenant();
        // initialize expectedTenant with necessary fields

        OBTenant actualTenant = cloudMetadataClient.getTenant(instanceId, tenantId);
        Assertions.assertEquals(expectedTenant, actualTenant);
    }

    // Add more test cases here for other methods in BeanConfiguration class

}

class MyTestClass {
    private String sensitiveField;

    public String getSensitiveField() {
        return sensitiveField;
    }

    public void setSensitiveField(String sensitiveField) {
        this.sensitiveField = sensitiveField;
    }
}