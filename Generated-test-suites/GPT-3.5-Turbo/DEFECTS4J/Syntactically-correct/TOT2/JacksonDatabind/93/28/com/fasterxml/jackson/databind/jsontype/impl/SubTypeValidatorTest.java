import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() {
        // Given
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = new JavaType(ValidClass.class);

        // When
        validator.validateSubType(ctxt, type);

        // Then
        // No exception should be thrown
    }

    @Test
    public void testValidateSubType_illegalClass() {
        // Given
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = new JavaType(IllegalClass.class);

        // When
        try {
            validator.validateSubType(ctxt, type);
            fail("JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            // Then
            assertEquals("Illegal type (org.springframework.IllegalClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    // Add more test cases for edge cases and exception handling scenarios

}