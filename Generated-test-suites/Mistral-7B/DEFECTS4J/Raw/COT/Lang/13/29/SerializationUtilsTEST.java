package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SerializationUtilsTest {

    @Test
    public void testCloneNullInput() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testCloneSerializableObject() throws Exception {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(original);

        assertEquals(original, cloned);
    }

    @Test
    public void testSerializeNullInput() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
    }

    @Test
    public void testSerializeSerializableObject() throws Exception {
        Map<String, Integer> objectToSerialize = new HashMap<>();
        objectToSerialize.put("one", 1);
        objectToSerialize.put("two", 2);

        byte[] serializedObject = SerializationUtils.serialize(objectToSerialize);
        Map<String, Integer> deserializedObject = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));

        assertEquals(objectToSerialize, deserializedObject);
    }

    @Test
    public void testDeserializeNullInput() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeSerializableObject() throws Exception {
        Map<String, Integer> objectToSerialize = new HashMap<>();
        objectToSerialize.put("one", 1);
        objectToSerialize.put("two", 2);

        byte[] serializedObject = SerializationUtils.serialize(objectToSerialize);

        Map<String, Integer> deserializedObject = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));

        assertEquals(objectToSerialize, deserializedObject);
    }

    @Test
    public void testDeserializeIllegalObjectType() throws Exception {
        assertThrows(RuntimeException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testSerializableFinallyBlock() throws Exception {
        Object inputObject = new Object() {
            public void serialize() throws IOException {
                throw new IOException("Simulated IOException");
            }

            public void close() throws IOException {
                System.out.println("Closing stream");
            }
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        try {
            SerializationUtils.serialize(inputObject, outputStream);
        } finally {
            byte[] serializedData = outputStream.toByteArray();
            SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        }
    }

    @Test
    public void testIOExceptionDuringDeserialization() throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        OutputStream out = outputStream;
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        Object inputObject = new Object() {
            public void writeObject(ObjectOutputStream out) throws IOException {
                throw new IOException("Simulated IOException");
            }

            public void close() throws IOException {
                System.out.println("Closing stream");
            }
        };

        try {
            objOut.writeObject(inputObject);
            objOut.close();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            SerializationUtils.deserialize(inputStream);
        } catch (SerializationException e) {
            assertEquals("Simulated IOException", e.getCause().getMessage());
        }
    }
}