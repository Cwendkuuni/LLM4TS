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
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        assertNotNull(clonedMap);
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        String originalString = "original";
        String clonedString = SerializationUtils.clone(originalString);

        byte[] originalBytes = SerializationUtils.serialize(originalString);
        byte[] clonedBytes = SerializationUtils.serialize(clonedString);

        String deserializedOriginalString = (String) SerializationUtils.deserialize(new ByteArrayInputStream(originalBytes));
        String deserializedClonedString = (String) SerializationUtils.deserialize(new ByteArrayInputStream(clonedBytes));

        assertEquals(originalString, deserializedOriginalString);
        assertEquals(clonedString, deserializedClonedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test
    public void testDeserializeNullInput() {
        assertNull(SerializationUtils.deserialize(null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidInput() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        SerializationUtils.deserialize(bais);
    }

    @Test(expected = StreamCorruptedException.class)
    public void testDeserializeCorruptedInput() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4 });
        SerializationUtils.deserialize(bais);
        bais.mark(1);
        bais.read();
        SerializationUtils.deserialize(bais);
    }

    @Test
    public void testDeserializeSerializableObject() throws IOException, ClassNotFoundException {
        Serializable serializableObject = new SerializableObject();
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        assertEquals(serializableObject, deserializedObject);
    }

    static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}