
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubTypeValidatorTest {
    private SubTypeValidator validator;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JavaType type;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidClass() throws JsonMappingException {
        validator.validateSubType(ctxt, type);
        // Assert that no exception is thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalClass() throws JsonMappingException {
        validator.validateSubType(ctxt, type);
    }
}
