package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.testutils.SerializationTestUtils;
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

public class SerializationUtilsTest {

    /**
     * Test cloning an object using serialization.
     */
    @Test
    public void testClone() {
        Map<Integer, String> originalMap = new HashMap<>();
        originalMap.put(1, "one");
        originalMap.put(2, "two");

        Map<Integer, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals("Cloned map should be equal to original map.", originalMap, clonedMap);
        Assert.assertNotSame("Cloned map should not be the same instance as original map.", originalMap, clonedMap);

        // Test with null input
        try {
            SerializationUtils.clone(null);
            Assert.fail("Expected SerializationException.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test serialization and deserialization of a simple object.
     */
    @Test
    public void testSerializationAndDeserialization() {
        SerialTestObject originalObject = new SerialTestObject("Original Object");

        byte[] serializedObject = SerializationUtils.serialize(originalObject);
        SerialTestObject deserializedObject = (SerialTestObject) SerializationUtils.deserialize(serializedObject);

        Assert.assertEquals("Deserialized object should be equal to original object.", originalObject, deserializedObject);

        // Test with null input
        try {
            SerializationUtils.deserialize(null);
            Assert.fail("Expected SerializationException.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test serialization and deserialization of a complex object with nested objects.
     */
    @Test
    public void testSerializationAndDeserializationComplex() {
        ComplexSerialTestObject originalObject = new ComplexSerialTestObject("Original Complex Object", new SerialTestObject("Nested Object"));

        byte[] serializedObject = SerializationUtils.serialize(originalObject);
        ComplexSerialTestObject deserializedObject = (ComplexSerialTestObject) SerializationUtils.deserialize(serializedObject);

        Assert.assertEquals("Deserialized complex object should be equal to original complex object.", originalObject, deserializedObject);

        // Test with null input
        try {
            SerializationUtils.deserialize(null);
            Assert.fail("Expected SerializationException.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test serialization and deserialization with exceptions.
     */
    @Test
    public void testSerializationAndDeserializationWithExceptions() {
        try {
            SerialTestObject exceptionThrowingObject = new SerialTestObjectWithExceptions();
            byte[] serializedObject = SerializationUtils.serialize(exceptionThrowingObject);
            SerialTestObject deserializedObject = (SerialTestObject) SerializationUtils.deserialize(serializedObject);
            Assert.fail("Expected SerializationException.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test serialization and deserialization managing finally and IOException.
     */
    @Test
    public void testSerializationAndDeserializationManagingFinallyAndIOException() {
        Serializable serializableObject = new SerialTestObjectWithFinallyAndIOException();

        try {
            byte[] serializedObject = SerializationUtils.serialize(serializableObject);
            Object deserializedObject = SerializationUtils.deserialize(serializedObject);

            Assert.assertNotNull("Deserialized object should not be null.", deserializedObject);

        } catch (SerializationException e) {
            // Expected
        }
    }

    /**
     * Test cloning managing finally and IOException.
     */
    @Test
    public void testCloneManagingFinallyAndIOException() {
        try {
            Serializable serializableObject = new SerialTestObjectWithFinallyAndIOException();
            SerializationUtils.clone(serializableObject);
            Assert.fail("Expected SerializationException.");
        } catch (SerializationException e) {
            // Expected
        }
    }

    private static class SerialTestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public SerialTestObject() {
        }

        public SerialTestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "SerialTestObject{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class ComplexSerialTestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private SerialTestObject nestedObject;

        public ComplexSerialTestObject() {
        }

        public ComplexSerialTestObject(String name, SerialTestObject nestedObject) {
            this.name = name;
            this.nestedObject = nestedObject;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SerialTestObject getNestedObject() {
            return nestedObject;
        }

        public void setNestedObject(SerialTestObject nestedObject) {
            this.nestedObject = nestedObject;
        }

        @Override
        public String toString() {
            return "ComplexSerialTestObject{" +
                    "name='" + name + '\'' +
                    ", nestedObject=" + nestedObject +
                    '}';
        }
    }

    private static class SerialTestObjectWithExceptions implements Serializable, Throwable {
        private static final long serialVersionUID = 1L;

        @Override
        public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new IOException("Expected IOException during deserialization.");
        }

        @Override
        public void readObjectData(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new ClassNotFoundException("Expected ClassNotFoundException during deserialization.");
        }

        @Override
        public String toString() {
            return "SerialTestObjectWithExceptions{}";
        }
    }

    private static class SerialTestObjectWithFinallyAndIOException implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new IOException("Expected IOException during deserialization.");
        }

        @Override
        public void readObjectData(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new ClassNotFoundException("Expected ClassNotFoundException during deserialization.");
        }

        @Override
        public void finalize() throws Throwable {
            throw new IOException("Expected IOException during finalize.");
        }

        @Override
        public String toString() {
            return "SerialTestObjectWithFinallyAndIOException{}";
        }
    }
}