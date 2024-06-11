package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    /**
     * Test cloning using SerializationUtils
     */
    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("testKey", "testValue");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(originalMap.size(), clonedMap.size());
        Assert.assertEquals(originalMap.get("testKey"), clonedMap.get("testKey"));
    }

    /**
     * Test serialization and deserialization of a simple object
     */
    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        String testString = "testString";

        // Serialize
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testString, byteArrayOutputStream);
        byte[] serializedData = byteArrayOutputStream.toByteArray();

        // Deserialize
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        String deserializedString = (String) SerializationUtils.deserialize(byteArrayInputStream);

        Assert.assertEquals(testString, deserializedString);
    }

    /**
     * Test serialization and deserialization of a complex object ( HashMap )
     */
    @Test
    public void testSerializationAndDeserializationOfComplexObject() throws IOException, ClassNotFoundException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("testKey1", "testValue1");
        originalMap.put("testKey2", "testValue2");

        // Serialize
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, byteArrayOutputStream);
        byte[] serializedData = byteArrayOutputStream.toByteArray();

        // Deserialize
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(byteArrayInputStream);

        Assert.assertNotNull(deserializedMap);
        Assert.assertEquals(originalMap.size(), deserializedMap.size());
        Assert.assertEquals(originalMap.get("testKey1"), deserializedMap.get("testKey1"));
        Assert.assertEquals(originalMap.get("testKey2"), deserializedMap.get("testKey2"));
    }

    /**
     * Test cloning an null object using SerializationUtils
     */
    @Test(expected = NullPointerException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    /**
     * Test serialization of a null object
     */
    @Test(expected = NullPointerException.class)
    public void testSerializationNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    /**
     * Test deserialization of a null object
     */
    @Test(expected = NullPointerException.class)
    public void testDeserializationNullObject() {
        SerializationUtils.deserialize(null);
    }
}