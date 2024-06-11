package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.JsonToken;

import java.io.IOException;

@SuppressWarnings("unchecked")
public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    @org.junit.Test
    public void testDeserialize() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.getText()).thenReturn("hello");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(new String[]{"hello"}, result);
    }

    @org.junit.Test
    public void testDeserialize_nullValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, context);
        assertNull(result);
    }

    @org.junit.Test
    public void testDeserialize_customElementDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.getText()).thenReturn("foo");
        when(context.leaseObjectBuffer()).thenReturn(buffer);

        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(new String[]{"foo"}, result);
    }

    @org.junit.Test
    public void testDeserialize_emptyArray() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(parser, context);
        assertArrayEquals(new String[0], result);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.getText()).thenReturn("foo");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.handleNonArray(parser, context);
    }

    @org.junit.Test
    public void testCreateContextual() {
        // Test cases for createContextual method are complex and require additional setup.
        // They can be implemented using Mockito or similar testing frameworks.
    }
}