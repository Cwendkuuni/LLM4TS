import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {
   
    @Test
    public void testValidateSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
      
        // Typical use case
        try {
            validator.validateSubType(new DeserializationContext(), new JavaType());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }

        // Edge cases
        try {
            validator.validateSubType(null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
        
        try {
            validator.validateSubType(new DeserializationContext(), new JavaType());
        } catch (JsonMappingException e) {
            assertFalse(e.getMessage().contains("Illegal type"));
        }

        // Error scenarios
        try {
            validator.validateSubType(null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertNotNull(e.getMessage());
        }
    }
}