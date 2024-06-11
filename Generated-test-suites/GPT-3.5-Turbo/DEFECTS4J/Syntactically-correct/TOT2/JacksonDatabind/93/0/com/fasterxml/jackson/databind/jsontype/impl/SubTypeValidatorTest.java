import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        validator.validateSubType(ctxt, type);

        // No exception thrown means the validation passed
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        // Set the type to be an illegal class
        // for test purposes
        type = JavaType.construct(String.class);

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_SpringFrameworkClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        // Set the type to be a Spring framework class
        JavaType.construct(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);

        validator.validateSubType(ctxt, type);

        // No exception thrown means the validation passed
    }
}