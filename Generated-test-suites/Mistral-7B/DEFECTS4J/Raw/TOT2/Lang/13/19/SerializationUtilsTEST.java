package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
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

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    // Test for clone method

    /**
     * Test cloning a simple object
     */
    @Test
    public void testCloneSimpleObject() {
        String original = new String("Hello World");
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    /**
     * Test cloning a complex object (HashMap)
     */
    @Test
    public void testCloneComplexObject() {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original.size(), cloned.size());
        Assert.assertEquals(original.get("One"), cloned.get("One"));
        Assert.assertEquals(original.get("Two"), cloned.get("Two"));
    }

    // Test for serialize method

    /**
     * Test serializing and deserializing a simple object
     */
    @Test
    public void testSerializeDeserializeSimpleObject() throws IOException, ClassNotFoundException {
        String original = new String("Hello World");
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test serializing and deserializing a complex object (HashMap)
     */
    @Test
    public void testSerializeDeserializeComplexObject() throws IOException, ClassNotFoundException {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);

        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original.size(), deserialized.size());
        Assert.assertEquals(original.get("One"), deserialized.get("One"));
        Assert.assertEquals(original.get("Two"), deserialized.get("Two"));
    }

    // Test for deserialize method

    /**
     * Test deserializing an object from an InputStream
     */
    @Test
    public void testDeserializeFromInputStream() throws IOException, ClassNotFoundException {
        String original = new String("Hello World");
        byte[] serialized = SerializationUtils.serialize(original);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        String deserialized = (String) SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(original, deserialized);
    }

    /**
     * Test deserializing a complex object (HashMap) from an InputStream
     */
    @Test
    public void testDeserializeComplexObjectFromInputStream() throws IOException, ClassNotFoundException {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);

        byte[] serialized = SerializationUtils.serialize(original);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(original.size(), deserialized.size());
        Assert.assertEquals(original.get("One"), deserialized.get("One"));
        Assert.assertEquals(original.get("Two"), deserialized.get("Two"));
    }

    // Test for edge cases and exception handling

    /**
     * Test cloning a null object
     */
    @Test(expected = SerializationException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    /**
     * Test serializing a null object
     */
    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    /**
     * Test deserializing a null object
     */
    @Test(expected = SerializationException.class)
    public void testDeserializeNullObject() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test cloning an array
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneArray() {
        int[] array = {1, 2, 3};
        SerializationUtils.clone(array);
    }

    /**
     * Test cloning a non-Serializable object
     */
    @Test(expected = SerializationException.class)
    public void testCloneNonSerializableObject() {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        SerializationUtils.clone(nonSerializableObject);
    }

    // Non-SerializableObject is a dummy class, create it in a separate file

}