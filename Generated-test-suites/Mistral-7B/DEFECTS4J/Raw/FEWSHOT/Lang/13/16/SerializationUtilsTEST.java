package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("foo", "bar");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        assertEquals(originalMap, clonedMap);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeAndDeserialize() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("foo", "bar");

        byte[] serializedData = SerializationUtils.serialize(originalMap);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedData);

        assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void testSerializeNullObject() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserializeNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(new IOException("Test Exception"));
        out.close();

        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(bos.toByteArray())));
    }

    @Test
    public void testDeserializeClassNotFoundException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(new Object() {
            private static final long serialVersionUID = 1L;
        }.getClass());
        out.close();

        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(bos.toByteArray())));
    }

    @Test
    public void testSerializeManagingFinally() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        try {
            out.writeObject(new ObjectWithFinallyBlock());
        } finally {
            out.close();
        }

        byte[] serializedData = bos.toByteArray();
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertEquals(new ObjectWithFinallyBlock(), deserializedObject);
    }

    @Test
    public void testSerializeIOException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        try {
            out.writeObject(new IOException("Test Exception"));
        } finally {
            out.close();
        }

        byte[] serializedData = bos.toByteArray();
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertNull(deserializedObject);
    }

    @Test
    public void testDeserializeManagingFinally() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        try {
            out.writeObject(new ObjectWithFinallyBlockDeserializer());
        } finally {
            out.close();
        }

        byte[] serializedData = bos.toByteArray();
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertEquals(new ObjectWithFinallyBlockDeserializer(), deserializedObject);
    }

    @Test
    public void testDeserializeIOException() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);

        try {
            Object obj = new Object() {
                private static final long serialVersionUID = 1L;

                void readObject(ObjectInputStream in) throws IOException {
                    throw new IOException("Test Exception");
                }
            };
            out.writeObject(obj);
        } finally {
            out.close();
        }

        byte[] serializedData = bos.toByteArray();
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertNull(deserializedObject);
    }

}

class ObjectWithFinallyBlock implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject("Test String");
    }

    @Override
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new IOException("Test IOException");
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("ObjectWithFinallyBlock finalized");
    }
}

class ObjectWithFinallyBlockDeserializer implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject("Test String");
    }

    @Override
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new IOException("Test IOException");
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("ObjectWithFinallyBlockDeserializer finalized");
    }
}