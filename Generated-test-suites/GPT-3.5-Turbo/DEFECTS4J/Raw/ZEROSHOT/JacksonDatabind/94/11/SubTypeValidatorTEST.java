package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidInput_NoException() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with actual DeserializationContext instance
        JavaType type = null; // replace with actual JavaType instance

        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidInput_ThrowsException() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with actual DeserializationContext instance
        JavaType type = null; // replace with actual JavaType instance

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_CheckIllegalClassName_NotPresent() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with actual DeserializationContext instance
        JavaType type = null; // replace with actual JavaType instance

        Set<String> illegalClassNames = new HashSet<>(validator.DEFAULT_NO_DESER_CLASS_NAMES);
        illegalClassNames.remove("org.springframework.beans.factory.ObjectFactory");
        validator._cfgIllegalClassNames = illegalClassNames;

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_CheckIllegalClassName_Present() throws JsonMappingException {
        DeserializationContext ctxt = null; // replace with actual DeserializationContext instance
        JavaType type = null; // replace with actual JavaType instance

        Set<String> illegalClassNames = new HashSet<>(validator.DEFAULT_NO_DESER_CLASS_NAMES);
        illegalClassNames.add("org.springframework.beans.factory.ObjectFactory");
        validator._cfgIllegalClassNames = illegalClassNames;

        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}