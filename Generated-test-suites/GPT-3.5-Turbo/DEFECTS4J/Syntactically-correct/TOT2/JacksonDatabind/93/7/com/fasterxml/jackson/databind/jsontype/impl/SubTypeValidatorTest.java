import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType_validType_noExceptionThrown() {
        SubTypeValidator instance = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        assertDoesNotThrow(() -> instance.validateSubType(ctxt, type));
    }
    
    @Test
    public void testValidateSubType_illegalType_throwJsonMappingException() {
        SubTypeValidator instance = SubTypeValidator.instance();
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        
        // Set mock type to be an illegal class name
        when(type.getRawClass().getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");
        
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> instance.validateSubType(ctxt, type));
        assertTrue(exception.getMessage().contains("Illegal type"));
    }
}