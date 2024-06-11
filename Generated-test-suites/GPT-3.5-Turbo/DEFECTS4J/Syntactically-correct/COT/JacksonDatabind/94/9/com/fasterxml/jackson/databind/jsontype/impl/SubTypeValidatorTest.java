
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator = SubTypeValidator.instance();

    @Test
    public void testValidateSubTypeValid() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        Assert.assertDoesNotThrow(() -> subTypeValidator.validateSubType(ctxt, type));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeInvalid() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = new JavaType(Object.class);
        subTypeValidator.validateSubType(ctxt, type);
    }
}
