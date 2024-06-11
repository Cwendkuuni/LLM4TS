package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType() throws Exception {
        DeserializationContext ctxt = null;
        JavaType javaType = null;
        Set<String> cfgIllegalClassNames = new HashSet<>();

        // Test case for a valid subtype
        javaType = JavaType.class;
        try {
            subTypeValidator.validateSubType(ctxt, javaType);
        } catch (JsonMappingException e) {
            Assert.fail("Should not have thrown an exception");
        }

        // Test case for an invalid subtype
        javaType = new JavaType(SubTypeValidator.class);
        try {
            subTypeValidator.validateSubType(ctxt, javaType);
            Assert.fail("Should have thrown an exception");
        } catch (JsonMappingException e) {
            Assert.assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}