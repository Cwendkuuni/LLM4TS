package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private final SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubTypeWithValidType() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(Class.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithIllegalClass() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(org.apache.commons.collections.functors.InvokerTransformer.class);
        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithSpringAbstractPointcutAdvisor() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor.class);
        validator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubTypeWithSpringAbstractApplicationContext() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(org.springframework.context.support.AbstractApplicationContext.class);
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testCustomIllegalClassNames() throws JsonMappingException {
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("com.fasterxml.jackson.core.JsonToken");
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.setIllegalClassNames(illegalClassNames);
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(com.fasterxml.jackson.core.JsonToken.class);
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertNotNull(e);
            assertEquals("Illegal type (com.fasterxml.jackson.core.JsonToken) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testCustomIllegalClassNamesWithNullType() throws JsonMappingException {
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("com.fasterxml.jackson.core.JsonToken");
        SubTypeValidator validator = SubTypeValidator.instance();
        validator.setIllegalClassNames(illegalClassNames);
        DeserializationContext ctxt = null;
        JavaType type = null;
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertNotNull(e);
            assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}