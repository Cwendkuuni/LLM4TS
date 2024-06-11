
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = mock(DeserializationContext.class);
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        // Valid type
        JavaType validType = mock(JavaType.class);
        Class<?> validRawClass = Object.class;
        when(validType.getRawClass()).thenReturn(validRawClass);
        subTypeValidator.validateSubType(deserializationContext, validType);

        // Invalid type
        JavaType invalidType = mock(JavaType.class);
        Class<?> invalidRawClass = String.class;
        when(invalidType.getRawClass()).thenReturn(invalidRawClass);
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            subTypeValidator.validateSubType(deserializationContext, invalidType);
        });
        assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons", exception.getMessage());
    }
}
