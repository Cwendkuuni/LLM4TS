package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        DeserializationContext context = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testDeserializeOneElementArray() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{"test"}, result);
        verify(context).returnObjectBuffer(objectBuffer);
    }

    @Test
    public void testDeserializeMultipleElementsArray() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test1");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test2");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test3");
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{"test1", "test2", null, "test3"}, result);
        verify(context).returnObjectBuffer(objectBuffer);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        DeserializationContext context = mock(DeserializationContext.class);
        StringDeserializer mockStringDeserializer = mock(StringDeserializer.class);
        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mockStringDeserializer);
        when(mockStringDeserializer.deserialize(any(), any())).thenAnswer(i -> "customTest");

        StringArrayDeserializer deserializer = new StringArrayDeserializer(mockStringDeserializer);

        JsonToken jsonToken = JsonToken.VALUE_NULL;
        String[] result = new StringArrayDeserializer(mockStringDeserializer).deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{}, result);

        jsonToken = JsonToken.VALUE_STRING;
        result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{"customTest"}, result);
    
        jsonToken = JsonToken.VALUE_NULL;
        result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{"customTest"}, result);

        jsonToken = JsonToken.VALUE_STRING;
        result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[]{"customTest", "customTest"}, result);

        jsonToken = JsonToken.END_ARRAY;
        result = deserializer.deserialize(mockJsonParser, context);
        assertArrayEquals(new String[0], result);

        verify(context).returnObjectBuffer(objectBuffer);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");

        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.handleNonArray(mockJsonParser, context);
        assertArrayEquals(new String[]{"test"}, result);
        verify(context).returnObjectBuffer(objectBuffer);
    }

    @Test
    public void testHandleNonArray_DisableAcceptSingleValueAsArray() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.isEnabled(eq(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY))).thenReturn(false);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.handleNonArray(mockJsonParser, context);
        assertNull(result);
        verify(context, never()).returnObjectBuffer(any());
    }

    @Test
    public void testCreateContextualDeserializer() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer objectBuffer = mock(ObjectBuffer.class);
        StringDeserializer mockStringDeserializer = mock(StringDeserializer.class);

        when(context.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mockStringDeserializer);
        when(mockStringDeserializer.getClass()).thenReturn(StringDeserializer.class);

        when(context.handleSecondaryContextualization(any(), any())).thenAnswer(i -> mockStringDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(context, null);
        assertNotNull(result);
        assertEquals(StringArrayDeserializer.class, result.getClass());

        result = deserializer.createContextual(context, null);
        assertSame(deserializer, result);

        result = deserializer.createContextual(context, null);
        assertSame(deserializer, result);

        StringDeserializer deserializerWithStringDeserializer = new StringArrayDeserializer(mockStringDeserializer);
        JsonDeserializer<?> resultWithStringDeserializer = deserializerWithStringDeserializer.createContextual(context, null);
        assertNotNull(resultWithStringDeserializer);
        assertEquals(StringArrayDeserializer.class, resultWithStringDeserializer.getClass());
        assertSame(mockStringDeserializer, ((StringArrayDeserializer) resultWithStringDeserializer)._elementDeserializer);

        result = deserializerWithStringDeserializer.createContextual(context, null);
        assertSame(deserializerWithStringDeserializer, result);
    }

    @Test
    public void testCreateContextualDeserializerWithNullElementDeserializer() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        JsonDeserializer<?> result = deserializer.createContextual(context, null);
        assertNotNull(result);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertNull(result._elementDeserializer);
    }
}