import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.util.TestUtils.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType validType = mapper.getTypeFactory().constructType(String.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, validType);
        // No exception should be thrown
    }

    @Test
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType illegalType = mapper.getTypeFactory().constructType(SubTypeValidator.class);
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(ctxt, illegalType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check the error message
            String expectedMessage = String.format("Illegal type (%s) to deserialize: prevented for security reasons", SubTypeValidator.class.getName());
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}