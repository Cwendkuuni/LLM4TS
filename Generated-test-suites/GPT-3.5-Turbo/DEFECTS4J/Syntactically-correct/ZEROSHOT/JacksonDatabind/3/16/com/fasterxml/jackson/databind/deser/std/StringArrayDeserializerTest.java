package com.fasterxml.jackson.databind.deser.std;

import org.junit.Assert;
import org.junit.Test;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer = StringArrayDeserializer.instance;

    @Test
    public void testDeserialize() throws Exception {
        String json = "[\"apple\", \"banana\", \"cherry\"]";
        String[] expected = new String[] {"apple", "banana", "cherry"};
        Assert.assertArrayEquals(expected, stringArrayDeserializer.deserialize(json));
    }

    @Test
    public void testDeserializeWithNull() throws Exception {
        String json = "[\"apple\", null, \"cherry\"]";
        String[] expected = new String[] {"apple", null, "cherry"};
        Assert.assertArrayEquals(expected, stringArrayDeserializer.deserialize(json));
    }

    @Test
    public void testDeserializeEmptyArray() throws Exception {
        String json = "[]";
        String[] expected = new String[] {};
        Assert.assertArrayEquals(expected, stringArrayDeserializer.deserialize(json));
    }

    @Test(expected = Exception.class)
    public void testDeserializeNonArray() throws Exception {
        String json = "\"apple\"";
        stringArrayDeserializer.deserialize(json);
    }
}