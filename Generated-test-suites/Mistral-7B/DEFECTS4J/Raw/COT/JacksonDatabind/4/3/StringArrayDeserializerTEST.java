package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.Collections;

@SuppressWarnings("unchecked")
public class StringArrayDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.BeforeClass
    public static void setup() throws Exception {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(DeserializationFeature.FAIL_ON_EMPTY_ARRAYS, false);
    }

    // Basic test case for deserialize method
    @org.junit.Test
    public void testDeserialize() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertEquals(3, result.length);
        assertEquals("abc", result[0]);
        assertEquals("def", result[1]);
        assertEquals("ghi", result[2]);
    }

    // Test for handling null elements
    @org.junit.Test
    public void testDeserializeWithNullElements() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"abc\", null, \"ghi\"]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertEquals(3, result.length);
        assertEquals("abc", result[0]);
        assertNull(result[1]);
        assertEquals("ghi", result[2]);
    }

    // Test for handling empty array
    @org.junit.Test
    public void testDeserializeEmptyArray() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    // Test for deserializing with custom element deserializer
    @org.junit.Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"1\", \"2\", \"3\"]");
        JsonDeserializer<String> stringDeserializer = new SimpleStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(stringDeserializer);
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = deserializer.deserialize(jp, ctxt);
        assertEquals(3, result.length);
        assertEquals("1_custom", result[0]);
        assertEquals("2_custom", result[1]);
        assertEquals("3_custom", result[2]);
    }

    // Test for deserializing with TypeDeserializer
    @org.junit.Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        ObjectReader reader = MAPPER.readerForArrayOf(String.class);
        ArrayNode node = MAPPER.valueToTree(Collections.singletonList("CustomClass"));
        String[] result = reader.readValue(node);
        assertEquals(1, result.length);
        assertEquals("CustomClass", result[0].getClass().getSimpleName());
    }

    // Test for handling non-array input
    @org.junit.Test(expected = IOException.class)
    public void testDeserializeNonArray() throws IOException {
        JsonParser jp = new JsonFactory().createParser("\"abc\"");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer.instance.deserialize(jp, ctxt);
    }

    // Test for handling non-start array token
    @org.junit.Test(expected = IOException.class)
    public void testDeserializeNonStartArrayToken() throws IOException {
        JsonParser jp = new JsonFactory().createParser("{ \"key\": \"value\"}");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer.instance.deserialize(jp, ctxt);
    }

    // Test for handling null _elementDeserializer
    @org.junit.Test(expected = IOException.class)
    public void testDeserializeNullElementDeserializer() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"abc\", \"def\", \"ghi\"]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        deserializer.deserialize(jp, ctxt);
    }

    // Test for contextual deserialization
    @org.junit.Test
    public void testCreateContextual() throws Exception {
        StringArrayDeserializer deserializer = null;
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        BeanProperty property = new BeanProperty("test", String[].class);
        deserializer = StringArrayDeserializer.instance.createContextual(ctxt, property);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());

        JsonDeserializer<String> stringDeserializer = new SimpleStringDeserializer();
        deserializer = StringArrayDeserializer.instance.createContextual(ctxt, property);
        deserializer = (StringArrayDeserializer) deserializer.createContextual(ctxt, property);
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
        deserializer._elementDeserializer = stringDeserializer;
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
        assertEquals(StringDeserializer.class, deserializer._elementDeserializer.getClass());
    }

    // Test for deserializing with empty String
    @org.junit.Test
    public void testDeserializeEmptyString() throws IOException {
        JsonParser jp = new JsonFactory().createParser("[\"\"]");
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertEquals(0, result.length);
    }

    // Test for deserializing with null input
    @org.junit.Test
    public void testDeserializeNullInput() throws IOException {
        JsonParser jp = new JsonFactory().createParser(null);
        DeserializationContext ctxt = MAPPER.getDeserializationContext();
        String[] result = StringArrayDeserializer.instance.deserialize(jp, ctxt);
        assertArrayEquals(new String[0], result);
    }

    private static class SimpleStringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return "custom_" + jp.getValueAsString();
        }
    }
}