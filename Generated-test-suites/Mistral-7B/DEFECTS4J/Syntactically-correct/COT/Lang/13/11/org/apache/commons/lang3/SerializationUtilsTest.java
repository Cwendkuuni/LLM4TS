package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
        Assert.assertNotSame(original, cloned);
    }

    @Test
    public void testSerializeAndDeserializeString() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeHashMap() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        byte[] serialized = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeIOException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            SerializationUtils.serialize(new Object() {
            }, outputStream);
            Assert.fail();
        } catch (IOException e) {
            throw new SerializationException("Expected IOException", e);
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(SerializationUtils.serialize(new Object()));
        try {
            SerializationUtils.deserialize(inputStream, null);
            Assert.fail();
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Expected ClassNotFoundException", e);
        }
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[512]);
        try {
            SerializationUtils.deserialize(inputStream);
            Assert.fail();
        } catch (IOException e) {
            throw new SerializationException("Expected IOException", e);
        }
    }

    @Test
    public void testDeserializeClosedInputStream() {
        ByteArrayInputStream inputStream = TestUtils.getClosedByteArrayInputStream();
        try {
            SerializationUtils.deserialize(inputStream);
            Assert.fail();
        } catch (IOException e) {
            // Expected IOException
        }
    }
}