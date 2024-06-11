
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

    @Before
    public void setUp() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_validClass() {
        try {
            validator.validateSubType(null, JavaType.class);
        } catch (JsonMappingException e) {
            fail("Validation should pass for a valid class");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalClass() throws JsonMappingException {
        validator.validateSubType(null, Abatable.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalSpringClass() throws JsonMappingException {
        validator.validateSubType(null, AbstractPointcutAdvisor.class);
    }
}
