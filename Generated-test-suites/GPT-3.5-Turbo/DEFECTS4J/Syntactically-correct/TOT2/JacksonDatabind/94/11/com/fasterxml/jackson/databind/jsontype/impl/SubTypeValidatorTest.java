package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {
  
    @Test
    public void testValidateSubTypeNormalCase() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.construct(Void.class);

        assertDoesNotThrow(() -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeIllegalCase() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.construct(Object.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
    @Test
    public void testValidateSubTypeEdgeCase() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = JavaType.construct(AbstractApplicationContext.class);

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
  
    @Test
    public void testValidateSubTypeEmptyType() {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JavaType type = null;

        assertThrows(NullPointerException.class, () -> validator.validateSubType(ctxt, type));
    }
}