package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for SerializationUtils.
 */
public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        // Test cloning a non-serializable object, should throw an exception
        try {
            SerializationUtils.clone(new Object() {});
            assert false;
        } catch (SerializationException e) {
            // expected
        }

        // Test cloning a serializable object
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        assertNotNull(clonedMap);
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException {
        String input = "Hello World";
        Serializable serializedObject = SerializationUtils.serialize(input);
        assertNotNull(serializedObject);

        String deserializedObject = (String) SerializationUtils.deserialize(new ByteArrayInputStream((byte[]) serializedObject));
        assertEquals(input, deserializedObject);
    }

    @Test
    public void testSerializeDeserializeObjectWithCustomClassLoader() throws IOException, ClassNotFoundException {
        Object serializableObject = new CustomSerializableObject();
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);

        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject), CustomClassLoader.class.getClassLoader());
        assertEquals(serializableObject, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationException() {
        byte[] serializedObject = new byte[10];
        SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
    }

    @Test
    public void testDeepCloneObjectWithNestedObjects() {
        NestedObject originalObject = new NestedObject("original");
        NestedObject clonedObject = (NestedObject) SerializationUtils.clone(originalObject);
        assertEquals(originalObject, clonedObject);
        assertNotNull(clonedObject.nestedObject);
        assertNotNull(clonedObject.nestedObject.name);
        assertEquals(originalObject.nestedObject.name, clonedObject.nestedObject.name);
    }

    static class CustomSerializableObject implements Serializable {
    }

    static class NestedObject implements Serializable {
        private String name;
        private NestedObject nestedObject;

        public NestedObject(String name) {
            this.name = name;
            this.nestedObject = new NestedObject("nested");
        }

        public String getName() {
            return name;
        }
    }

    static class CustomClassLoader extends ClassLoader {
        public CustomClassLoader() {
            super(ClassLoader.getSystemClassLoader());
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            return super.findClass(name + "_custom");
        }
    }
}