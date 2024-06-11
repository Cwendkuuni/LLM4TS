package com.example.junit; // Replace with your testing package name

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private static final StringArrayDeserializer serializer = StringArrayDeserializer.instance;

    @Test
    public void testDeserializeNormalCase() throws JsonProcessingException {
        String json = "[\"abc\", \"def\"]";
        String[] expected = {"abc", "def"};
        String[] result = serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws JsonProcessingException {
        JsonDeserializer<String> elementDeserializer = new StdDeserializer<String>(String.class) {};
        StringArrayDeserializer serializerWithCustomElementDeserializer = new StringArrayDeserializer(elementDeserializer);
        String json = "[\"abc\", \"def\"]";
        String[] expected = {"abc", "def"};
        String[] result = serializerWithCustomElementDeserializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeUnexpectedToken() throws JsonProcessingException {
        String json = "{ \"key\": \"value\" }";
        String[] result = serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        // This test case should throw IOException as the JsonParser encounters an unexpected token.
    }

    @Test
    public void testDeserializeNull() throws JsonProcessingException {
        String json = "[]";
        String[] expected = null;
        String[] result = serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyArray() throws JsonProcessingException {
        String json = "[]";
        String[] expected = new String[0];
        String[] result = serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeEmptyStringAsNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        String json = "[]";
        String[] result = serializer.deserialize(new JsonFactory(), mapper, new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(new String[0], result);
        Assert.assertNull(result[0]);
    }

    @Test
    public void testDeserializeArrayWithCustomTypeDeserializer() throws JsonProcessingException {
        TypeDeserializer typeDeserializer = new StdTypeDeserializer<>(String[].class);
        String json = "[\"abc\", \"def\"]";
        String[] result = serializer.deserializeWithType(new JsonFactory(), new ObjectMapper(), typeDeserializer);
        Assert.assertArrayEquals(new String[]{"abc", "def"}, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArray() throws JsonProcessingException {
        String json = "\"abc\"";
        serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        // This test case should throw JsonMappingException as the JsonParser encounters a single value, not an array.
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        String json = "[null]";
        String[] expected = {null};
        String[] result = serializer.deserialize(new JsonFactory(), new ObjectMapper(), new JsonParser(new StringReader(json)));
        Assert.assertArrayEquals(expected, result);
    }
}