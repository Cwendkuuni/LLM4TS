
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;
    private DeserializationContext ctxt;

    @Before
    public void setup() {
        validator = SubTypeValidator.instance();
        ctxt = mock(DeserializationContext.class);
    }

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        JavaType validType = mock(JavaType.class);
        validator.validateSubType(ctxt, validType);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        JavaType illegalType = mock(JavaType.class);
        when(illegalType.getRawClass()).thenReturn(String.class);
        validator.validateSubType(ctxt, illegalType);
        // JsonMappingException should be thrown
    }
}