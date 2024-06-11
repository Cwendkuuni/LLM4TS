
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeValidType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = JavaType.builder().rawClass(Object.class).build();
        DeserializationContext ctxt = null;
        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeInvalidTypeThrowsException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        JavaType type = JavaType.builder().rawClass(SubTypeValidator.class).build();
        DeserializationContext ctxt = null;
        validator.validateSubType(ctxt, type);
    }
}
