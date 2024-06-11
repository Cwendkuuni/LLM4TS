package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_withJsonProperty() {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        
        // Act
        Object result = deserializer.deserialize(parser, context);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType_withStartArray() {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        
        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_ARRAY);
        when(typeDeserializer.deserializeTypedFromAny(parser, context)).thenReturn("result");
        
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        // Assert
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeWithType_withStartObject() {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_START_OBJECT);
        when(typeDeserializer.deserializeTypedFromAny(parser, context)).thenReturn("result");
        
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        // Assert
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeWithType_withFieldName() {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_FIELD_NAME);
        when(typeDeserializer.deserializeTypedFromAny(parser, context)).thenReturn("result");
        
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        // Assert
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeWithType_withOtherToken() {
        // Arrange
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(parser.getCurrentTokenId()).thenReturn(JsonTokenId.ID_NOT_AVAILABLE);
        
        // Act
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        
        // Assert
        assertNull(result);
    }
}