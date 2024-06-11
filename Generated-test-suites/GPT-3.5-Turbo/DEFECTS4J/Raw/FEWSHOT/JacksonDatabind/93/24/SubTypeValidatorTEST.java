package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator validator = SubTypeValidator.instance();
    private DeserializationContext ctxt;
    private JavaType type;

    @Test
    public void testValidateSubType_ValidType() throws JsonMappingException {
        type = JavaType.construct(ClassA.class);
        validator.validateSubType(ctxt, type);
        type = JavaType.construct(ClassB.class);
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_InvalidType() throws JsonMappingException {
        type = JavaType.construct(ClassC.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.ClassC) to deserialize: prevented for security reasons", e.getMessage());
        }
        type = JavaType.construct(ClassD.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.ClassD) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    private static class ClassA {}
    private static class ClassB {}
    private static class ClassC {}
    private static class ClassD {}
}