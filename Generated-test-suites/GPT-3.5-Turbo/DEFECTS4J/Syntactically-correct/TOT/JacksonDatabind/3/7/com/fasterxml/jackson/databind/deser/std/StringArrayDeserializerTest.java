package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_typicalUseCase() {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        String[] expected = {"apple", "banana", "cherry"};

        String[] result = StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_emptyArray() {
        String json = "[]";
        String[] expected = {};

        String[] result = StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_singleValueAsArray() {
        String json = "\"apple\"";
        String[] expected = {"apple"};

        String[] result = StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_nullValue() {
        String json = "[null, null, null]";
        String[] expected = {null, null, null};

        String[] result = StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());

        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_invalidArray() {
        String json = "[\"apple\", \"banana\"";

        StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());
    }

    @Test(expected = IOException.class)
    public void testDeserialize_invalidElement() {
        String json = "[\"apple\", 123, \"cherry\"]";

        StringArrayDeserializer.instance.deserialize(json, new DeserializationContext());
    }

}