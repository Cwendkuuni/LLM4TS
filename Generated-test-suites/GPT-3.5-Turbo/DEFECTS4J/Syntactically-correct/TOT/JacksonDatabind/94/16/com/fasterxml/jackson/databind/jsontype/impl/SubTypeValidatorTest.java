package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_typicalUseCase() throws Exception {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType javaType = new JavaType(String.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(ctxt, javaType);
        // Assert that no exception is thrown
    }

    @Test
    public void testValidateSubType_edgeCase() throws Exception {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType javaType = new JavaType(Class.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(ctxt, javaType);
        // Assert that no exception is thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_errorScenario() throws Exception {
        DeserializationContext ctxt = new DeserializationContext();
        JavaType javaType = new JavaType(Object.class);
        SubTypeValidator validator = SubTypeValidator.instance();

        validator.validateSubType(ctxt, javaType);
    }

}