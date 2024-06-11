package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new StringArrayDeserializer();
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"abc\",\"def\",\"ghi\"]";
        String[] expected = {"abc", "def", "ghi"};
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithNullValues() throws Exception {
        String json = "[\"abc\",null,\"def\",null,\"ghi\"]";
        String[] expected = {"abc", null, "def", null, "ghi"};
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws Exception {
        String json = "[]";
        String[] expected = {};
        String[] result = deserializer.deserialize(json);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWithNonArray() throws Exception {
        String json = "\"abc\"";
        deserializer.deserialize(json);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWithInvalidJson() throws Exception {
        String json = "[\"abc\",\"def\",]";
        deserializer.deserialize(json);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        String json = "[\"abc\",\"def\",\"ghi\"]";
        String[] expected = {"abc", "def", "ghi"};
        String[] result = deserializer.deserializeWithType(json);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWithTypeWithNonArray() throws Exception {
        String json = "\"abc\"";
        deserializer.deserializeWithType(json);
    }

    @Test
    public void testDeserializeWithTypeWithNullValues() throws Exception {
        String json = "[\"abc\",null,\"def\",null,\"ghi\"]";
        String[] expected = {"abc", null, "def", null, "ghi"};
        String[] result = deserializer.deserializeWithType(json);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeWithTypeWithEmptyArray() throws Exception {
        String json = "[]";
        String[] expected = {};
        String[] result = deserializer.deserializeWithType(json);
        assertArrayEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWithTypeWithInvalidJson() throws Exception {
        String json = "[\"abc\",\"def\",]";
        deserializer.deserializeWithType(json);
    }

}