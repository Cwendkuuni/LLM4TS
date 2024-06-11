package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.core.base.ClassId;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubTypeWithAllowedClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = JavaType.fromClass(String.class);

        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubTypeWithDisallowedClassFromSet() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;

        // Test with a class name that is in the DEFAULT_NO_DESER_CLASS_NAMES set
        JavaType type = JavaType.fromClass(org.apache.commons.collections4.functors.InstantiateTransformer.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeWithSpringClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;

        // Test with a class name that starts with the PREFIX_SPRING string
        JavaType type = JavaType.fromClass(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeWithSubClassOfDisallowedClass() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;

        // Test with a class that is a subclass of a class in the DEFAULT_NO_DESER_CLASS_NAMES set
        JavaType superclass = JavaType.fromClass(com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.class);
        JavaType subclass = JavaType.fromClass(com.sun.org.apache.xalan.xsltc.trax.TemplatesImpl.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, subclass));
    }

    @Test
    public void testValidateSubTypeWithNull() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = null;
        JavaType type = null;

        validator.validateSubType(ctxt, type);
    }
}