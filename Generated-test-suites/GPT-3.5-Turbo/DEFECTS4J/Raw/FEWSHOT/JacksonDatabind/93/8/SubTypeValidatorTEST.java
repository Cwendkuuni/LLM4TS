
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Provide a valid type
        assertFalse(subTypeValidator.validateSubType(ctxt, type));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Provide an invalid type
        assertTrue(subTypeValidator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubType_withIllegalClassNames() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Set some illegal class names
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        illegalClassNames.add("org.apache.commons.collections.functors.InstantiateTransformer");
        subTypeValidator.setIllegalClassNames(illegalClassNames);

        // Provide an invalid type with illegal class names
        assertTrue(subTypeValidator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubType_withPrefixString() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;

        // Set a type with prefix string
        Class<?> raw = Object.class; // replace with a concrete class
        type = JavaType.construct(raw);

        assertTrue(subTypeValidator.validateSubType(ctxt, type));
    }
}
