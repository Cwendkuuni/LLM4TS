package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import org.junit.Test;

/**
 * Test class for StringArrayDeserializer
 */
public class StringArrayDeserializerTest {

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)}
     */
    @Test
    public void testSerialize() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\",\"Test2\",\"Test3\"]";
        String[] expected = {"Test1", "Test2", "Test3"};

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} with null values
     */
    @Test
    public void testSerializeWithNullValues() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\", null, \"Test3\"]";
        String[] expected = {"Test1", null, "Test3"};

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} with an empty JSON array
     */
    @Test
    public void testSerializeEmptyArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        String[] expected = {};

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserialize(JsonParser, DeserializationContext)} with a JSON array containing non-string values
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeNonStringValues() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";

        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)}
     */
    @Test
    public void testSerializeCustomDeserializer() throws Exception {
        String[] expected = {"Test1", "Test2", "Test3"};
        JsonDeserializer<String> elementDeserializer = new StandardSerializer<String>(String.class) {};

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\",\"Test2\",\"Test3\"]";

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)} with null values
     */
    @Test
    public void testSerializeCustomDeserializerWithNullValues() throws Exception {
        JsonDeserializer<String> elementDeserializer = new StandardSerializer<String>(String.class) {};

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\", null, \"Test3\"]";
        String[] expected = {"Test1", null, "Test3"};

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)} with an empty JSON array
     */
    @Test
    public void testSerializeCustomDeserializerEmptyArray() throws Exception {
        JsonDeserializer<String> elementDeserializer = new StandardSerializer<String>(String.class) {};

        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[]";
        String[] expected = {};

        String[] result = mapper.readValue(json, deserializer.getType());
        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeCustom(JsonParser, DeserializationContext)} with a JSON array containing non-string values
     */
    @Test(expected = JsonMappingException.class)
    public void testSerializeCustomDeserializerNonStringValues() throws Exception {
        JsonDeserializer<String> elementDeserializer = new StandardSerializer<String>(String.class) {};
        StringArrayDeserializer deserializer = new StringArrayDeserializer(elementDeserializer);
        ObjectMapper mapper = new ObjectMapper();
        String json = "[1, 2, 3]";

        mapper.readValue(json, deserializer.getType());
    }

    /**
     * Test method for {@link StringArrayDeserializer#deserializeWithType(JsonParser, DeserializationContext, TypeDeserializer)}
     */
    @Test
    public void testSerializeWithTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[\"Test1\",\"Test2\",\"Test3\"]";

        TypeDeserializer typeDeserializer = new ArrayTypeDeserializer(new SimpleType("String[]"));
        String[] result = mapper.readValue(json, typeDeserializer);
        String[] expected = {"Test1", "Test2", "Test3"};

        assertArrayEquals(expected, result);
    }

    /**
     * Test method for {@link StringArrayDeserializer#handleNonArray(JsonParser, DeserializationContext)}
     */
    @Test(expected = JsonMappingException.class)
    public void testHandleNonArray() throws Exception {
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"value\": \"Test1\" }";

        mapper.readValue(json, deserializer.getType());
    }
}