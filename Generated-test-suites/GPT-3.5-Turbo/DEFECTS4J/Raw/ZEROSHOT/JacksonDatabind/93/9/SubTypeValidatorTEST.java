package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private static SubTypeValidator subTypeValidator;

    @BeforeClass
    public static void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validSubType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            Assert.fail("Should not have thrown JsonMappingException");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidSubType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        subTypeValidator.validateSubType(ctxt, type);
    }
}