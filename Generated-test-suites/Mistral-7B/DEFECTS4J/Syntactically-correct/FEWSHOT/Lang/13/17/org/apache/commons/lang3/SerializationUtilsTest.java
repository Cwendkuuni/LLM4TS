package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
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

    @Test
    public void testClone() {
        Object obj = new Object() {
        };
        Object cloned = SerializationUtils.clone(obj);
        assertEquals(obj.getClass(), cloned.getClass());
        assertNull(cloned.getClass().getSuperclass());
        assertEquals(obj, cloned);

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        Object clonedMap = SerializationUtils.clone(map);
        assertEquals(map.getClass(), clonedMap.getClass());
        assertSame(map, clonedMap);
    }

    @Test
    public void testSerializeToOutputStream() {
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        String deserializeString = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serializeBytes));
        assertEquals(testString, deserializeString);
    }

    @Test
    public void testSerializeToByteArray() {
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        String deserializeString = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serializeBytes));
        assertEquals(testString, deserializeString);
    }

    @Test
    public void testDeserializeFromInputStream() {
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializeBytes);
        String deserializeString = (String) SerializationUtils.deserialize(bais);
        assertEquals(testString, deserializeString);
    }

    @Test
    public void testDeserializeFromByteArray() {
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        String deserializeString = (String) SerializationUtils.deserialize(serializeBytes);
        assertEquals(testString, deserializeString);
    }

    @Test
    public void testDeserializeInvalidClass() {
        byte[] serializeBytes = new byte[0];
        assertThrows(ClassNotFoundException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(serializeBytes)));
    }

    @Test
    public void testDeserializeIOException() {
        byte[] serializeBytes = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(serializeBytes);
        assertThrows(IOException.class, () -> SerializationUtils.deserialize(bais));
    }

    @Test
    public void testDeserializeStreamCorruptedException() {
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializeBytes);
        bais.mark(5);
        bais.read();
        assertThrows(StreamCorruptedException.class, () -> SerializationUtils.deserialize(bais));
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        // Testing the custom ClassLoaderAwareObjectInputStream class
        String testString = "testString";
        byte[] serializeBytes = SerializationUtils.serialize(testString);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializeBytes);

        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals("test.TestClass")) {
                    return new TestClass();
                }
                throw new ClassNotFoundException();
            }
        };

        SerializationUtils.ClassLoaderAwareObjectInputStream in = null;
        try {
            in = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, classLoader);
            Object readObject = in.readObject();
            assertSame(TestClass.class, readObject.getClass());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}