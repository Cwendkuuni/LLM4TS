

package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private DeserializationContext ctxt;
    private JavaType type;
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        ctxt = mock(DeserializationContext.class);
        type = mock(JavaType.class);
        subTypeValidator = SubTypeValidator.instance();
        subTypeValidator._cfgIllegalClassNames = new HashSet<>();
    }

    @Test
    public void testValidateSubType_WithValidClass_ShouldNotThrowJsonMappingException() throws JsonMappingException {
        when(type.getRawClass()).thenReturn(TestClass.class);

        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_WithInvalidClass_ShouldThrowJsonMappingException() throws JsonMappingException {
        when(type.getRawClass()).thenReturn(InvalidTestClass.class);

        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_WithSpringAbstractPointcutAdvisor_ShouldThrowJsonMappingException() throws JsonMappingException {
        when(type.getRawClass()).thenReturn(TestClass.class);
        subTypeValidator._cfgIllegalClassNames.add("AbstractPointcutAdvisor");

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type (TestClass) to deserialize: prevented for security reasons"));
        }
    }

    @Test
    public void testValidateSubType_WithSpringAbstractApplicationContext_ShouldThrowJsonMappingException() throws JsonMappingException {
        when(type.getRawClass()).thenReturn(TestClass.class);
        subTypeValidator._cfgIllegalClassNames.add("AbstractApplicationContext");

        try {
            subTypeValidator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            assertTrue(e.getMessage().contains("Illegal type (TestClass) to deserialize: prevented for security reasons"));
        }
    }

    private static class TestClass {}

    private static class InvalidTestClass {}

}

