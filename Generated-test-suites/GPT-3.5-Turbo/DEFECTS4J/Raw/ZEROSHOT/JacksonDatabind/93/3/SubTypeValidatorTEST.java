package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType type;
    private SubTypeValidator subTypeValidator;

    @BeforeClass
    public void setUp() {
        ctxt = new DeserializationContext();
        type = JavaType.nullType();
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidSubType_ThrowsNoException() {
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testValidateSubType_InvalidSubType_ThrowsException() {
        type = JavaType.fromClass(org.springframework.util.FileSystemUtils.class);
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("Expected exception not thrown");
        } catch (JsonMappingException e) {
            // expected
        }
    }
}