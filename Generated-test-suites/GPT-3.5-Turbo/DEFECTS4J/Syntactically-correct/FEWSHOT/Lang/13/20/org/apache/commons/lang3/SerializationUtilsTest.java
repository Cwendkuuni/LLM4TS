package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    @Test
    public void testClone() {
        // Test cloning a null object
        assertNull(SerializationUtils.clone(null));

        // Test cloning a non-null object
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test serializing a null object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        byte[] serializedNull = baos.toByteArray();
        assertEquals(0, serializedNull.length);

        // Test serializing a non-null object
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("key1", "value1");
        dataMap.put("key2", "value2");
        baos.reset();
        SerializationUtils.serialize(dataMap, baos);
        byte[] serializedData = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            Object deserializedObject = ois.readObject();
            assertEquals(dataMap, deserializedObject);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ois.close();
        bais.close();
    }

    @Test
    public void testDeserialize() throws IOException {
        // Test deserializing a null object
        byte[] serializedNull = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedNull);
        assertNull(SerializationUtils.deserialize(bais));
        bais.close();

        // Test deserializing a non-null object
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("key1", "value1");
        dataMap.put("key2", "value2");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(dataMap);
        oos.close();
        byte[] serializedData = baos.toByteArray();
        ByteArrayInputStream deserializedBais = new ByteArrayInputStream(serializedData);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(deserializedBais);
        assertEquals(dataMap, deserializedMap);
        deserializedBais.close();
    }
}