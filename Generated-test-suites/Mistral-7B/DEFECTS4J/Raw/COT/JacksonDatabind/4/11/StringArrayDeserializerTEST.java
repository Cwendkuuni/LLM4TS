package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Tests for StringArrayDeserializer class
 */
public class StringArrayDeserializerTest {

    @Test
    public void testDefaultConstructor() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        assertNotNull(deserializer);
        assertEquals(String[].class, deserializer.getClass());
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testCustomConstructor() throws Exception {
        JsonDeserializer<String> elementDeserializer = createMockDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        assertEquals(String[].class, deserializer.getClass());
        assertEquals(elementDeserializer, deserializer._elementDeserializer);
    }

    @Test
    public void testDeserializeStandard() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("['one', 'two', 'three']");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"one", "two", "three"}, result);
    }

    @Test
    public void testDeserializeCustom() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createMockDeserializer());
        JsonParser parser = createParserWithJsonString("['one', 'two', 'three']");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"one", "two", "three"}, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("{'one': 'two'}");
        deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeNullPointer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createMockDeserializer());
        JsonParser parser = null;
        deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeEmptyJsonArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("[]");
        deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserializeEmptyJsonObject() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("{}");
        deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
    }

    @Test
    public void testDeserializeNullValues() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("['one', null, 'three']");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"one", null, "three"}, result);
    }

    @Test
    public void testDeserializeEmptyStrings() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("['', 'two', '']");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"", "", ""}, result);
    }

    @Test
    public void testDeserializeEmptyJsonArrayWithNull() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("[null]");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"null"}, result);
    }

    @Test
    public void testDeserializeEmptyJsonArrayWithEmptyStrings() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        JsonParser parser = createParserWithJsonString("['']");
        String[] result = deserializer.deserialize(parser, new ObjectMapper().getDeserializationContext());
        assertArrayEquals(new String[]{"", ""}, result);
    }

    @Test
    public void testCreateContextualNullDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        assertNotNull(deserializer);
        assertEquals(String[].class, deserializer.getClass());
        assertNull(deserializer._elementDeserializer);
    }

    @Test
    public void testCreateContextualDefaultDeserializer() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createMockDeserializer());
        deserializer.createContextual(new ObjectMapper().getDeserializationContext(), null);
        assertNotNull(deserializer._elementDeserializer);
        assertEquals(String.class, deserializer._elementDeserializer.getClass());
    }

    @Test
    public void testCreateContextualCustomDeserializer() throws Exception {
        JsonDeserializer<String> customDeserializer = createMockDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        JsonDeserializer<?> result = deserializer.createContextual(new ObjectMapper().getDeserializationContext(), null);
        assertEquals(StringArrayDeserializer.class, result.getClass());
    }

    @Test
    public void testCreateContextualCustomDeserializerWithType() throws Exception {
        JsonDeserializer<String> customDeserializer = createMockDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        TypeDeserializer typeDeserializer = new StdTypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        JsonDeserializer<?> result = deserializer.createContextual(new ObjectMapper().getDeserializationContext(), null);
        assertEquals(StringArrayDeserializer.class, result.getClass());
    }

    @Test
    public void testCreateContextualDefaultDeserializerWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer(createMockDeserializer());
        TypeDeserializer typeDeserializer = new StdTypeDeserializer() {
            @Override
            public Object deserializeTypedFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
                return null;
            }
        };
        JsonDeserializer<?> result = deserializer.createContextual(new ObjectMapper().getDeserializationContext(), null);
        assertNotSame(deserializer, result);
    }

    private JsonParser createParserWithJsonString(String jsonString) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new StringReader(jsonString));
        return parser;
    }

    private JsonDeserializer<String> createMockDeserializer() {
        return new JsonStringDeserializer() {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText();
            }
        };
    }
}