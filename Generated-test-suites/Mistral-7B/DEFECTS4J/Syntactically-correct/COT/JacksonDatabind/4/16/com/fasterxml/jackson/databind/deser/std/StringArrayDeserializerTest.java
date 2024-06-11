package com.yourpackage.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringArrayDeserializerTest {

    @Test
    public void testDefaultDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.getValueAsString()).thenReturn("testValue");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, context);

        Assert.assertArrayEquals(new String[]{"test", "testValue"}, result);
    }

    @Test(expected = IOException.class)
    public void testNonArrayToken() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] result = deserializer.deserialize(jsonParser, context);

        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testElementDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> elementDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(context.findContextualValueDeserializer(String[].class, null)).thenReturn(elementDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        StringArrayDeserializer deserializerWithElementDeserializer = new StringArrayDeserializer(elementDeserializer);
        String[] result = deserializerWithElementDeserializer.deserialize(jsonParser, context);

        String[] expectedResult = new String[]{"test", null};
        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testEmptyArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer.deserialize(jsonParser, context);
    }

    @Test(expected = IOException.class)
    public void testCustomDeserializerNullInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        deserializer.deserialize(jsonParser, context);
    }

    @Test
    public void testCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        JsonDeserializer<String> customDeserializer = mock(JsonDeserializer.class);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("test2");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        when(customDeserializer.deserialize(jsonParser, context)).thenReturn("testResult");

        when(context.findContextualValueDeserializer(String[].class, null)).thenReturn(customDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = deserializer.deserialize(jsonParser, context);

        String[] expectedResult = new String[]{"testResult", "testResult"};
        Assert.assertArrayEquals(expectedResult, result);
    }
}