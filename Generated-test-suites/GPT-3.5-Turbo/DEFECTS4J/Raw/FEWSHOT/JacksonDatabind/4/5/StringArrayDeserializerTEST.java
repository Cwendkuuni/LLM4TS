package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

public class StringArrayDeserializerTest {

    // Test stripLeadingAndTrailingQuotes
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("\"", Util.stripLeadingAndTrailingQuotes("\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    // Test stripLeadingHyphens
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    // Test deserialize with null value
    @Test
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    // Test deserialize with custom element deserializer
    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        deserializer._elementDeserializer = mock(JsonDeserializer.class);
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result);
    }

    // Test deserialize with null element value
    @Test
    public void testDeserializeWithNullElementValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.END_ARRAY);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(buffer.appendCompletedChunk(any(Object[].class))).thenReturn(new Object[1]);
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(buffer);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertNull(result[0]);
    }

    // Test deserialize with non-null element value
    @Test
    public void testDeserializeWithNonNullElementValue() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("foo");
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[1]);
        when(buffer.appendCompletedChunk(any(Object[].class))).thenReturn(new Object[1]);
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(buffer);

        String[] result = deserializer.deserialize(jp, ctxt);

        assertEquals("foo", result[0]);
    }

    // Test deserializeWithType
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn(new String[0]);

        Object result = deserializer.deserializeWithType(jp, ctxt, typeDeserializer);

        assertEquals(new String[0], result);
    }

    // Test handleNonArray without accept single value as array
    @Test(expected=JsonMappingException.class)
    public void testHandleNonArrayWithoutAcceptSingleValueAsArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(false);

        deserializer.handleNonArray(jp, ctxt);
    }

    // Test handleNonArray with accept single value as array
    @Test
    public void testHandleNonArrayWithAcceptSingleValueAsArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(deserializer._parseString(jp, ctxt)).thenReturn("foo");

        String[] result = deserializer.handleNonArray(jp, ctxt);

        assertEquals("foo", result[0]);
    }

    // Test createContextual with default element deserializer
    @Test
    public void testCreateContextualWithDefaultElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deser);
        when(deserializer.isDefaultDeserializer(deser)).thenReturn(true);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(deserializer, result);
    }

    // Test createContextual with custom element deserializer
    @Test
    public void testCreateContextualWithCustomElementDeserializer() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        assertEquals(deserializer, result);
    }
}