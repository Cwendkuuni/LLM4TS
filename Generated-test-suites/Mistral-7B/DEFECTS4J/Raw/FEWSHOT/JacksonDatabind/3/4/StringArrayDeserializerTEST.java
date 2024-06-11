package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.test.BaseTest;

import java.io.IOException;

/**
 * Test cases for StringArrayDeserializer class.
 */
@JacksonStdImpl
public class StringArrayDeserializerTest extends BaseTest {

    /**
     * Test case for deserializing a String array from a JSON array using the default deserializer.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeDefault() throws IOException {
        String json = "[\"foo\", \"bar\", \"baz\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertEquals(3, result.length);
        assertEquals("foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("baz", result[2]);
    }

    /**
     * Test case for deserializing a String array from a JSON array with custom value deserializer.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeCustom() throws IOException {
        String json = "[\"foo\", \"bar\", \"baz\"]";
        JsonDeserializer<String> elementDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_custom";
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertEquals(3, result.length);
        assertEquals("foo_custom", result[0]);
        assertEquals("bar_custom", result[1]);
        assertEquals("baz_custom", result[2]);
    }

    /**
     * Test case for deserializing an empty JSON array.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeEmptyJsonArray() throws IOException {
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertArrayEquals(new String[0], result);
    }

    /**
     * Test case for deserializing a JSON array with only one value.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeSingleValueJsonArray() throws IOException {
        String json = "[\"foo\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertEquals(1, result.length);
        assertEquals("foo", result[0]);
    }

    /**
     * Test case for deserializing a JSON array with null value.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeNullValueJsonArray() throws IOException {
        String json = "[null]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertArrayEquals(new String[] { null }, result);
    }

    /**
     * Test case for deserializing a JSON array with non-String values.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonStringValueJsonArray() throws IOException {
        String json = "[1, 2, 3]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.readValue(json, String[].class, deserializer);
    }

    /**
     * Test case for deserializing a JSON array with custom content converter.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeCustomContentConverter() throws IOException {
        String json = "[42, 17.5, true, null]";
        ObjectMapper mapper = new ObjectMapper();
        String[] result
            = mapper.readValue(json, String[].class, new StringArrayDeserializer(new NumberToIntValueDeserializer()));
        assertArrayEquals(new String[] {"42", "17", "true", null}, result);
    }

    /**
     * Test case for deserializing a JSON array with custom contextual deserializer.
     *
     * @throws IOException if an I/O error occurs while deserializing the JSON string.
     */
    @Test
    public void testDeserializeCustomContextualDeserializer() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText() + "_custom";
            }
        };
        String json = "[\"foo\", \"bar\", \"baz\"]";
        JsonDeserializer<String[]> deserializer = new StringArrayDeserializer(stringDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String[] result = mapper.readValue(json, String[].class, deserializer);
        assertEquals(3, result.length);
        assertEquals("foo_custom", result[0]);
        assertEquals("bar_custom", result[1]);
        assertEquals("baz_custom", result[2]);
    }
}