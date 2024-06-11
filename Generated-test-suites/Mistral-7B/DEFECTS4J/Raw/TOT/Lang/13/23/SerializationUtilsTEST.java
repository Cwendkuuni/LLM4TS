package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test cases for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    /**
     * Test cloning using {@link SerializationUtils#clone(Object)}.
     */
    @Test
    public void testClone() {
        // Test cloning an simple object
        Object original = new Object() {
        };
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original.getClass(), cloned.getClass());

        // Test cloning a complex object
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        Object complex = map;
        Object clonedComplex = SerializationUtils.clone(complex);
        Assert.assertNotSame(complex, clonedComplex);
        Assert.assertEquals(complex.getClass(), clonedComplex.getClass());
        Assert.assertEquals(map, (Map<?, ?>) clonedComplex);

        // Test cloning a null object
        Object nullObject = null;
        Object clonedNullObject = SerializationUtils.clone(nullObject);
        Assert.assertNull(clonedNullObject);

        // Test cloning an unserializable object
        final NonSerializable nonSerializable = new NonSerializable();
        try {
            SerializationUtils.clone(nonSerializable);
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test serializing and deserializing an object using {@link SerializationUtils#serialize(Object, OutputStream)}
     * and {@link SerializationUtils#deserialize(InputStream)}.
     */
    @Test
    public void testSerializationAndDeserialization() throws IOException, ClassNotFoundException {
        Object original = new Object() {
        };
        byte[] serialized = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertNotSame(original, deserialized);
        Assert.assertEquals(original.getClass(), deserialized.getClass());

        // Test serializing and deserializing a complex object
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        byte[] serializedComplex = SerializationUtils.serialize(map);
        Object deserializedComplex = SerializationUtils.deserialize(new ByteArrayInputStream(serializedComplex));
        Assert.assertNotSame(map, deserializedComplex);
        Assert.assertEquals(map.getClass(), deserializedComplex.getClass());
        Assert.assertEquals(map, (Map<?, ?>) deserializedComplex);

        // Test serializing and deserializing a null object
        Object nullObject = null;
        byte[] serializedNullObject = SerializationUtils.serialize(nullObject);
        Object deserializedNullObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedNullObject));
        Assert.assertNull(deserializedNullObject);

        // Test serializing and deserializing an unserializable object
        final NonSerializable nonSerializable = new NonSerializable();
        byte[] serializedNonSerializable = SerializationUtils.serialize(nonSerializable);
        Object deserializedNonSerializable = SerializationUtils.deserialize(new ByteArrayInputStream(serializedNonSerializable));
        Assert.assertSame(nonSerializable, deserializedNonSerializable);
    }

    /**
     * Test serializing and deserializing an object with custom ClassLoader.
     */
    @Test
    public void testSerializationAndDeserializationWithCustomClassLoader()
            throws IOException, ClassNotFoundException {
        final CustomSerializable customSerializable = new CustomSerializable();

        byte[] serialized = SerializationUtils.serialize(customSerializable);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        ObjectInputStream ois = new ClassLoaderAwareObjectInputStream(bais, getClass().getClassLoader());
        Object deserialized = ois.readObject();
        Object expected = new CustomSerializable();
        Assert.assertEquals(expected.getClass(), deserialized.getClass());
        Assert.assertEquals(customSerializable, expected);
    }

    /**
     * Test handling exceptions during serialization and deserialization.
     */
    @Test
    public void testExceptions() {
        try {
            SerializationUtils.serialize(new IOException("IOException"), new ByteArrayOutputStream());
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[]{-1}));
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }

        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]), new NonSerializableClassLoader());
            Assert.fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Simple non-serializable object.
     */
    private static class NonSerializable implements Serializable {
    }

    /**
     * Custom ClassLoader that throws an exception for a specific class.
     */
    private static class NonSerializableClassLoader extends ClassLoader {
        @Override
        public Class<?> findClass(String name) {
            if ("java/io/ObjectOutputStream".equals(name)) {
                throw new RuntimeException("Custom ClassLoader exception");
            }
            return super.findClass(name);
        }
    }

    /**
     * Custom serializable object.
     */
    private static class CustomSerializable implements Serializable {
    }
}