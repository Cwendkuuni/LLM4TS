
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subtypeValidator;

    @Before
    public void setup() {
        subtypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            subtypeValidator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
