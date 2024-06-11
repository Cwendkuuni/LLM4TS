package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultDeserialization() throws Exception {
        String json = "[\"foo\",\"bar\",\"baz\"]";
        String[] result = mapper.readValue(json, StringArrayDeserializer.class);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
    }

    @Test
    public void testCustomDeserialization() throws Exception {
        String json = "[null, null, null]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        String[] result = mapper.readValue(json, deserializer.getClass());
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testEmptyArray() throws Exception {
        String json = "[]";
        StringArrayDeserializer deserializer = new StringArrayDeserializer(null);
        String[] result = mapper.readValue(json, deserializer.getClass());
        assertEquals(0, result.length);
    }

    @Test
    public void testNonArray() throws Exception {
        String json = "\"foo\"";
        try {
            mapper.readValue(json, StringArrayDeserializer.class);
            fail();
        } catch (JsonProcessingException e) {
            assertEquals("Unexpected character ('\"' (code 34)): expected a valid value (number, String, array, object), at [Source: (StringReader); line: 1, column: 1]", e.getMessage());
        }
    }

    @Test
    public void testContextualDeserialization() throws Exception {
        String json = "[null, null, null]";
        JsonDeserializer<String> customDeserializer = new StringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String[] result = mapper.readValue(json, deserializer.getClass());
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
        assertNull(result[2]);
    }

    static class StringDeserializer extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}