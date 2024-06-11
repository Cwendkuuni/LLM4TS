package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;

    @Before
    public void setup() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext(null, null);
        javaType = JavaType.newBuilder().addClass(String.class).build();
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }

    @Test
    public void testValidateSubType_IllegalClass() {
        JavaType illegalType = JavaType.newBuilder().addClass(Object.class).build();
        assertThrows(JsonMappingException.class, () -> {
            subTypeValidator.validateSubType(deserializationContext, illegalType);
        });
    }

    @Test
    public void testValidateSubType_NullContext() {
        assertThrows(NullPointerException.class, () -> {
            subTypeValidator.validateSubType(null, javaType);
        });
    }

    @Test
    public void testInstance_SingletonPattern() {
        SubTypeValidator validator1 = SubTypeValidator.instance();
        SubTypeValidator validator2 = SubTypeValidator.instance();
        assertEquals(validator1, validator2);
    }
}