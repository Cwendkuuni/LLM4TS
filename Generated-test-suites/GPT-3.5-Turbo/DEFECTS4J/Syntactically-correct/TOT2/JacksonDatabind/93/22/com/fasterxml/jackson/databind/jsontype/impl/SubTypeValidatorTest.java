package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;
    private DeserializationContext ctxt;
    private JavaType type;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
        ctxt = new DeserializationContext(null, null);
        type = JavaType.constructUnsafe(Object.class);
    }

    @Test
    public void testInstance() {
        assertNotNull(validator);
    }

    @Test
    public void testValidateSubTypeValid() throws JsonMappingException {
        validator.validateSubType(ctxt, type);
        // Should not throw an exception
    }

    @Test
    public void testValidateSubTypeInvalid() {
        JavaType illegalType = JavaType.constructUnsafe("org.apache.commons.collections.functors.InvokerTransformer");
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, illegalType));
        assertTrue(exception.getMessage().contains("Illegal type"));
    }

}