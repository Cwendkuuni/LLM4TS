package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {
    SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;
        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        DeserializationContext context = null;
        JavaType type = null;
        validator.validateSubType(context, type);
    }
}