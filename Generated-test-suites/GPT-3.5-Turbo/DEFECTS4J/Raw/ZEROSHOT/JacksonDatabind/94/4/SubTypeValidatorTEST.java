import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    private DeserializationContext ctxt;
    private JavaType type;
    private SubTypeValidator validator;
    
    @Before
    public void setUp() {
        ctxt = new DeserializationContext();
        type = JavaType.fromClass(String.class);
        validator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType_validType() {
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        JavaType illegalType = JavaType.fromClass(org.apache.commons.collections.functors.InvokerTransformer.class);
        validator.validateSubType(ctxt, illegalType);
    }
}