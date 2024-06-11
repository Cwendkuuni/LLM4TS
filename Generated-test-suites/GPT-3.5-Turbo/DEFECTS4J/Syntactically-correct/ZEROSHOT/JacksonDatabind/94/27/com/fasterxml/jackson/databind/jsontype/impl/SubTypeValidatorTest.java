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
    public void testValidateSubType_validType() {
        try {
            subTypeValidator.validateSubType(null, JavaType.class);
        } catch (JsonMappingException e){
            fail("Should not throw exception for valid type");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalType() throws JsonMappingException {
        subTypeValidator.validateSubType(null, SubTypeValidatorTest.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalSpringType() throws JsonMappingException {
        subTypeValidator.validateSubType(null, FakeSpringClass.class);
    }
}

class FakeSpringClass {}