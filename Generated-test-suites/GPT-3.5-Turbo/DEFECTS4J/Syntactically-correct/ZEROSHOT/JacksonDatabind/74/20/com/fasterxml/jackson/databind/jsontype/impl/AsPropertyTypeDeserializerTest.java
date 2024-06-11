
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AsPropertyTypeDeserializerTest {
  
    @Test
    public void deserializeTypedFromObject_shouldReturnObject() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(null, null);
        
        // Assert
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void deserializeTypedForId_shouldReturnObject() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        
        // Act
        Object result = deserializer.deserializeTypedForId(null, null, null);
        
        // Assert
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void deserializeTypedUsingDefaultImpl_shouldReturnObject() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        
        // Act
        Object result = deserializer.deserializeTypedUsingDefaultImpl(null, null, null);
        
        // Assert
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void deserializeTypedFromAny_shouldReturnObject() throws Exception {
        // Arrange
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer();
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(null, null);
        
        // Assert
        assertTrue(result instanceof Object);
    }
}
