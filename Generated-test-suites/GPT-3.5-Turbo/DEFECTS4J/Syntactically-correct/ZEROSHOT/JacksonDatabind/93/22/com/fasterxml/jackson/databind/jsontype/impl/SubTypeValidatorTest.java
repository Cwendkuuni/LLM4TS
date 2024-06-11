package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.builder().build();
        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = JavaType.builder().rawClass(String.class).build();
        validator.validateSubType(ctxt, type);
    }

}