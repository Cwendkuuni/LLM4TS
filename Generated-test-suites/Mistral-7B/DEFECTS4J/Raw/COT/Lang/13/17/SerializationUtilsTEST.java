package org.apache.commons.lang3.test;

import static org.junit.Assert.assertArrayEquals;
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
import org.apache.commons.lang3.test.SerializationTestUtils;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertArrayEquals(original.getBytes(), cloned.getBytes());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException {
        Serializable original = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertNotNull(deserialized);
        assertArrayEquals(SerializationTestUtils.serialize(original), deserialized);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSerializeDeserializeObjectArray() throws IOException, ClassNotFoundException {
        Serializable[] originals = new Serializable[] { new HashMap<>(), new HashMap<>() };
        byte[] serialized = SerializationUtils.serialize(originals);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertNotNull(deserialized);
        Serializable[] deserializedArray = (Serializable[]) deserialized;
        for (int i = 0; i < 2; i++) {
            Map<Object, Object> map = (Map<Object, Object>) deserializedArray[i];
            assertNotNull(map);
            assertArrayEquals(SerializationTestUtils.serialize(originals[i]), SerializationTestUtils.serialize(map));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize((InputStream) null);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Serializable serializable = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(serializable);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        Object deserialized = SerializationUtils.deserialize(new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader()));
        assertNotNull(deserialized);
        assertArrayEquals(SerializationTestUtils.serialize(serializable), SerializationTestUtils.serialize(deserialized));
    }

    @Test
    public void testClassLoaderAwareObjectInputStreamFailedClassLoader() throws IOException, ClassNotFoundException {
        Serializable serializable = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(serializable);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        Object deserialized = SerializationUtils.deserialize(new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new ClassLoader() {}));
        assertNotNull(deserialized);
        assertArrayEquals(SerializationTestUtils.serialize(serializable), SerializationTestUtils.serialize(deserialized));
    }

    private ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}