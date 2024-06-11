import org.apache.commons.lang3.SerializationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    // Test clone method
    @Test
    public void testClone() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        Map<String, Integer> clonedMap = SerializationUtils.clone(map);

        Assert.assertEquals(map, clonedMap);
        Assert.assertNotSame(map, clonedMap);
    }

    // Test serialize method with valid object and output stream
    @Test
    public void testSerializeWithValidObjectAndOutputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        String obj = "Hello, World!";
        SerializationUtils.serialize(obj, oos);

        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        ObjectInputStream ois = new ObjectInputStream(bais);

        try {
            String deserializedObj = (String) ois.readObject();
            Assert.assertEquals(obj, deserializedObj);
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException should not be thrown");
        }
    }

    // Test serialize method with invalid output stream
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithInvalidOutputStream() {
        String obj = "Hello, World!";
        SerializationUtils.serialize(obj, null);
    }

    // Test serialize method with null object
    @Test
    public void testSerializeWithNullObject() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        SerializationUtils.serialize(null, oos);

        byte[] serializedObj = baos.toByteArray();
        Assert.assertEquals(0, serializedObj.length);
    }

    // Test serialize method that throws SerializationException
    @Test(expected = SerializationException.class)
    public void testSerializeWithSerializationException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeBoolean(true);  // Trying to serialize non-serializable object

        oos.close();
        baos.close();
    }

    // Test deserialize method with valid input stream
    @Test
    public void testDeserializeWithValidInputStream() throws IOException {
        String obj = "Hello, World!";
        byte[] serializedObj;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            SerializationUtils.serialize(obj, oos);
            serializedObj = baos.toByteArray();
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        Object deserializedObj = SerializationUtils.deserialize(bais);

        Assert.assertEquals(obj, deserializedObj);
    }

    // Test deserialize method with invalid input stream
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithInvalidInputStream() {
        SerializationUtils.deserialize(null);
    }

    // Test deserialize method with null objectData
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullObjectData() {
        SerializationUtils.deserialize(null);
    }

    // Test deserialize method that throws SerializationException
    @Test(expected = SerializationException.class)
    public void testDeserializeWithSerializationException() {
        byte[] objectData = new byte[]{1, 2, 3, 4};  // Invalid object data
        SerializationUtils.deserialize(objectData);
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method
    @Test
    public void testClassLoaderAwareObjectInputStreamResolveClass() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        // Serialize an object with a non-existent class
        oos.writeObject(new NonExistentClass());

        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, null);

        try {
            Object deserializedObj = ois.readObject();
            Assert.assertNotNull(deserializedObj);
            Assert.assertTrue(deserializedObj instanceof NonExistentClass);
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException should not be thrown");
        }
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method with ClassNotFoundException
    @Test(expected = ClassNotFoundException.class)
    public void testClassLoaderAwareObjectInputStreamResolveClassClassNotFoundException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        // Serialize an object with a non-existent class
        oos.writeObject(new NonExistentClass());

        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new InvalidClassLoader());

        ois.readObject();
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method with IOException
    @Test(expected = IOException.class)
    public void testClassLoaderAwareObjectInputStreamResolveClassIOException() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new InvalidClassLoader());

        ois.readObject();
    }

    // Test ClassLoaderAwareObjectInputStream constructor with valid input
    @Test
    public void testClassLoaderAwareObjectInputStreamConstructorWithValidInput() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, null);
    }

    // Test ClassLoaderAwareObjectInputStream constructor with null input
    @Test(expected = IllegalArgumentException.class)
    public void testClassLoaderAwareObjectInputStreamConstructorWithNullInput() {
        new SerializationUtils.ClassLoaderAwareObjectInputStream(null, null);
    }

    // Test ClassLoaderAwareObjectInputStream constructor with IOException
    @Test(expected = IOException.class)
    public void testClassLoaderAwareObjectInputStreamConstructorWithIOException() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new InvalidClassLoader());
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method with ClassNotFoundException and fallback to ClassLoaderAwareObjectInputStream resolveresolveClass method
    @Test
    public void testClassLoaderAwareObjectInputStreamResolveClassClassNotFoundExceptionWithFallback() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        // Serialize an object with a non-existent class
        oos.writeObject(new NonExistentClass());

        byte[] serializedObj = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObj);
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new InvalidClassLoaderWithFallback());

        try {
            Object deserializedObj = ois.readObject();
            Assert.assertNotNull(deserializedObj);
            Assert.assertTrue(deserializedObj instanceof NonExistentClass);
        } catch (ClassNotFoundException e) {
            Assert.fail("ClassNotFoundException should not be thrown");
        }
    }

    // Test ClassLoaderAwareObjectInputStream resolveClass method with IOException and fallback to ClassLoaderAwareObjectInputStream resolveresolveClass method
    @Test
    public void testClassLoaderAwareObjectInputStreamResolveClassIOExceptionWithFallback() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{});
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, new InvalidClassLoaderWithFallback());

        ois.readObject();
    }

    // Helper class for test cases
    private static class NonExistentClass implements Serializable {
        // Empty class
    }

    // Helper class for test cases
    private static class InvalidClassLoader extends ClassLoader {
        // Invalid class loader that throws ClassNotFoundException
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            throw new ClassNotFoundException();
        }
    }

    // Helper class for test cases
    private static class InvalidClassLoaderWithFallback extends ClassLoader {
        // Invalid class loader that throws IOException and fallback to ClassLoaderAwareObjectInputStream resolveresolveClass method
        @Override
        public Class<?> loadClass(String name) throws IOException {
            throw new IOException();
        }
    }

}