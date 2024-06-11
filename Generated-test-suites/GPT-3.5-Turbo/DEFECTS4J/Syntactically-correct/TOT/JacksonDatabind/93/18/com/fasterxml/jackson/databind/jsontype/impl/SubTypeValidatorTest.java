import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType javaType;

    @Before
    public void setup() {
        ctxt = mock(DeserializationContext.class);
        javaType = mock(JavaType.class);
    }
    
    @Test
    public void testValidateSubType_ValidClass_NoExceptionThrown() {
        // valid class that should not throw any exception
        when(javaType.getRawClass()).thenReturn(Object.class);

        SubTypeValidator.instance().validateSubType(ctxt, javaType);

        // verify that no exception is thrown
        verifyNoInteractions(ctxt);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass_ExceptionThrown() {
        // illegal class that should throw JsonMappingException
        when(javaType.getRawClass()).thenReturn(String.class);

        SubTypeValidator.instance().validateSubType(ctxt, javaType);
    }
    
    @Test
    public void testValidateSubType_SpringAbstractBeanFactoryPointcutAdvisor_ExceptionThrown() {
        // class that starts with prefix "org.springframework." and is AbstractBeanFactoryPointcutAdvisor
        // should throw JsonMappingException
        when(javaType.getRawClass()).thenReturn(ExampleAbstractBeanFactoryPointcutAdvisor.class);

        SubTypeValidator.instance().validateSubType(ctxt, javaType);

        // verify that JsonMappingException is thrown
        verify(ctxt).reportInputMismatch(javaType, "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest$ExampleAbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons");
    }
    
    @Test
    public void testValidateSubType_SpringAbstractApplicationContext_ExceptionThrown() {
        // class that starts with prefix "org.springframework." and is AbstractApplicationContext
        // should throw JsonMappingException
        when(javaType.getRawClass()).thenReturn(ExampleAbstractApplicationContext.class);

        SubTypeValidator.instance().validateSubType(ctxt, javaType);

        // verify that JsonMappingException is thrown
        verify(ctxt).reportInputMismatch(javaType, "Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest$ExampleAbstractApplicationContext) to deserialize: prevented for security reasons");
    }
    
    static class ExampleAbstractBeanFactoryPointcutAdvisor {}
    
    static class ExampleAbstractApplicationContext {}

}