package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext ctxt;
    @Mock
    private JavaType type;

    private SubTypeValidator subTypeValidator;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType() {
        subTypeValidator.validateSubType(ctxt, type);
        verify(ctxt, times(1)).getRawClass();
        verify(type, times(1)).getRawClass();
        verifyNoMoreInteractions(ctxt, type);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() {
        when(type.getRawClass()).thenReturn(SecurityBreachType.class);
        subTypeValidator.validateSubType(ctxt, type);
    }

    private static class SecurityBreachType{

    }
}