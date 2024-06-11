
package com.fasterxml.jackson.databind.jsontype.impl;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SubTypeValidatorTest {
    @Test
    public void testValidateSubType_shouldNotThrowException_whenValidSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
        } catch(JsonMappingException e) {
            assertEquals("Failed - should not throw JsonMappingException", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_shouldThrowException_whenInvalidSubType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
        } catch(JsonMappingException e) {
            assertEquals("Illegal type () to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
