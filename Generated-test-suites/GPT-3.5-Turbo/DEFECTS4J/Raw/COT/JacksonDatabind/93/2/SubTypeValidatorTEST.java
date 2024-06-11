
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType_NoException() {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        try {
            validator.validateSubType(null, new JavaType());
            // If no exception is thrown, the test passes
            assertTrue(true);
        } catch (Exception e) {
            // If an exception is thrown, the test fails
            fail("Exception thrown");
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType_ExceptionThrown() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        
        validator.validateSubType(null, new JavaType(Object.class));
        
        // If an exception is thrown, the test passes
        assertTrue(true);
    }
    
}
