package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

public class SubTypeValidatorTest {

    private static final String PREFIX_SPRING = "org.springframework.";

    @Test
    public void testValidateSubType_ValidType_NoException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;

        // Valid type to be deserialized
        type = JavaType.construct(Integer.class);
        validator.validateSubType(context, type);

        // Valid type to be deserialized
        type = JavaType.construct(String.class);
        validator.validateSubType(context, type);

        // Valid type to be deserialized
        type = JavaType.construct(Object.class);
        validator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidType_ThrowsException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;

        // Invalid type to be deserialized
        type = JavaType.construct(ClassLoader.class);
        validator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidSpringType_ThrowsException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;

        // Invalid Spring type to be deserialized (AbstractPointcutAdvisor)
        type = JavaType.construct(AbstractPointcutAdvisor.class);
        validator.validateSubType(context, type);

        // Invalid Spring type to be deserialized (AbstractApplicationContext)
        type = JavaType.construct(AbstractApplicationContext.class);
        validator.validateSubType(context, type);
    }

    @Test
    public void testValidateSubType_ValidSpringType_NoException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;

        // Valid Spring type to be deserialized
        type = JavaType.construct(SpringBean.class);
        validator.validateSubType(context, type);

        // Valid Spring type to be deserialized
        type = JavaType.construct(SpringContext.class);
        validator.validateSubType(context, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InvalidNastyClass_ThrowsException() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = null;
        JavaType type = null;

        // Invalid nasty class to be deserialized (InvokerTransformer)
        type = JavaType.construct(org.apache.commons.collections.functors.InvokerTransformer.class);
        validator.validateSubType(context, type);

        // Invalid nasty class to be deserialized (InstantiateTransformer)
        type = JavaType.construct(org.apache.commons.collections.functors.InstantiateTransformer.class);
        validator.validateSubType(context, type);
    }

}