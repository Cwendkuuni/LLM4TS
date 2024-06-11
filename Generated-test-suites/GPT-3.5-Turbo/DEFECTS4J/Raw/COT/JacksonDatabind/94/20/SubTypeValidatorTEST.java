import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_withValidType_shouldNotThrowException() {
        DeserializationContext context = null;
        JavaType type = null;

        try {
            subTypeValidator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            Assert.fail("An unexpected JsonMappingException is thrown.");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_withInvalidType_shouldThrowException() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;

        subTypeValidator.validateSubType(context, type);
    }
}