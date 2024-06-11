package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test cases for {@link StringArrayDeserializer} class.
 */
public class StringArrayDeserializerTest {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Test deserialization of empty String array.
     */
    @Test
    public void testEmptyStringArray() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test deserialization of String array with null elements.
     */
    @Test
    public void testStringArrayWithNulls() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\n null,\n null,\n null,\n null\n ]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[] { null, null, null, null }, result);
    }

    /**
     * Test deserialization of String array with single element.
     */
    @Test
    public void testStringArrayWithSingleElement() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\n \"foo\"\n ]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[] { "foo" }, result);
    }

    /**
     * Test deserialization of String array with multiple elements.
     */
    @Test
    public void testStringArrayWithMultipleElements() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("[\n \"foo\",\n \"bar\",\n \"baz\"\n ]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[] { "foo", "bar", "baz" }, result);
    }

    /**
     * Test deserialization of String array with custom element deserializer.
     */
    @Test
    public void testStringArrayWithCustomElementDeserializer() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectReader reader = MAPPER.readerForArrayOf(String.class);
        DeserializationContext ctxt = jsonFactory.getDeserializationContext();
        TypeDeserializer typeDeserializer = MAPPER.getTypeDeserializerForAnnotation(String[].class, StringArrayDeserializer.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(typeDeserializer);

        JsonParser parser = jsonFactory.createParser("[\n \"foo\",\n \"bar\",\n \"baz\"\n ]");
        String[] result = deserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[] { "foo", "bar", "baz" }, result);
    }

    /**
     * Test handling of non-array input.
     */
    @Test
    public void testHandleNonArray() throws Exception {
        JsonParser parser = MAPPER.getFactory().createParser("{\"foo\": \"bar\"}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.handleNonArray(parser, ctxt);
        assertArrayEquals(new String[] { "bar" }, result);
    }

    /**
     * Test contextual deserialization.
     */
    @Test
    public void testContextualDeserialization() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectReader reader = MAPPER.readerForArrayOf(String.class);
        DeserializationContext ctxt = jsonFactory.getDeserializationContext();
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        TypeDeserializer typeDeserializer = MAPPER.getTypeDeserializerForAnnotation(String[].class, StringArrayDeserializer.class);

        JsonParser parser = jsonFactory.createParser("[\n \"foo\",\n \"bar\",\n \"baz\"\n ]");
        StringArrayDeserializer contextualDeserializer = deserializer.createContextual(ctxt, null);
        String[] result = contextualDeserializer.deserialize(parser, ctxt);
        assertArrayEquals(new String[] { "foo", "bar", "baz" }, result);
    }
}