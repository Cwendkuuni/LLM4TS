package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertEquals(original.getClass(), cloned.getClass());
        assertEquals(original, cloned);

        // Test with null
        assertNull(SerializationUtils.clone(null));

        // Test with non-Serializable object
        assertThrows(RuntimeException.class, () -> SerializationUtils.clone(new Object() {
            // Non-Serializable class
        }));
    }

    @Test
    public void testSerializeDeserializeByteArray() {
        Serializable serializableObject = new HashMap<>();
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);
        Object deserializedObject = SerializationUtils.deserialize(serializedObject);
        assertEquals(serializableObject, deserializedObject);
    }

    @Test
    public void testSerializeDeserializeInputStream() {
        Serializable serializableObject = new HashMap<>();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(serializableObject, byteArrayOutputStream);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertEquals(serializableObject, deserializedObject);
    }

    @Test
    public void testSerializeDeserializeNullInputStream() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testSerializeDeserializeNullObject() {
        assertNull(SerializationUtils.deserialize(null));
    }

    @Test
    public void testIOExceptionDuringSerialization() {
        byte[] objectData = new byte[1024];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(new HashMap<>()); // Write object to byte array output stream
            out.close(); // Closes the output stream
            Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
            assertNotNull(deserializedObject);
        } catch (IOException e) {
            // Swallow exception
        }
        assertThrows(RuntimeException.class, () -> {
            try {
                SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testUnexpectedEOFException() {
        byte[] objectData = new byte[1024];
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
            Object deserializedObject = SerializationUtils.deserialize(bais);
            assertNotNull(deserializedObject);
            bais.close(); // Closes the input stream
        } catch (IOException e) {
            // Swallow exception
        }
        assertThrows(StreamCorruptedException.class, () -> {
            try {
                SerializationUtils.deserialize(new ByteArrayInputStream(objectData));
            } catch (IOException e) {
                throw new StreamCorruptedException(e.getMessage());
            }
        });
    }
}