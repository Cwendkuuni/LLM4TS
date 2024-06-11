import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mock deserialization context
        JavaType type = null; // Mock JavaType

        // Typical use case
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (%s) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Edge cases
        // Test with a class name that is in the DEFAULT_NO_DESER_CLASS_NAMES set
        type = mockJavaType("org.apache.commons.collections.functors.InvokerTransformer");
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Test with a Spring class name that should be blocked
        type = mockJavaType("org.springframework.beans.factory.ObjectFactory");
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Error scenario
        // Test with null DeserializationContext
        ctxt = null;
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNotNull(e);
        }
    }

    private JavaType mockJavaType(String className) {
        // Mock JavaType object with the given class name
        return new JavaType(null, Class.forName(className), null, null, null, null, false);
    }
}