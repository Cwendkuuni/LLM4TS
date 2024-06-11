import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null);
        JavaType type = new JavaType(Object.class);

        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException: " + e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_illegalClass() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null);
        JavaType type = new JavaType(org.apache.commons.collections.functors.InvokerTransformer.class);

        try {
            validator.validateSubType(context, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

}