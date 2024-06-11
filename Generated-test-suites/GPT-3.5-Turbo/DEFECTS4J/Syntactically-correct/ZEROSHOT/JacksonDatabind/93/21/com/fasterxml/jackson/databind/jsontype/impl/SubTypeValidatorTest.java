
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JavaType type;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateSubType_validType_doesNotThrowException() throws JsonMappingException {
        subTypeValidator.validateSubType(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void validateSubType_invalidType_throwsException() throws JsonMappingException {
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("com.example.InvalidType");
        subTypeValidator._cfgIllegalClassNames = illegalClassNames;

        subTypeValidator.validateSubType(ctxt, type);
    }
}

