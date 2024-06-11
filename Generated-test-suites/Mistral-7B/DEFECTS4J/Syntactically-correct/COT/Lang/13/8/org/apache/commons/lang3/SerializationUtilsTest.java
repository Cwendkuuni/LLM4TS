package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.util.TestUtils;
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
 * Tests for SerializationUtils class
 */
public class SerializationUtilsTest {

    // Clone test cases

    /**
     * Test cloning an object using SerializationUtils.clone()
     */
    @Test
    public void testClone() {
        TestObject testObject = new TestObject();
        TestObject clonedObject = SerializationUtils.clone(testObject);

        Assert.assertNotSame(testObject, clonedObject);
        Assert.assertEquals(testObject, clonedObject);
    }

    /**
     * Test cloning a null object using SerializationUtils.clone()
     */
    @Test(expected = NullPointerException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    /**
     * Test cloning a non-serializable object using SerializationUtils.clone()
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCloneNonSerializableObject() {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        SerializationUtils.clone(nonSerializableObject);
    }

    // Serialize test cases

    /**
     * Test serializing an object using SerializationUtils.serialize()
     */
    @Test
    public void testSerialize() throws IOException {
        TestObject testObject = new TestObject();
        byte[] objectData = SerializationUtils.serialize(testObject);
        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(objectData);

        Assert.assertNotSame(testObject, deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);
    }

    /**
     * Test serializing a null object using SerializationUtils.serialize()
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    // Deserialize test cases

    /**
     * Test deserializing an object using SerializationUtils.deserialize()
     */
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        TestObject testObject = new TestObject();
        byte[] objectData = SerializationUtils.serialize(testObject);

        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(objectData);

        Assert.assertNotSame(testObject, deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);
    }

    /**
     * Test deserializing a null object using SerializationUtils.deserialize()
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullObject() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test deserializing an empty byte array using SerializationUtils.deserialize()
     */
    @Test(expected = IOException.class)
    public void testDeserializeEmptyByteArray() {
        SerializationUtils.deserialize(new byte[0]);
    }

    // Edge cases and exception handling test cases

    /**
     * Test deserializing an object with a custom classloader using SerializationUtils.deserialize()
     */
    @Test
    public void testDeserializeCustomClassLoader() throws IOException, ClassNotFoundException {
        TestCustomClass customClass = new TestCustomClass();
        byte[] objectData = SerializationUtils.serialize(customClass);

        ClassLoader classLoader = TestUtils.getTestClassLoader();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(objectData));
        in.defaultReadObject();
        Object readObject = in.readObject();

        Assert.assertEquals(customClass, readObject);
    }

    /**
     * Test deserializing an object with a custom ObjectStreamClass using SerializationUtils.deserialize()
     */
    @Test
    public void testDeserializeCustomObjectStreamClass() throws IOException, ClassNotFoundException {
        TestCustomObjectStreamClass customObjectStreamClass = new TestCustomObjectStreamClass();
        byte[] objectData = SerializationUtils.serialize(customObjectStreamClass);

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(objectData)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                return customObjectStreamClass;
            }
        };

        Object readObject = in.readObject();

        Assert.assertEquals(customObjectStreamClass, readObject);
    }

    // Nested classes

    public static class TestObject implements Serializable {
        int value = 42;
    }

    public static class NonSerializableObject {
    }

    public static class TestCustomClass implements Serializable {
    }

    public static class TestCustomObjectStreamClass
            extends ObjectStreamClass {
        public TestCustomObjectStreamClass() {
            super(TestCustomClass.class);
        }

        @Override
        public String getSerialForm() {
            return "TestCustomClass";
        }
    }

}