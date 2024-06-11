import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    // Stubbed objects for the tests
    private DeserializationContext ctxt = mock(DeserializationContext.class);
    private JavaType type = mock(JavaType.class);

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator._cfgIllegalClassNames.add(type.getRawClass().getName());
        validator.validateSubType(ctxt, type);
        // Expecting JsonMappingException to be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator._cfgIllegalClassNames.add("com.example.IllegalSubType");
        validator.validateSubType(ctxt, type.withRawClass(IllegalSubType.class));
        // Expecting JsonMappingException to be thrown
    }

    @Test
    public void testValidateSubType_SpringFrameworkClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type.withRawClass(AbstractBeanFactoryPointcutAdvisor.class));
        // No exception should be thrown
    }
}