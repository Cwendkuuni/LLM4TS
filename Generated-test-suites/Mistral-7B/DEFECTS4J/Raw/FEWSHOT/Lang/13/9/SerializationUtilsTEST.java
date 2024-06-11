package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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

    @SuppressWarnings("unchecked")
    private static <T> void assertEqualsSerializable(T expected, T actual) {
        byte[] expectedBytes = SerializationUtils.serialize(expected);
        byte[] actualBytes = SerializationUtils.serialize(actual);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testCloneSerializableObject() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertEqualsSerializable(original, cloned);
    }

    @Test
    public void testCloneNonSerializableObject() {
        Object original = new Object() {
            // this class is not serializable
        };

        try {
            Object cloned = SerializationUtils.clone(original);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(100));
    }

    @Test
    public void testSerializeAndDeserializeSerializableObject() {
        Object original = new HashMap<>();
        Object deserialized = SerializationUtils.deserialize(SerializationUtils.serialize(original));
        assertEqualsSerializable(original, deserialized);
    }

    @Test
    public void testDeserializeNullInputStream() {
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeEmptyInputStream() {
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            fail("Expected EOFException");
        } catch (EOFException e) {
            // expected
        }
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("invalid bytes".getBytes());
        try {
            SerializationUtils.deserialize(inputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test(expected = StreamCorruptedException.class)
    public void testDeserializeStreamCorruptedException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeInt(12345); // write non-serializable integer
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            SerializationUtils.deserialize(inputStream);
            fail("Expected StreamCorruptedException");
        } catch (StreamCorruptedException e) {
            // expected
        }
    }

    @Test
    public void testSerializeManagingFinallyAndIOException() {
        try {
            Object obj = new HashMap<>();
            byte[] objectData = SerializationUtils.serialize(obj);
            fail("Expected no exception");
        } catch (SerializationException e) {
            // expected
        } finally {
            // the stream should be closed here
        }
    }

    @Test
    public void testDeserializeManagingFinallyAndIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        Object obj = new HashMap<>();
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        byte[] objectData = objectOutputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectData);
        try {
            Object deserialized = SerializationUtils.deserialize(inputStream);
            assertEqualsSerializable(obj, deserialized);
            fail("Expected no exception");
        } catch (SerializationException e) {
            // expected
        } finally {
            // the stream should be closed here
        }
    }

}