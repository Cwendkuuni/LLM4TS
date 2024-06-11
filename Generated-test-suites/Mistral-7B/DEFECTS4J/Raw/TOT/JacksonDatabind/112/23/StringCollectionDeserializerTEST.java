package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static com.fasterxml.jackson.databind.JsonMapping.*;
import static org.junit.Assert.*;

/**
 * Test cases for {@link StringCollectionDeserializer} class
 */
public class StringCollectionDeserializerTest {

    private ObjectMapper mapper;
    private StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null, null, null, null);
    }

    /**
     * Test deserialization of empty JSON array into an empty list.
     */
    @Test
    public void testEmptyJsonArray() throws IOException {
        String json = "[]";
        List<String> result = mapper.readValue(json, deserializer.getType());
        assertTrue(result.isEmpty());
    }

    /**
     * Test deserialization of JSON array with single String value into a list containing that value.
     */
    @Test
    public void testSingleValueJsonArray() throws IOException {
        String json = "[\"hello\"]";
        List<String> result = mapper.readValue(json, deserializer.getType());
        assertEquals("hello", result.get(0));
    }

    /**
     * Test deserialization of JSON array with multiple String values into a list containing those values.
     */
    @Test
    public void testMultipleValuesJsonArray() throws IOException {
        String json = "[\"hello\", \"world\"]";
        List<String> result = mapper.readValue(json, deserializer.getType());
        assertEquals("hello", result.get(0));
        assertEquals("world", result.get(1));
    }

    /**
     * Test deserialization with a custom deserializer for String values.
     */
    @Test
    public void testCustomValueDeserializer() throws IOException {
        StringCollectionDeserializer customDeserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, null,
                new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        String result = p.nextTextValue();
                        return result.toUpperCase();
                    }
                }, null, null, null);

        String json = "[\"hello\", \"world\"]";
        List<String> result = mapper.readValue(json, customDeserializer.getType());
        assertEquals("HELLO", result.get(0));
        assertEquals("WORLD", result.get(1));
    }

    /**
     * Test deserialization with a JsonDeserializer for Collection.
     */
    @Test
    public void testDelegateDeserializer() throws IOException {
        String json = "[1, 2, 3]";
        JsonDeserializer<List<String>> delegateDeserializer = new StringDeserializer();

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(
                mapper.getTypeFactory().constructCollectionType(List.class, String.class),
                null, delegateDeserializer, null, null, null);

        List<String> result = mapper.readValue(json, deserializer.getType());
        assertEquals(Arrays.asList("1", "2", "3"), result);
    }

    /**
     * Test handling of null values.
     */
    @Test
    public void testNullValue() throws IOException {
        String json = "[null]";
        List<String> result = mapper.readValue(json, deserializer.getType());
        assertTrue(result.isEmpty());

        json = "[null, null]";
        result = mapper.readValue(json, deserializer.getType());
        assertEquals(Collections.nemptyList(), result);

        ObjectNode node = mapper.createObjectNode();
        node.put("foo", null);
        json = node.toString();
        result = mapper.readValue(json, deserializer.getType());
        assertEquals(Collections.nemptyList(), result);
    }

    /**
     * Test handling of empty JSON object.
     */
    @Test
    public void testEmptyJsonObject() throws IOException {
        String json = "{}";
        List<String> result = mapper.readValue(json, deserializer.getType());
        assertTrue(result.isEmpty());
    }

    /**
     * Test handling of JSON object with no "array" property.
     */
    @Test(expected = JsonMappingException.class)
    public void testMissingArrayProperty() throws IOException {
        String json = "{\"foo\": \"bar\"}";
        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test handling of JSON object with an invalid property name.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidArrayPropertyName() throws IOException {
        String json = "{\"invalidArrayName\": [\"foo\", \"bar\"]}";
        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test handling of JSON object with an invalid array type.
     */
    @Test(expected = JsonMappingException.class)
    public void testInvalidArrayType() throws IOException {
        String json = "{{\"array\": [1, 2, 3]}}";
        mapper.readValue(json, deserializer.getType());
    }
}