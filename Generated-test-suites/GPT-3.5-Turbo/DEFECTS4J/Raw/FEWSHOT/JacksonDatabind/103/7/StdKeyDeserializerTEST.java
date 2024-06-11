package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeyDeserializerTest {
    
    @Test
    public void testDeserializeKey_WhenKeyIsNull_ShouldReturnNull() throws Exception {
        // Arrange
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        
        // Act
        Object result = keyDeserializer.deserializeKey(null, deserializationContext);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testDeserializeKey_WhenKeyTypeIsString_ShouldReturnKey() throws Exception {
        // Arrange
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(String.class);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        
        // Act
        Object result = keyDeserializer.deserializeKey("test", deserializationContext);
        
        // Assert
        assertEquals("test", result);
    }
    
    @Test
    public void testDeserializeKey_WhenKeyTypeIsObject_ShouldReturnKey() throws Exception {
        // Arrange
        StdKeyDeserializer keyDeserializer = new StdKeyDeserializer.StringKD(Object.class);
        DeserializationContext deserializationContext = new DeserializationContext(null, null, null);
        
        // Act
        Object result = keyDeserializer.deserializeKey("test", deserializationContext);
        
        // Assert
        assertEquals("test", result);
    }
}