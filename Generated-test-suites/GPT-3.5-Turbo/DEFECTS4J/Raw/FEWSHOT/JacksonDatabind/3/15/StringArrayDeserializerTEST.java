package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\",\"bar\",\"baz\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String[] expected = {"foo", "bar", "baz"};
        assertArrayEquals(expected, StringArrayDeserializer.instance.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_WithNulls() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\",null,\"baz\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String[] expected = {"foo", null, "baz"};
        assertArrayEquals(expected, StringArrayDeserializer.instance.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_WithEmptyArray() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String[] expected = {};
        assertArrayEquals(expected, StringArrayDeserializer.instance.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_WithSingleValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"foo\"");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String[] expected = {"foo"};
        assertArrayEquals(expected, StringArrayDeserializer.instance.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserialize_WithSingleNullValue() throws IOException {
        JsonParser jp = new JsonFactory().createParser("null");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        String[] expected = {null};
        assertArrayEquals(expected, StringArrayDeserializer.instance.deserialize(jp, ctxt));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"foo\",\"bar\",\"baz\"]");
        DeserializationContext ctxt = new DeserializationContext(null, null);
        TypeDeserializer typeDeserializer = new TypeDeserializer(null, null);
        Object result = StringArrayDeserializer.instance.deserializeWithType(jp, ctxt, typeDeserializer);
        assertTrue(result instanceof String[]);
        String[] expected = {"foo", "bar", "baz"};
        assertArrayEquals(expected, (String[])result);
    }
    
}