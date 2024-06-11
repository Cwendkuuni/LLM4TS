
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType_NoException() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();

        // Act
        validator.validateSubType(ctxt, type);

        // Assert: No exception thrown
    }
}
