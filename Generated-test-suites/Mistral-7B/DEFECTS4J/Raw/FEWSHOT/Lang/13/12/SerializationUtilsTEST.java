package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        @SuppressWarnings("rawtypes")
        Map clonedMap = SerializationUtils.clone(map);

        assertEquals(map, clonedMap);
        assertNotNull(clonedMap);
        assertNull(clonedMap.getClass().getDeclaredField("serialVersionUID"));

        for (Object key : map.keySet()) {
            assertEquals(map.get(key), clonedMap.get(key));
        }

        @SuppressWarnings("rawtypes")
        Map clonedMap2 = SerializationUtils.clone(clonedMap);

        assertEquals(clonedMap, clonedMap2);
        assertNotNull(clonedMap2);

        // Test null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        byte[] serializedMap = SerializationUtils.serialize(map);

        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));

        assertEquals(map, deserializedMap);
        assertNotNull(deserializedMap);
        assertNull(deserializedMap.getClass().getDeclaredField("serialVersionUID"));

        for (Object key : map.keySet()) {
            assertEquals(map.get(key), deserializedMap.get(key));
        }
    }

    @Test
    public void testNullOnDeserialization() throws IOException, ClassNotFoundException {
        // Test null on deserialization
        assertNull(SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testThrowExceptionOnDeserialization() throws IOException, ClassNotFoundException {
        byte[] serializedObjectData = new byte[1];

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedObjectData));
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // expected
        }
    }
}