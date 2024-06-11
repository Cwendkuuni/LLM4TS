package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Object original = new HashMap<>();
        HashMap<String, String> cloned = (HashMap<String, String>) SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertEquals(original, cloned);

        // test cloning a null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerDeserNullObject() {
        byte[] objectData = SerializationUtils.serialize(null);
        assertNull(SerializationUtils.deserialize(objectData));
    }

    @Test
    public void testSerDeserSerializableObject() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        byte[] objectData = SerializationUtils.serialize(map);
        Map<String, String> deserialized = (Map<String, String>) SerializationUtils.deserialize(objectData);
        assertEquals(map, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerDeserException() {
        Object obj = new Object() {
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Test exception");
            }
        };
        byte[] objectData = SerializationUtils.serialize(obj);
        SerializationUtils.deserialize(objectData);
    }

    @Test(expected = StreamCorruptedException.class)
    public void testSerDeserStreamCorruptedException() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        ObjectOutputStream out = new ObjectOutputStream(baos);

        out.writeObject("test");
        byte[] objectData = baos.toByteArray();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(objectData));
        in.readObject();

        out.writeObject(new byte[0]);
        objectData = baos.toByteArray();
        in = new ObjectInputStream(new ByteArrayInputStream(objectData));
        in.readObject();
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("key1", "value1");
        map1.put("key2", "value2");

        Map<String, String> map2 = new HashMap<>();
        map2.put("key3", "value3");
        map2.put("key4", "value4");

        byte[] objectData1 = SerializationUtils.serialize(map1);
        byte[] objectData2 = SerializationUtils.serialize(map2);

        ByteArrayInputStream bais = new ByteArrayInputStream(objectData1);
        ObjectInputStream in = new ClassLoaderAwareObjectInputStream(bais, Map.class.getClassLoader());
        Map<String, String> deserialized1 = (Map<String, String>) in.readObject();
        assertEquals(map1, deserialized1);

        bais = new ByteArrayInputStream(objectData2);
        in = new ClassLoaderAwareObjectInputStream(bais, Object.class.getClassLoader());
        Map<String, String> deserialized2 = (Map<String, String>) in.readObject();
        assertEquals(map2, deserialized2);
    }
}