import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType(MyClass.class);
        
        SubTypeValidator validator = SubTypeValidator.instance();
        
        validator.validateSubType(ctxt, type);
        
        assertTrue(true);
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType() {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType(DangerousClass.class);
        
        SubTypeValidator validator = SubTypeValidator.instance();
        
        validator.validateSubType(ctxt, type);
    }
}