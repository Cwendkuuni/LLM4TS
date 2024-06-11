import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        SubTypeValidator validator = SubTypeValidator.instance();
        assertNotNull(validator);
    }

    @Test
    public void testValidateSubTypeValid() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // provide valid DeserializationContext object
        JavaType type = null; // provide valid JavaType object
        assertDoesNotThrow(() -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeIllegal() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // provide valid DeserializationContext object
        JavaType type = null; // provide JavaType object with illegal type
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
        assertEquals("Illegal type to deserialize: prevented for security reasons", exception.getMessage());
    }

}