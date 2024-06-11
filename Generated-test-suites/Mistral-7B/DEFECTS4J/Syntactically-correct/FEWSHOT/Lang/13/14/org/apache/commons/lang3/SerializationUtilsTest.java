package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        // Test cloning a simple object
        Object original = new Object() {
        };
        Object cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Test cloning a Map
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);
        Object clonedMap = SerializationUtils.clone(map);
        assertEquals(map, clonedMap);
        assertSame(map.getClass(), clonedMap.getClass());

        // Test cloning a nested object
        Map<String, Object> nestedMap = new HashMap<String, Object>();
        nestedMap.put("innerMap", new HashMap<String, String>() {
            {
                put("key", "value");
            }
        });
        Object clonedNestedMap = SerializationUtils.clone(nestedMap);
        assertEquals(nestedMap, clonedNestedMap);
        assertSame(nestedMap.getClass(), clonedNestedMap.getClass());
        assertSame(nestedMap.get("innerMap"), clonedNestedMap.get("innerMap"));
        assertSame(nestedMap.get("innerMap").getClass(), clonedNestedMap.get("innerMap").getClass());
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        // Test serializing and deserializing a simple object
        Object original = new Object() {
        };
        byte[] objectData = SerializationUtils.serialize(original);
        Object cloned = SerializationUtils.deserialize(objectData);
        assertEquals(original, cloned);

        // Test serializing and deserializing a Map
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("one", 1);
        map.put("two", 2);
        byte[] mapData = SerializationUtils.serialize(map);
        Object clonedMap = SerializationUtils.deserialize(mapData);
        assertEquals(map, clonedMap);
        assertSame(map.getClass(), clonedMap.getClass());

        // Test serializing and deserializing a nested object
        Map<String, Object> nestedMap = new HashMap<String, Object>();
        nestedMap.put("innerMap", new HashMap<String, String>() {
            {
                put("key", "value");
            }
        });
        byte[] nestedMapData = SerializationUtils.serialize(nestedMap);
        Object clonedNestedMap = SerializationUtils.deserialize(nestedMapData);
        assertEquals(nestedMap, clonedNestedMap);
        assertSame(nestedMap.getClass(), clonedNestedMap.getClass());
        assertSame(nestedMap.get("innerMap"), clonedNestedMap.get("innerMap"));
        assertSame(nestedMap.get("innerMap").getClass(), clonedNestedMap.get("innerMap").getClass());
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        // Test custom ClassLoaderAwareObjectInputStream
        Object original = new Object() {
        };
        byte[] objectData = SerializationUtils.serialize(original);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectData);
        Object cloned = new ClassLoaderAwareObjectInputStream(inputStream, getClass().getClassLoader()).readObject();
        assertEquals(original, cloned);
    }

}