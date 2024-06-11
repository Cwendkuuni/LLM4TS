package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_Typical() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String json = "[\"apple\", \"banana\", \"orange\"]";
        String[] expected = {"apple", "banana", "orange"};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_NullValue() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String json = "[\"apple\", null, \"orange\"]";
        String[] expected = {"apple", null, "orange"};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_EmptyStringAsNull() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String json = "[\"\", \"banana\", \"orange\"]";
        String[] expected = {null, "banana", "orange"};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    @Test(expected = MappingException.class)
    public void testDeserialize_NonArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String json = "\"apple\"";
        String[] result = deserializer.deserialize(json);
    }

    @Test
    public void testDeserialize_EmptyArray() {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String json = "[]";
        String[] expected = {};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithCustomDeserializer() {
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String json = "[\"apple\", \"banana\", \"orange\"]";
        String[] expected = {"APPLE", "BANANA", "ORANGE"};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithNullCustomDeserializer() {
        CustomStringDeserializer customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String json = "[\"apple\", null, \"orange\"]";
        String[] expected = {"APPLE", null, "ORANGE"};
        String[] result = deserializer.deserialize(json);

        assertArrayEquals(expected, result);
    }

    private class CustomStringDeserializer implements JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            String value = jp.getText();
            return value.toUpperCase();
        }

        @Override
        public String getNullValue() {
            return null;
        }
    }
}