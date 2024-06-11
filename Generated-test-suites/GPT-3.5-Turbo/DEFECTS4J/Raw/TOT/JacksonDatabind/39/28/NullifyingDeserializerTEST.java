package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserialize_SkipChildren() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new MockJsonParser();
        DeserializationContext context = new MockDeserializationContext();
        
        try {
            // Act
            Object result = deserializer.deserialize(parser, context);
            
            // Assert
            assertNull(result);
            assertEquals(parser.nextToken(), null);  // Checking if parsing has moved to the next token
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    @Test
    public void testDeserializeWithType_DeserializeTypedFromAny() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new MockJsonParser();
        DeserializationContext context = new MockDeserializationContext();
        TypeDeserializer typeDeserializer = new MockTypeDeserializer();
        
        try {
            // Act
            Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
            
            // Assert
            assertNull(result);
            assertEquals(parser.nextToken(), null);  // Checking if parsing has moved to the next token
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    // Edge case - Empty JSON object
    @Test
    public void testDeserialize_SkipChildren_EmptyJSONObject() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new MockJsonParser("{}");
        DeserializationContext context = new MockDeserializationContext();
        
        try {
            // Act
            Object result = deserializer.deserialize(parser, context);
            
            // Assert
            assertNull(result);
            assertEquals(parser.nextToken(), null);  // Checking if parsing has moved to the next token
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    // Edge case - Empty JSON array
    @Test
    public void testDeserialize_SkipChildren_EmptyJSONArray() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new MockJsonParser("[]");
        DeserializationContext context = new MockDeserializationContext();
        
        try {
            // Act
            Object result = deserializer.deserialize(parser, context);
            
            // Assert
            assertNull(result);
            assertEquals(parser.nextToken(), null);  // Checking if parsing has moved to the next token
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
    
    // Error scenario - Invalid JSON input
    @Test
    public void testDeserialize_SkipChildren_InvalidJSON() {
        // Arrange
        NullifyingDeserializer deserializer = new NullifyingDeserializer();
        JsonParser parser = new MockJsonParser("invalid JSON");
        DeserializationContext context = new MockDeserializationContext();
        
        try {
            // Act
            Object result = deserializer.deserialize(parser, context);
            
            // Assert
            assertNull(result);
            // Parsing should not move forward due to invalid JSON, so the current token should still be the same
            assertEquals(parser.getCurrentToken(), JsonToken.NOT_AVAILABLE);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}