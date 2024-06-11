package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("hello", "world");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        assertNotNull(clonedMap);
        assertEquals(originalMap.size(), clonedMap.size());
        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedKey = clonedMap.getKey(key);
            String clonedValue = clonedMap.get(key);
            assertEquals(key, clonedKey);
            assertEquals(value, clonedValue);
        }
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("hello", "world");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, bos);

        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(bos.toByteArray());

        assertNotNull(deserializedMap);
        assertEquals(originalMap.size(), deserializedMap.size());
        for (Map.Entry<String, String> entry : originalMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String deserializedKey = deserializedMap.getKey(key);
            String deserializedValue = deserializedMap.get(key);
            assertEquals(key, deserializedKey);
            assertEquals(value, deserializedValue);
        }
    }

    @Test
    public void testNullInput() {
        assertNull(SerializationUtils.deserialize(null));
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(new SerializationUtils(), bos);
    }

    @Test
    public void testCloneOfNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionOnClone() {
        Serializable serializable = new Object() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused") // used for serialVersionUID
            private void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Cannot serialize this object.");
            }
        };

        try {
            SerializationUtils.clone(serializable);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }
}