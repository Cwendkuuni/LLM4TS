
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidSubType_NoException() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mock DeserializationContext
        JavaType subType = null; // Mock valid subType

        try {
            validator.validateSubType(ctxt, subType);
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidSubType_JsonMappingException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mock DeserializationContext
        JavaType subType = null; // Mock invalid subType

        validator.validateSubType(ctxt, subType);
    }

    @Test
    public void testValidateSubType_IllegalClassNames_JsonMappingException() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null; // Mock DeserializationContext

        for (String illegalClassName : validator._cfgIllegalClassNames) {
            try {
                validator.validateSubType(ctxt, JavaType.builder().rawClass(Class.forName(illegalClassName)).build());
                fail("Expected JsonMappingException");
            } catch (JsonMappingException e) {
                assertEquals("Illegal type (" + illegalClassName + ") to deserialize: prevented for security reasons",
                        e.getMessage());
            } catch (ClassNotFoundException e) {
                fail("Illegal class name: " + illegalClassName);
            }
        }
    }

}
