package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        Serializable serializedMap = SerializationUtils.serialize(map);
        Map<Integer, String> clonedMap = SerializationUtils.clone(serializedMap);

        Assert.assertNotNull(clonedMap);
        Assert.assertEquals(map.size(), clonedMap.size());
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Assert.assertEquals(entry.getKey(), clonedMap.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), clonedMap.get(entry.getKey()));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerialize() throws Exception {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);

        Map<Integer, String> deserializedMap = (Map<Integer, String>) SerializationUtils.deserialize(outputStream.toByteArray());

        Assert.assertNotNull(deserializedMap);
        Assert.assertEquals(map.size(), deserializedMap.size());
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            Assert.assertEquals(entry.getKey(), deserializedMap.get(entry.getKey()));
            Assert.assertEquals(entry.getValue(), deserializedMap.get(entry.getKey()));
        }
    }

    @Test
    public void testSerializeFail() {
        Object obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Simulated serialization exception");
            }
        };

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SerializationUtils.serialize(obj, outputStream);
            // This will not be reached
        } catch (SerializationException e) {
            Assert.assertEquals("Simulated serialization exception", e.getMessage());
        }
    }

    @Test
    public void testDeserializeFail() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[5]);

        try {
            SerializationUtils.deserialize(inputStream);
            // This will not be reached
        } catch (SerializationException e) {
            Assert.assertEquals("Stream corrupted (invalid stream header: 53442068)", e.getMessage());
        }
    }

}