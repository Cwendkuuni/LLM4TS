package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.fromClazz(Object.class);
        // Verify that the validateSubType method does not throw an exception for a valid type
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("An unexpected JsonMappingException occurred");
        }
    }

    @Test
    public void testValidateSubType_IllegalType() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.fromClazz(org.apache.commons.collections.functors.InvokerTransformer.class);
        // Verify that the validateSubType method throws the expected exception for an illegal type
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException is expected but not thrown");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }

    @Test
    public void testValidateSubType_InterfaceType() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.fromClazz(java.util.Set.class);
        // Verify that the validateSubType method does not throw an exception for an interface type
        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("An unexpected JsonMappingException occurred");
        }
    }

    @Test
    public void testValidateSubType_SpringClass() {
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.fromClazz(org.springframework.beans.factory.support.AbstractBeanFactoryPointcutAdvisor.class);
        // Verify that the validateSubType method throws the expected exception for a Spring class
        try {
            subTypeValidator.validateSubType(ctxt, type);
            fail("JsonMappingException is expected but not thrown");
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type"));
        }
    }
}