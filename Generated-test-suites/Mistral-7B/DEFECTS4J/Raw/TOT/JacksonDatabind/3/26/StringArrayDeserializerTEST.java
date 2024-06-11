package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class StringArrayDeserializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testDefaultDeserializer() throws JsonProcessingException {
        String json = "[\"foo\",\"bar\",\"baz\"]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testCustomDeserializer() throws JsonProcessingException {
        JsonDeserializer<String> customDeserializer = mapper.constructDeserializer(String.class, new SimpleStringSerializer());
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);
        String json = "[null, null, null]";
        String[] result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
        assertArrayEquals(new String[]{null, null, null}, result);
    }

    @Test
    public void testEmptyArray() throws JsonProcessingException {
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testNullValue() throws JsonProcessingException {
        String json = "null";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
        assertNull(result);
    }

    @Test
    public void testEmptyStringAsNull() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        String json = "[]";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        String[] result = deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArray() throws JsonProcessingException {
        String json = "{\"foo\":\"bar\"}";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
    }

    @Test(expected = JsonProcessingException.class)
    public void testInvalidJson() throws JsonProcessingException {
        String json = "\"foo\"";
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        deserializer.deserialize(new JsonFactory(), mapper.getDeserializationContext(), new JsonParser(new StringReader(json)));
    }
}