import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {
    
    @Test
    public void testValidateSubType_validSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType();
        type.setRawClass(String.class);
        
        validator.validateSubType(ctxt, type); // this should pass without any exceptions
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = new JavaType();
        type.setRawClass(Object.class);
        
        validator.validateSubType(ctxt, type); // this should throw a JsonMappingException
    }
    
}