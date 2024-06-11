package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import org.mockito.Mockito;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator = SubTypeValidator.instance();
    private DeserializationContext deserializationContext = mock(DeserializationContext.class);

    @Test
    public void testValidateSubType_InvalidSubType() throws JsonMappingException {
        Mockito.when(deserializationContext.getRawResolvableType()).thenReturn(JavaType.fromClass(InvalidSubType.class));

        try {
            subTypeValidator.validateSubType(deserializationContext, deserializationContext.getTypeFactory().constructType(InvalidSubType.class));
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest$InvalidSubType) to deserialize: prevented for security reasons", e.getMessage());
            return;
        }
        assertFalse("JsonMappingException not thrown for invalid subtype", true);
    }

    @Test
    public void testValidateSubType_ValidSpringFrameworkSubType() throws JsonMappingException {
        Mockito.when(deserializationContext.getRawResolvableType()).thenReturn(JavaType.fromClass(ValidSpringFrameworkSubType.class));

        subTypeValidator.validateSubType(deserializationContext, deserializationContext.getTypeFactory().constructType(ValidSpringFrameworkSubType.class));
    }

    @Test
    public void testValidateSubType_ValidSubType() throws JsonMappingException {
        Mockito.when(deserializationContext.getRawResolvableType()).thenReturn(JavaType.fromClass(ValidSubType.class));

        subTypeValidator.validateSubType(deserializationContext, deserializationContext.getTypeFactory().constructType(ValidSubType.class));
    }

    public static class InvalidSubType {
        // Invalid SubType
    }

    public static class ValidSubType {
        // Valid SubType
    }

    public static class ValidSpringFrameworkSubType {
        // Valid Spring Framework SubType
    }
}