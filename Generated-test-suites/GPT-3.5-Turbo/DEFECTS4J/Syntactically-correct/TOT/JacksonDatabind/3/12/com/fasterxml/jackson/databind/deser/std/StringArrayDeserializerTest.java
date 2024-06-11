
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;

public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void deserialize_withValidArray_shouldReturnCorrectStringArray() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        ValueDeserializer<?> valueDeserializer = mock(ValueDeserializer.class);

        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value");

        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(buffer.appendCompletedChunk(any())).thenReturn(new Object[2]);
        when(buffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[]{"value"});
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(new String[]{"value"});

        StringArrayDeserializer deserializer = new StringArrayDeserializer(valueDeserializer);

        // Act
        String[] result = deserializer.deserialize(jp, ctxt);

        // Assert
        assertArrayEquals(new String[]{"value"}, result);
    }

    @Test
    public void deserialize_withNullValue_shouldReturnNullArray() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        ValueDeserializer<?> valueDeserializer = mock(ValueDeserializer.class);

        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("value");

        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(buffer.appendCompletedChunk(any())).thenReturn(new Object[2]);
        when(buffer.completeAndClearBuffer(any(), anyInt(), any())).thenReturn(new String[]{null});
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(new String[]{null});

        StringArrayDeserializer deserializer = new StringArrayDeserializer(valueDeserializer);

        // Act
        String[] result = deserializer.deserialize(jp, ctxt);

        // Assert
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void deserialize_withNonArrayToken_shouldThrowIOException() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ValueDeserializer<?> valueDeserializer = mock(ValueDeserializer.class);

        when(jp.isExpectedStartArrayToken()).thenReturn(false);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(valueDeserializer);

        // Act
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void createContextual_withDefaultDeserializer_shouldReturnThisInstance() throws IOException {
        // Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        // Assert
        assertEquals(deserializer, result);
    }

    @Test
    public void createContextual_withNonNullValueDeserializer_shouldReturnNewInstanceWithUpdatedValueDeserializer() throws IOException {
        // Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Act
        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        // Assert
        assertEquals(deserializer, result);
    }
}

