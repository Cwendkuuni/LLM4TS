import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test(expected = NullPointerException.class)
    public void testValidateSubTypeWithNullDeserializationContext() throws JsonMappingException {
        SubTypeValidator.instance().validateSubType(null, JavaType.builder(Object.class).build());
    }

    @Test(expected = NullPointerException.class)
    public void testValidateSubTypeWithNullJavaType() throws JsonMappingException {
        SubTypeValidator.instance().validateSubType(DeserializationContext.builder().build(), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithIllegalClassName() throws JsonMappingException {
        JavaType illegalType = JavaType.builder(InvokerTransformer.class).build();
        SubTypeValidator.instance().validateSubType(DeserializationContext.builder().build(), illegalType);
    }

    @Test
    public void testValidateSubTypeWithInterface() throws JsonMappingException {
        JavaType interfaceType = JavaType.builder(CharSequence.class).build();
        SubTypeValidator.instance().validateSubType(DeserializationContext.builder().build(), interfaceType);
        // No exception should be thrown
    }
}