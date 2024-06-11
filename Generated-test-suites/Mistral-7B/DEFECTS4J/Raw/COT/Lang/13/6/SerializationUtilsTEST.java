package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests for SerializationUtils class
 */
public class SerializationUtilsTest {

    private static byte[] testObjectData; // serialized data for test object
    private static Serializable testObject; // test object

    @BeforeClass
    public static void setupClass() {
        testObject = new TestSerializableClass();
        testObjectData = SerializationUtils.serialize(testObject);
    }

    /**
     * Test clone method with valid object
     */
    @Test
    public void testCloneValidObject() {
        TestSerializableClass clonedObject = SerializationUtils.clone(testObject);
        Assert.assertNotSame(testObject, clonedObject);
        Assert.assertEquals(testObject, clonedObject);
    }

    /**
     * Test clone method with null object
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    /**
     * Test serialize method with valid object and OutputStream
     */
    @Test
    public void testSerializeValidObjectOutputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        Assert.assertNotNull(serializedData);
        Assert.assertNotSame(testObjectData, serializedData);
    }

    /**
     * Test serialize method with null OutputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(testObject, null);
    }

    /**
     * Test deserialize method with valid byte array
     */
    @Test
    public void testDeserializeValidByteArray() {
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(testObjectData));
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);
    }

    /**
     * Test deserialize method with null InputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test ClassLoaderAwareObjectInputStream
     */
    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        byte[] mapData = SerializationUtils.serialize(map);
        ByteArrayInputStream classLoaderAwareObjectInputStream = new ByteArrayInputStream(mapData);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(classLoaderAwareObjectInputStream);
        Assert.assertEquals(map, deserializedMap);
    }

    /**
     * Test SerializationException handling
     */
    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        // any serialization method that throws a SerializationException
        SerializationUtils.serialize(new Object() {}, null);
    }

    /**
     * Test deserialization of a class with custom serialVersionUID
     */
    @Test
    public void testCustomSerialVersionUID() throws IOException, ClassNotFoundException {
        CustomSerializableClass customSerializableClass = new CustomSerializableClass(42);
        byte[] customSerializableClassData = SerializationUtils.serialize(customSerializableClass);
        ByteArrayInputStream customSerializableClassInputStream = new ByteArrayInputStream(customSerializableClassData);
        CustomSerializableClass deserializedCustomSerializableClass = (CustomSerializableClass) SerializationUtils.deserialize(customSerializableClassInputStream);
        Assert.assertEquals(42, deserializedCustomSerializableClass.serialVersionUID);
    }

    /**
     * Test cloning of a class with custom serialVersionUID
     */
    @Test
    public void testCustomSerialVersionUIDClone() {
        CustomSerializableClass clonedCustomSerializableClass = (CustomSerializableClass) SerializationUtils.clone(new CustomSerializableClass(42));
        Assert.assertEquals(42, clonedCustomSerializableClass.serialVersionUID);
    }

    /**
     * Test SerializationUtils constructor
     */
    @Test(expected = IllegalStateException.class)
    public void testSerializationUtilsConstructor() {
        new SerializationUtils();
    }

    /**
     * Test clone method with circular references
     */
    @Test(expected = StackOverflowError.class)
    public void testCloneCircularReferences() {
        CircularReference circularReference = new CircularReference();
        SerializationUtils.clone(circularReference);
    }

    private static class TestSerializableClass implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "TestSerializableClass";
        }
    }

    private static class CustomSerializableClass implements Serializable {
        private static final long serialVersionUID = 42L;
        private int serialVersionUID;

        public CustomSerializableClass(int serialVersionUID) {
            this.serialVersionUID = serialVersionUID;
        }
    }

    private static class CircularReference implements Serializable {
        private static final long serialVersionUID = 1L;
        private CircularReference reference;

        public CircularReference() {
            this.reference = this;
        }
    }
}