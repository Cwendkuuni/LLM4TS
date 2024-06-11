package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializationAndDeserialization() throws IOException {
        String[] input = new String[]{"foo", "bar", "baz"};
        String json = mapper.writeValueAsString(input);
        String[] output = mapper.readValue(json, String[].class);
        assertArrayEquals(input, output);
    }

    @Test
    public void testDeserializationWithCustomElementDeserializer() throws IOException {
        String json = "[\"abc\", \"def\"]";
        JsonDeserializer<String> elementDeserializer = mapper.deserializerForClass(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        String[] output = mapper.readValue(json, String[].class, deserializer);
        assertArrayEquals(new String[]{"abc", "def"}, output);
    }

    @Test
    public void testDeserializationWithNullElementDeserializer() throws IOException {
        String json = "[null, null]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] output = mapper.readValue(json, String[].class, deserializer);
        assertArrayEquals(new String[]{null, null}, output);
    }

    @Test
    public void testDeserializationWithEmptyArray() throws IOException {
        String json = "[]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        String[] output = mapper.readValue(json, String[].class, deserializer);
        assertArrayEquals(new String[]{}, output);
    }

    @Test
    public void testDeserializationWithNullJson() throws IOException {
        String json = null;
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertNull(mapper.readValue(json, String[].class, deserializer));
    }

    @Test
    public void testDeserializationWithEmptyJson() throws IOException {
        String json = "{}";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertNull(mapper.readValue(json, String[].class, deserializer));
    }

    @Test
    public void testDeserializationWithInvalidJson() throws IOException {
        String json = "{invalid: json}";
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        assertNull(mapper.readValue(json, String[].class, deserializer));
    }

    @Test
    public void testDeserializationWithContextualization() throws IOException {
        String json = "[\"abc\", \"def\"]";
        JsonDeserializer<String> elementDeserializer = mapper.deserializerForClass(String.class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        ObjectBuffer buffer = mapper.getObjectBuffer();
        Object[] chunk = buffer.resetAndStart();

        JsonParser jp = mapper.parserForObject(json);
        StringArrayDeserializer contextualDeserializer = deserializer.createContextual(mapper.getDeserializationContext(), null);
        String[] output = contextualDeserializer.deserialize(jp, mapper.getDeserializationContext());

        // Ensure that custom deserializer was used
        assertEquals(elementDeserializer, contextualDeserializer._elementDeserializer);
        assertArrayEquals(new String[]{"abc", "def"}, output);
    }
}