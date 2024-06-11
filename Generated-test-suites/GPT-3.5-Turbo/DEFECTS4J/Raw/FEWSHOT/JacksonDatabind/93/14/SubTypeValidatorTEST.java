import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        // Test 1: Test when the class name is in DEFAULT_NO_DESER_CLASS_NAMES set
        DeserializationContext context1 = null; // initialize with appropriate value
        JavaType type1 = null; // initialize with appropriate value
        try {
            validator.validateSubType(context1, type1);
            fail("Exception not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (" + type1.getRawClass().getName() + ") to deserialize: prevented for security reasons", e.getMessage());
        }
        
        // Test 2: Test when the class name starts with PREFIX_STRING
        DeserializationContext context2 = null; // initialize with appropriate value
        JavaType type2 = null; // initialize with appropriate value
        try {
            validator.validateSubType(context2, type2);
            fail("Exception not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (" + type2.getRawClass().getName() + ") to deserialize: prevented for security reasons", e.getMessage());
        }
        
        // Test 3: Test when the class name does not meet any of the condition above
        DeserializationContext context3 = null; // initialize with appropriate value
        JavaType type3 = null; // initialize with appropriate value
        try {
            validator.validateSubType(context3, type3);
        } catch (JsonMappingException e) {
            fail("Exception thrown");
        }
    }
}