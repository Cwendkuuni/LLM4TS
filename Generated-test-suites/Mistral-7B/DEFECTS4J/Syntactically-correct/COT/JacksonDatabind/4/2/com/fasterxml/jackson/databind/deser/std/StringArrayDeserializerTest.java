package com.example.stringarraydeserializertest;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserializeDefault() throws IOException {
        String json = "[\"hello\", \"world\"]";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{"hello", "world"}, result);
    }

    @Test
    public void testDeserializeWithCustomElementDeserializer() throws IOException {
        JsonDeserializer<String> customElementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        String json = "[\"hello\", \"world\"]";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), deserializer.getClass());
        Assert.assertArrayEquals(new String[]{"HELLO", "WORLD"}, result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidInput() throws IOException {
        String json = "{ \"invalid\": 1 }";
        objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
    }

    @Test
    public void testDeserializeEmptyArray() throws IOException {
        String json = "[]";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeNullArray() throws IOException {
        String json = "null";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testDeserializeNullElement() throws IOException {
        String json = "[null]";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{null}, result);
    }

    @Test
    public void testDeserializeEmptyStringElement() throws IOException {
        String json = "[\"\"]";
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), StringArrayDeserializer.class);
        Assert.assertArrayEquals(new String[]{""}, result);
    }

    @Test
    public void testDeserializeCustomType() throws IOException {
        String json = "[\"apple\", \"banana\", \"orange\"]";
        TypeReference<String[]> typeReference = new TypeReference<String[]>() {};
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), typeReference);
        Assert.assertArrayEquals(new String[]{"apple", "banana", "orange"}, result);
    }

    @Test
    public void testDeserializeEmptyInputStream() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Assert.assertArrayEquals(new String[]{}, deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeNullInput() throws IOException {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();
        Assert.assertArrayEquals(new String[]{}, deserializer.deserialize(null, null));
    }

    @Test
    public void testDeserializeWithBeanProperty() throws IOException {
        JsonDeserializer<String> customElementDeserializer = new StdDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return jp.getText().toUpperCase();
            }
        };

        BeanProperty property = new BeanProperty("testProperty", String[].class);
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customElementDeserializer);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "[\"hello\", \"world\"]";

        TestClass testClass = new TestClass();
        String[] result = objectMapper.readValue(new ByteArrayInputStream(json.getBytes()), new TypeReference<TestClass>() {});
        Assert.assertEquals(testClass, result[0]);
        Assert.assertArrayEquals(new String[]{"HELLO", "WORLD"}, result[1]);
    }

    static class TestClass {
        String testProperty;
        String[] testArrayProperty;
    }
}

class StdDeserializer<T> extends JsonDeserializer<T> {
    // empty constructor
}