import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    // Basic test case for validateSubType method
    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        // Testing a valid type
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Testing an illegal type
        type = mock(JavaType.class);
        when(type.getRawClass()).thenReturn(Object.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}