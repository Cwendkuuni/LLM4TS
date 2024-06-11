package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    // Test for clone method
    @Test
    public void testClone() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Serializable serializedMap = SerializationUtils.serialize(map);
        Map<String, String> clonedMap = SerializationUtils.clone(map);

        // Verify that the cloned map is equal to the original map
        Assert.assertEquals(map, clonedMap);

        // Verify that the cloned map's serialization is equal to the original map's serialization
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        byte[] mapData = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(mapData);
        Map<String, String> deserializedMap = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(map, deserializedMap);

        // Verify that the cloned map's serialization is equal to the cloned map's serialization
        byte[] clonedMapData = SerializationUtils.serialize(clonedMap);
        ByteArrayInputStream clonedInputStream = new ByteArrayInputStream(clonedMapData);
        Map<String, String> clonedDeserializedMap = SerializationUtils.deserialize(clonedInputStream);
        Assert.assertEquals(clonedMap, clonedDeserializedMap);

        // Test with null input
        Assert.assertNull(SerializationUtils.clone(null));
    }

    // Test for serialize method
    @Test
    public void testSerialize() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        byte[] mapData = outputStream.toByteArray();

        // Verify that the deserialized map is equal to the original map
        ByteArrayInputStream inputStream = new ByteArrayInputStream(mapData);
        Map<String, String> deserializedMap = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(map, deserializedMap);

        // Test with null input
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
    }

    // Test for deserialize method
    @Test
    public void testDeserialize() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        byte[] mapData = outputStream.toByteArray();

        // Verify that the deserialized map is equal to the original map
        ByteArrayInputStream inputStream = new ByteArrayInputStream(mapData);
        Map<String, String> deserializedMap = SerializationUtils.deserialize(inputStream);
        Assert.assertEquals(map, deserializedMap);

        // Test with null input
        Assert.assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }
}