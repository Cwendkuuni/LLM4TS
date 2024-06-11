package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Object object = new Object() {
        };

        Object clonedObject = SerializationUtils.clone(object);
        assertNotSame(object, clonedObject);
        assertEquals(object.getClass(), clonedObject.getClass());

        // Test with Serializable object
        Serializable serializableObject = new HashMap<>();
        Object clonedSerializableObject = SerializationUtils.clone(serializableObject);
        assertNotSame(serializableObject, clonedSerializableObject);
        assertEquals(serializableObject.getClass(), clonedSerializableObject.getClass());
        assertEquals(serializableObject, clonedSerializableObject); // Objects are equal
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        String testObject = "test";
        byte[] serializedData = SerializationUtils.serialize(testObject);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));
        assertEquals(testObject, deserializedObject);

        // Test with custom Object that implements Serializable
        class TestObject implements Serializable {
            private static final long serialVersionUID = 1L;
            private String name;

            public TestObject(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }
        }

        TestObject testObjectToSerialize = new TestObject("Test Object");
        byte[] serializedTestObjectData = SerializationUtils.serialize(testObjectToSerialize);
        Object deserializedTestObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedTestObjectData));
        TestObject deserializedTestObjectInstance = (TestObject) deserializedTestObject;
        assertEquals(testObjectToSerialize.getName(), deserializedTestObjectInstance.getName());

        // Test with custom Serializable class that has no-argument constructor
        class CustomSerializableClass implements Serializable {
            private static final long serialVersionUID = 1L;
            private String name;

            public CustomSerializableClass(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            // No-argument constructor
            protected CustomSerializableClass() {
            }
        }

        CustomSerializableClass customSerializableObjectToSerialize = new CustomSerializableClass("Custom Serializable Object");
        byte[] serializedCustomSerializableObjectData = SerializationUtils.serialize(customSerializableObjectToSerialize);
        Object deserializedCustomSerializableObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedCustomSerializableObjectData));
        CustomSerializableClass deserializedCustomSerializableObjectInstance = (CustomSerializableClass) deserializedCustomSerializableObject;
        assertEquals(customSerializableObjectToSerialize.getName(), deserializedCustomSerializableObjectInstance.getName());
    }

    @Test
    public void testStreamClosing() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        SerializationUtils.serialize(new Object(), byteArrayOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Object deserializedObject = SerializationUtils.deserialize(byteArrayInputStream);

        // Verify that the byteArrayOutputStream is closed
        assertNull(byteArrayOutputStream.size());

        // Verify that the byteArrayInputStream is closed
        assertNull(byteArrayInputStream.available());
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        // Test serializing and deserializing an object in different classloaders
        ClassLoader classLoader1 = this.getClass().getClassLoader();
        ClassLoader classLoader2 = new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    return Class.forName(name, false, classLoader1);
                } catch (ClassNotFoundException e) {
                    return super.findClass(name);
                }
            }
        };

        class TestClass implements Serializable {
            private static final long serialVersionUID = 1L;
            private int value;

            public TestClass(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        TestClass testClass1 = new TestClass(1);
        byte[] serializedData = SerializationUtils.serialize(testClass1);

        ObjectInputStream objectInputStream1 = new ObjectInputStream(new ByteArrayInputStream(serializedData)) {
            @Override
            protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                return classLoader1.findLoadedClass(desc.getName());
            }
        };
        Object deserializedObject1 = objectInputStream1.readObject();
        assertSame(testClass1, deserializedObject1);

        Object deserializedObject2 = null;
        try {
            ObjectInputStream objectInputStream2 = new ObjectInputStream(new ByteArrayInputStream(serializedData)) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    return classLoader2.findLoadedClass(desc.getName());
                }
            };
            deserializedObject2 = objectInputStream2.readObject();
        } catch (ClassNotFoundException e) {
            // Expected as the TestClass is not loaded in the classLoader2
        }
        assertNull(deserializedObject2);
    }
}