import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

public class SubTypeValidatorTest {
    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void validateSubType_ValidType_NoExceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        validator.validateSubType(ctxt, type);
        
        assertTrue(true);
    }
    
    @Test
    public void validateSubType_InvalidType_ExceptionThrown() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch(JsonMappingException e) {
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}