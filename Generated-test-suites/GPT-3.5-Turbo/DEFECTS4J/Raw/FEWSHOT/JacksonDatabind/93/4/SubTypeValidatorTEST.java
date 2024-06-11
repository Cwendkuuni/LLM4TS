
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = new SubTypeValidator();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = String.class;
        String full = raw.getName();
        
        // Act
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            // Assert
            assertEquals("Illegal type (" + full + ") to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_withIllegalClassName() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = new SubTypeValidator();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        
        // Set illegal class names
        Set<String> illegalClassNames = new HashSet<String>();
        illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        illegalClassNames.add("org.apache.commons.collections.functors.InstantiateTransformer");
        validator._cfgIllegalClassNames = illegalClassNames;
        
        // Act
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            // Assert
            assertEquals("Illegal type (" + "org.apache.commons.collections.functors.InvokerTransformer" + ") to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
