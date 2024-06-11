package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Test for clone method
    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test(expected = NullPointerException.class)
    public void testCloneWithNullInput() {
        SerializationUtils.clone(null);
    }

    // Test for serialize method with OutputStream
    @Test
    public void testSerializeWithOutputStream() {
        String input = "Test Input";
        byte[] output = SerializationUtils.serialize(input);
        String deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(output));
        assertEquals(input, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullOutputStream() {
        SerializationUtils.serialize(null, null);
    }

    // Test for serialize method with Serializable object
    @Test
    public void testSerializeWithSerializableObject() {
        String input = "Test Input";
        byte[] output = SerializationUtils.serialize(input);
        String deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(output));
        assertEquals(input, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullInput() {
        SerializationUtils.serialize(null);
    }

    // Test for deserialize method with InputStream
    @Test
    public void testDeserializeWithInputStream() {
        String input = "Test Input";
        byte[] serialized = SerializationUtils.serialize(input);
        String deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(input, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    // Test for deserialize method with byte array
    @Test
    public void testDeserializeWithByteArray() {
        String input = "Test Input";
        byte[] serialized = SerializationUtils.serialize(input);
        String deserialized = SerializationUtils.deserialize(serialized);
        assertEquals(input, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    // Test for ClassLoaderAwareObjectInputStream
    @Test
    public void testClassLoaderAwareObjectInputStream() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));
        assertEquals(map, deserializedMap);
    }

}