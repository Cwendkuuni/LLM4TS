package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
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
        Object obj = new HashMap<>();
        HashMap<String, String> cloned = SerializationUtils.clone(obj);
        assertEquals(obj, cloned);
    }

    @Test
    public void testCloneNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeAndDeserializeObject() throws IOException, ClassNotFoundException {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(obj, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeObjectStream() throws IOException, ClassNotFoundException {
        Object obj = new HashMap<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, out);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(out.toByteArray()));
        assertEquals(obj, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNull() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNull() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        try {
            SerializationUtils.deserialize(inputStream);
        } finally {
            inputStream.close();
        }
        throw new IOException("Test IOException");
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeClassNotFoundException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(new Object() {});
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                throw new ClassNotFoundException("Test ClassNotFoundException");
            }
        };
        SerializationUtils.ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(inputStream, classLoader);
        classLoaderAwareObjectInputStream.readObject();
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeStreamCorruptedException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
        objectOutputStream.writeObject(new Object() {});
        objectOutputStream.writeInt(0); // Writing an int as the next object to the stream
        objectOutputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
        SerializationUtils.deserialize(inputStream);
    }
}