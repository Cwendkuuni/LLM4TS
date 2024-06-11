package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StringArrayDeserializerTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, StringArrayDeserializer.instance);
        mapper.registerModule(module);
    }

    @Test
    public void testDeserializationWithValidArray() throws IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        String[] expected = {"value1", "value2", "value3"};

        String[] result = mapper.readValue(json, String[].class);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializationWithNullValue() throws IOException {
        String json = "[\"value1\", null, \"value3\"]";
        String[] expected = {"value1", null, "value3"};

        String[] result = mapper.readValue(json, String[].class);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializationWithEmptyArray() throws IOException {
        String json = "[]";
        String[] expected = {};

        String[] result = mapper.readValue(json, String[].class);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializationWithEmptyStringAsNullObjectEnabled() throws IOException {
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        String json = "[\"\"]";
        String[] expected = {null};

        String[] result = mapper.readValue(json, String[].class);

        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithEmptyStringAsNullObjectDisabled() throws IOException {
        String json = "[\"\"]";

        String[] result = mapper.readValue(json, String[].class);
    }
}