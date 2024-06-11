package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithDefaultNoDeserClassNames() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = null;

        // Test with types in DEFAULT_NO_DESER_CLASS_NAMES
        try {
            type = new JavaType(null, "org.apache.commons.collections.functors.InvokerTransformer", null, null, null, false);
            validator.validateSubType(ctxt, type);
            assertEquals(false, true);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        try {
            type = new JavaType(null, "org.apache.commons.collections.functors.InstantiateTransformer", null, null, null, false);
            validator.validateSubType(ctxt, type);
            assertEquals(false, true);
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InstantiateTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }

        // Continue for other types in DEFAULT_NO_DESER_CLASS_NAMES

        // Test with types not in DEFAULT_NO_DESER_CLASS_NAMES
        try {
            type = new JavaType(null, "org.springframework.beans.factory.BeanFactory", null, null, null, false);
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals(false, true);
        }

        try {
            type = new JavaType(null, "java.lang.String", null, null, null, false);
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertEquals(false, true);
        }
    }
}