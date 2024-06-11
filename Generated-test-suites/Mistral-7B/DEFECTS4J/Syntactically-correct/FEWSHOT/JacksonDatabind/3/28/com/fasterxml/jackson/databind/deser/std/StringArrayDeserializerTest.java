package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ObjectBuffer;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test cases for {@link StringArrayDeserializer} class
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends BaseTest {

    private static final String[] EMPTY_STRING_ARRAY = {};

    @SuppressWarnings("unchecked")
    private static final JsonDeserializer<String> MOCK_ELEMENT_DESERIALIZER = mock(JsonDeserializer.class);

    @Test
    public void testDeserialize() throws IOException {
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] expected = {"foo", "bar", null};
        String[] result;

        JsonParser jp = createMockParser(JsonToken.START_ARRAY);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);

        jp = createMockParser(JsonToken.VALUE_STRING);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("foo");
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{"foo"}, result);

        jp = createMockParser(JsonToken.VALUE_NULL);
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(new String[]{null}, result);

        jp = createMockParser(JsonToken.END_ARRAY);
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(EMPTY_STRING_ARRAY, result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] expected = {"foo", "bar", null};
        String[] result;

        JsonParser jp = createMockParser(JsonToken.START_ARRAY);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(typeDeserializer.deserializeTypedFromArray(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(expected);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        ObjectBuffer buffer = new ObjectBuffer(16);
        String[] expected = {"foo", "bar", null};
        String[] result;

        JsonParser jp = createMockParser(JsonToken.START_ARRAY);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(ctxt.findContextualValueDeserializer(any(Class.class), any(BeanProperty.class)))
                .thenReturn(MOCK_ELEMENT_DESERIALIZER);

        StringArrayDeserializer deserializer = new StringArrayDeserializer(MOCK_ELEMENT_DESERIALIZER);
        result = deserializer.deserialize(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String[] expected = {"foo"};
        String[] result;

        JsonParser jp = createMockParser(JsonToken.VALUE_STRING);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(any(DeserializationFeature.class))).thenReturn(true);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        result = deserializer.handleNonArray(jp, ctxt);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        StringArrayDeserializer result;

        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);

        when(ctxt.findContextualValueDeserializer(any(Class.class), any(BeanProperty.class)))
                .thenReturn(deser);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);

        result = new StringArrayDeserializer().createContextual(ctxt, property);
        assertNotNull(result);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertEquals(deser, result._elementDeserializer);

        when(ctxt.findContextualValueDeserializer(any(Class.class), any(BeanProperty.class)))
                .thenReturn(null);
        when(ctxt.constructType(String.class)).thenReturn(String[].class);

        result = new StringArrayDeserializer().createContextual(ctxt, property);
        assertNotNull(result);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertNull(result._elementDeserializer);

        when(ctxt.findContextualValueDeserializer(any(Class.class), any(BeanProperty.class)))
                .thenReturn(deser);
        when(deser.isDefaultDeserializer(any(JsonDeserializer.class))).thenReturn(false);

        result = new StringArrayDeserializer().createContextual(ctxt, property);
        assertNotNull(result);
        assertEquals(StringArrayDeserializer.class, result.getClass());
        assertNotNull(result._elementDeserializer);
        assertEquals(deser, result._elementDeserializer);
    }
}