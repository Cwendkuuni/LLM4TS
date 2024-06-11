package com.example.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import java.io.IOException;
import java.util.Arrays;

/**
 * JUnit 4 test file for StringArrayDeserializer class.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private static final StringArrayDeserializer DESIALIZER = StringArrayDeserializer.instance;

    @org.junit.Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"hello\",\"world\"]");
        DESIALIZER._elementDeserializer = mock(JsonDeserializer.class);
        when(DESIALIZER._elementDeserializer.deserialize(parser, mock(DeserializationContext.class))).thenReturn("test");
    }

    @org.junit.Test
    public void testDeserializeEmptyArray() throws Exception {
        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        assertArrayEquals(new String[0], result);
    }

    @org.junit.Test
    public void testDeserializeSingleElementArray() throws Exception {
        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        when(DESIALIZER.isDefaultDeserializer(_elementDeserializer)).thenReturn(false);
        when(DESIALIZER.handleNonArray(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new String[]{"test"});

        result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        assertArrayEquals(new String[]{"test"}, result);
    }

    @org.junit.Test
    public void testDeserializeMultipleElementsArray() throws Exception {
        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        when(DESIALIZER.isDefaultDeserializer(_elementDeserializer)).thenReturn(false);

        JsonParser parser = mock(JsonParser.class);
        when(DESIALIZER._elementDeserializer.deserialize(parser, mock(DeserializationContext.class))).thenReturn("test1");
        when(DESIALIZER._elementDeserializer.deserialize(parser, mock(DeserializationContext.class))).thenReturn("test2");
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.VALUE_STRING).thenReturn(JsonToken.END_ARRAY);

        result = DESIALIZER.deserialize(parser, mock(DeserializationContext.class));
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @org.junit.Test
    public void testDeserializeCustomElementDeserializer() throws Exception {
        String[] result = DESIALIZER._deserializeCustom(null, mock(DeserializationContext.class));
        when(DESIALIZER._elementDeserializer.getNullValue()).thenReturn("nullValue");
        when(DESIALIZER._elementDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("test1").thenReturn("test2");

        result = DESIALIZER._deserializeCustom(null, mock(DeserializationContext.class));
        assertArrayEquals(new String[]{"test1", "test2"}, result);
    }

    @org.junit.Test
    public void testDeserializeWithType() throws Exception {
        TypeReference<String[]> typeReference = new TypeReference<String[]>() {};
        String[] result = DESIALIZER.deserializeWithType(null, mock(JsonParser.class), mock(DeserializationContext.class), mock(TypeDeserializer.class));
        assertArrayEquals(new String[0], result);
    }

    @org.junit.Test
    public void testHandleNonArrayNullValue() throws Exception {
        when(DESIALIZER._elementDeserializer).thenReturn(mock(JsonDeserializer.class));
        when(DESIALIZER._elementDeserializer.getNullValue()).thenReturn(null);
        when(DESIALIZER.handleNonArray(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new String[]{null});

        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        assertArrayEquals(new String[]{null}, result);
    }

    @org.junit.Test
    public void testHandleNonArrayValueString() throws Exception {
        when(DESIALIZER._elementDeserializer).thenReturn(mock(JsonDeserializer.class));
        when(DESIALIZER._elementDeserializer.getNullValue()).thenReturn(null);
        when(DESIALIZER.handleNonArray(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(new String[]{"test"});

        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        assertArrayEquals(new String[]{"test"}, result);
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeWithUnexpectedToken() throws Exception {
        String[] result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
        when(DESIALIZER.isExpectedStartArrayToken(any(JsonParser.class))).thenReturn(false);

        result = DESIALIZER.deserialize(null, mock(DeserializationContext.class));
    }

    @org.junit.Test
    public void testCreateContextualNullProperty() throws Exception {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer result = DESIALIZER.createContextual(null, null);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreateContextualWithNullDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = null;
        StringArrayDeserializer result = DESIALIZER.createContextual(mock(DeserializationContext.class), null);
        assertNull(result);
    }

    @org.junit.Test
    public void testCreateContextualWithDefaultDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        when(DESIALIZER.isDefaultDeserializer(deserializer)).thenReturn(true);
        StringArrayDeserializer result = DESIALIZER.createContextual(mock(DeserializationContext.class), null);
        assertSame(DESIALIZER, result);
    }

    @org.junit.Test
    public void testCreateContextualWithCustomDeserializer() throws Exception {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        StringArrayDeserializer result = DESIALIZER.createContextual(mock(DeserializationContext.class), null);
        assertSame(DESIALIZER, result);
        when(DESIALIZER.isDefaultDeserializer(deserializer)).thenReturn(false);
        StringArrayDeserializer deserializerWrapper = new StringArrayDeserializer(deserializer);
        assertSame(deserializerWrapper, result);
    }
}