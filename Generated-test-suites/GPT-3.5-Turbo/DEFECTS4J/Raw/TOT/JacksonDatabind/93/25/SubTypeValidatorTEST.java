package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private JavaType javaType;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_Success() throws JsonMappingException {
        // Typical use case
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_Failure() throws JsonMappingException {
        // Error scenario
        subTypeValidator.validateSubType(deserializationContext, null);
    }

    @Test
    public void testIllegalClassNames() {
        Set<String> illegalClassNames = subTypeValidator._cfgIllegalClassNames;
        assertNotNull(illegalClassNames);
        assertFalse(illegalClassNames.isEmpty());
        // Edge case
        assertTrue(illegalClassNames.contains("java.rmi.server.UnicastRemoteObject"));
        assertTrue(illegalClassNames.contains("org.springframework.beans.factory.ObjectFactory"));
    }

}