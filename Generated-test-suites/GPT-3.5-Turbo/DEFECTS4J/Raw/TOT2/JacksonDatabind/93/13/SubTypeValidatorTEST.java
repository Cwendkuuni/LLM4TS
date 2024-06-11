import static org.junit.Assert.*;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() {
        // Test when a valid type is passed as input
        // Expected behavior is no exception thrown
        // Sample valid JavaType
        JavaType validType = new JavaType();

        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(new DeserializationContext(), validType);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown for a valid type");
        }
    }

    @Test
    public void testValidateSubType_invalidType() {
        // Test when an invalid type is passed as input
        // Expected behavior is JsonMappingException thrown
        // Sample invalid JavaType with a "nasty class" name
        JavaType invalidType = new JavaType("org.apache.commons.collections.functors.InvokerTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(new DeserializationContext(), invalidType);
        });
    }

    @Test
    public void testValidateSubType_springFrameworkType() {
        // Test when a Spring framework type that should be blocked is passed as input
        // Expected behavior is JsonMappingException thrown
        // Sample Spring framework type
        JavaType springType = new JavaType("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> {
            validator.validateSubType(new DeserializationContext(), springType);
        });
    }

    @Test
    public void testValidateSubType_interfaceType() {
        // Test when an interface type is passed as input
        // Expected behavior is no exception thrown
        // Sample interface JavaType
        JavaType interfaceType = new JavaType("java.util.List");

        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(new DeserializationContext(), interfaceType);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown for an interface type");
        }
    }

    @Test
    public void testValidateSubType_nullType() {
        // Test when a null type is passed as input
        // Expected behavior is IllegalArgumentException thrown
        JavaType nullType = null;

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(IllegalArgumentException.class, () -> {
            validator.validateSubType(new DeserializationContext(), nullType);
        });
    }
}