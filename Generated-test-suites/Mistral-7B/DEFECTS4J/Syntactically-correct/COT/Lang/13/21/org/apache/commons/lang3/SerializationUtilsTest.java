package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Test Clone method
    @Test
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertNotSame(original, cloned);
        assertEquals(original, cloned);
    }

    // Test clone with null input
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    // Test serialize method
    @Test
    public void testSerialize() {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertNotSame(obj, deserialized);
        assertEquals(obj, deserialized);
    }

    // Test serialize with null input
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    // Test deserialize method
    @Test
    public void testDeserialize() {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertNotSame(obj, deserialized);
        assertEquals(obj, deserialized);
    }

    // Test deserialize with null input
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    // Test deserialize with custom ClassLoader
    @Test
    public void testDeserializeCustomClassLoader() throws Exception {
        Object obj = new ClassA();
        byte[] serialized = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, ClassLoaderTest.class.getClassLoader());
        Object deserialized = ois.readObject();
        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj, deserialized);
    }

    // Test deserialize with custom ClassLoader and different class name
    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeCustomClassLoaderDifferentClassName() throws Exception {
        Object obj = new ClassA();
        byte[] serialized = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, ClassLoaderTest.class.getClassLoader());
        ois.readObject(); // Deserialize successfully using the custom ClassLoader
        ois.readObject(); // Throw ClassNotFoundException using the custom ClassLoader
    }

    // Test exception handling for serialization and deserialization
    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
        } catch (IOException e) {
            throw new SerializationException("IOException while reading cloned object data", e);
        }
    }

    static class ClassA implements Serializable {
    }

    static class ClassLoaderTest {
    }

}