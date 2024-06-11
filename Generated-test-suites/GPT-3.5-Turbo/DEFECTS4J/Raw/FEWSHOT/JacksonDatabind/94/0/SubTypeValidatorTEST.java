package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {
    private SubTypeValidator validator;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validSubType() {
        try {
            validator.validateSubType(new DeserializationContext(null, null), new JavaType(ValidSubType.class));
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown for a valid sub type");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalSubType() throws JsonMappingException {
        validator.validateSubType(new DeserializationContext(null, null), new JavaType(IllegalSubType.class));
        fail("Exception should be thrown for an illegal sub type");
    }

    public static class ValidSubType { }

    public static class IllegalSubType { }

}