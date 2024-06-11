package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType() {
        try {
            subTypeValidator.validateSubType(null, JavaType.factory().constructType(String.class));
        } catch (Exception e) {
            fail("Should not throw exception for valid type");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws JsonMappingException {
        subTypeValidator.validateSubType(null, JavaType.factory().constructType(AbstractPointcutAdvisor.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_InterfaceType() throws JsonMappingException {
        subTypeValidator.validateSubType(null, JavaType.factory().constructType(DummyInterface.class));
    }

}