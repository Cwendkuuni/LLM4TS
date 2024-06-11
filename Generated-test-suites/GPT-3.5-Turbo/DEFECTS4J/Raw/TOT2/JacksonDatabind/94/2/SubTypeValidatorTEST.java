package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = JavaType.construct(String.class);
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = JavaType.construct(Class.forName("org.apache.commons.collections.functors.InvokerTransformer"));
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_springFrameworkType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = JavaType.construct(Class.forName("org.springframework.beans.factory.ObjectFactory"));
        validator.validateSubType(ctxt, type);
        // No exception should be thrown
    }

}