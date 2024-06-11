package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "one");
        map.put("2", "two");

        HashMap<String, String> clonedMap = SerializationUtils.clone(map);
        assertEquals(map, clonedMap);

        clonedMap.put("3", "three");
        assertNotEquals(map, clonedMap);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws IOException, ClassNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "one");
        map.put("2", "two");

        byte[] serializedData = SerializationUtils.serialize(map);
        HashMap<String, String> deserializedMap = (HashMap<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertEquals(map, deserializedMap);
    }

    @Test
    public void testSerializeAndDeserializeObjectStream() throws IOException, ClassNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        map.put("1", "one");
        map.put("2", "two");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = byteArrayOutputStream;
        SerializationUtils.serialize(map, outputStream);
        outputStream.close();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        HashMap<String, String> deserializedMap = (HashMap<String, String>) SerializationUtils.deserialize(inputStream);

        assertEquals(map, deserializedMap);
    }

    @Test
    public void testSerializeNullObject() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testDeserializeNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }
}