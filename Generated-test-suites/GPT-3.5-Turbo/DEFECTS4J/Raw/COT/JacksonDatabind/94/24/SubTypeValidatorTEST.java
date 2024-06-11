import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();

        DeserializationContext ctxt = null; // initialize with appropriate value
        JavaType type = null; // initialize with appropriate value

        try {
            validator.validateSubType(ctxt, type);
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected exception, test passed
        }
    }

}