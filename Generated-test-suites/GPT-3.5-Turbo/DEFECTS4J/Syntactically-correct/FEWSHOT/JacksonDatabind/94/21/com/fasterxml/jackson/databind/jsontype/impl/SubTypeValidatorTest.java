package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
        deserializationContext = new DeserializationContext(null, null);
        javaType = null;
    }

    @Test
    public void testValidateSubType_SafeType() {
        try {
            validator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("Should not throw an exception");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        javaType = JavaType.construct(SubTypeValidator.class);
        validator.validateSubType(deserializationContext, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalSpringType() throws JsonMappingException {
        javaType = JavaType.construct(AbstractPointcutAdvisor.class);
        validator.validateSubType(deserializationContext, javaType);
    }
}