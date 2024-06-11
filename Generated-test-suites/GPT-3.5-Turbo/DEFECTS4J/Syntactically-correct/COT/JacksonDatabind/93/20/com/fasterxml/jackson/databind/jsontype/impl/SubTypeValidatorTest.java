
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidClass_NoException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        validator.validateSubType(ctxt, type);
        
        assertEquals("SubTypeValidator should throw an exception for a valid class", true);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidClass_ThrowException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> raw = mock(Class.class);
        when(raw.getName()).thenReturn("invalidClass");
        when(type.getRawClass()).thenReturn(raw);

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_SpringFrameworkInterface_NoException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Class<?> raw = mock(Class.class);
        when(raw.getName()).thenReturn("org.springframework.SomeInterface");
        when(type.getRawClass()).thenReturn(raw);

        validator.validateSubType(ctxt, type);
        
        assertEquals("SubTypeValidator should throw an exception for Spring Framework interface", true);
    }
}
