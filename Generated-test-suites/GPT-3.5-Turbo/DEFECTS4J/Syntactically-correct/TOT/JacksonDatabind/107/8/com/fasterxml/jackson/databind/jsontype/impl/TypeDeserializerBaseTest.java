
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializer;
    
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        typeDeserializer = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }
    
    @Test
    public void baseTypeName_ReturnsBaseTypeName(){
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializer.baseTypeName());
    }

    @Test
    public void getPropertyName_ReturnsTypePropertyName() {
        assertNull(typeDeserializer.getPropertyName());
    }

    @Mock
    private DeserializationContext deserializationContext;
    
    @Test
    public void _findDefaultImplDeserializer_WhenDefaultImplementationIsNull_ShouldReturnNullifyingDeserializer() throws IOException {
        // Arrange
        typeDeserializer._defaultImpl = null;
        
        // Act
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(deserializationContext);
        
        // Assert
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    @Test
    public void _findDefaultImplDeserializer_WhenDefaultImplementationIsVoid_ShouldReturnNullifyingDeserializer() throws IOException {
        // Arrange
        typeDeserializer._defaultImpl = JavaType.Void;
        
        // Act
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(deserializationContext);
        
        // Assert
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    @Test
    public void _findDefaultImplDeserializer_WhenDefaultImplementationIsNotBogusClass_ShouldReturnContextualValueDeserializer() throws IOException {
        // Arrange
        typeDeserializer._defaultImpl = JavaType.construct(Object.class);
        
        // Act
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(deserializationContext);
        
        // Assert
        assertNotNull(deserializer);
    }

    @Test
    public void _handleMissingTypeId_ShouldReturnHandleMissingTypeIdFromContext() throws IOException {
        // Arrange
        String extraDesc = "Extra description";
        
        // Act
        JavaType result = typeDeserializer._handleMissingTypeId(deserializationContext, extraDesc);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void _handleUnknownTypeId_ShouldReturnUnknownTypeIdFromContext() throws IOException {
        // Arrange
        String typeId = "Unknown";
        
        // Act
        JavaType result = typeDeserializer._handleUnknownTypeId(deserializationContext, typeId);
        
        // Assert
        assertNotNull(result);
    }
    
}
