package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

/**
 * Test class for StringArrayDeserializer
 */
@JacksonStdImpl
public final class StringArrayDeserializerTest {

    private static final StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

    @org.junit.Test
    public void testDeserialize() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        String value1 = "value1";
        when(mockParser.getText()).thenReturn(value1);

        String[] result = stringArrayDeserializer.deserialize(mockParser, mockContext);
        assertEquals(new String[] { value1 }, result);

        reset(mockParser);
        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        result = stringArrayDeserializer.deserialize(mockParser, mockContext);
        assertNull(result);

        reset(mockParser);
        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_NULL);
        String value1_1 = "value1.1";
        String value2 = null;
        String[] result2 = stringArrayDeserializer.deserialize(mockParser, mockContext);
        assertEquals(new String[] { value1_1, value2 }, result2);
    }

    @org.junit.Test
    public void testDeserializeCustom() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        StringDeserializer mockElementDeserializer = mock(StringDeserializer.class);
        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(mockElementDeserializer);

        ObjectBuffer mockBuffer = mock(ObjectBuffer.class);
        when(mockContext.leaseObjectBuffer()).thenReturn(mockBuffer);

        Object[] chunk = mockBuffer.resetAndStart();
        String[] result = stringArrayDeserializer.deserializeCustom(mockParser, mockContext);

        verify(mockParser, times(5)).nextToken();
        verifyNoMoreInteractions(mockParser);
        assertEquals(result, new String[] { /* ... */ }); // Replace with expected values
        verify(mockBuffer).completeAndClearBuffer(chunk, (int) result.length, eq(String.class));
        verify(mockContext).returnObjectBuffer(mockBuffer);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeWithInvalidInput() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.isExpectedStartArrayToken()).thenReturn(false);
        stringArrayDeserializer.deserialize(mockParser, mockContext);
    }

    @org.junit.Test
    public void testDeserializeWithType() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);

        when(mockParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockTypeDeserializer.deserializeTypedFromArray(mockParser, mockContext)).thenReturn(new String[0]);

        Object result = stringArrayDeserializer.deserializeWithType(mockParser, mockContext, mockTypeDeserializer);
        assertEquals(new String[0], result);
    }

    @org.junit.Test
    public void testHandleNonArray() throws IOException {
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);

        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String value = "value";

        String[] result = stringArrayDeserializer.handleNonArray(mockParser, mockContext);
        assertEquals(new String[] { value }, result);

        reset(mockParser);
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        result = stringArrayDeserializer.handleNonArray(mockParser, mockContext);
        assertNull(result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreateContextualDeserializerWithNullValueDeserializer() throws IOException {
        DeserializationContext mockContext = mock(DeserializationContext.class);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(null);
        stringArrayDeserializer.createContextual(mockContext, null);
    }
}