package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException, CloneNotSupportedException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        Map<Integer, String> clonedMap = SerializationUtils.clone(map);

        assertEquals(map, clonedMap);
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithNullObject() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserialize() throws SerializationException, IOException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        byte[] serializedData = SerializationUtils.serialize(map);
        Map<Integer, String> deserializedMap = (Map<Integer, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertEquals(map, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testSerializeAndDeserializeWithObjectOutputStream() throws SerializationException, IOException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Map<Integer, String> deserializedMap = (Map<Integer, String>) SerializationUtils.deserialize(byteArrayInputStream);

        assertEquals(map, deserializedMap);
    }

    @Test
    public void testSerializableObject() throws SerializationException, IOException {
        Serializable serializableObject = new Object() {
        };

        byte[] serializedData = SerializationUtils.serialize(serializableObject);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertNotNull(deserializedObject);
        assertEquals(serializableObject, deserializedObject);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException, ClassNotFoundException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Object deserializedMap = SerializationUtils.deserialize(inputStream);

        assertEquals(map.getClass().getName(), deserializedMap.getClass().getName());
    }
}