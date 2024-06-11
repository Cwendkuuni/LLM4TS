package org.apache.commons.lang3.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream;

import org.junit.Test;

/**
 * Tests for {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    /**
     * Test for clone method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    /**
     * Test for clone method with non-Serializable input.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCloneNonSerializableInput() {
        SerializationUtils.clone(new Object() {});
    }

    /**
     * Test for clone method with Serializable input.
     */
    @Test
    public void testCloneSerializableInput() {
        String original = "original";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    /**
     * Test for serialize method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    /**
     * Test for serialize method with non-Serializable input.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSerializeNonSerializableInput() {
        SerializationUtils.serialize(new Object() {}, new ByteArrayOutputStream());
    }

    /**
     * Test for serialize method with Serializable input.
     */
    @Test
    public void testSerializeSerializableInput() {
        String input = "input";
        byte[] serialized = SerializationUtils.serialize(input);
        assertEquals(serialized.length, 2 * input.length());
    }

    /**
     * Test for serialize method with InputStream and null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInputStreamInput() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    /**
     * Test for serialize method with InputStream and non-Serializable input.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSerializeNonSerializableInputStreamInput() {
        SerializationUtils.serialize(new Object() {}.getClass().getDeclaredConstructor().newInstance(), new ByteArrayOutputStream());
    }

    /**
     * Test for serialize method with InputStream and Serializable input.
     */
    @Test
    public void testSerializeSerializableInputStreamInput() {
        String input = "input";
        OutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(input, outputStream);
        byte[] serialized = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(serialized.length, 2 * input.length());
    }

    /**
     * Test for deserialize method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test for deserialize method with empty byte array.
     */
    @Test
    public void testDeserializeEmptyByteArray() {
        assertNull(SerializationUtils.deserialize(new byte[0]));
    }

    /**
     * Test for deserialize method with null class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullClass() {
        SerializationUtils.deserialize(new byte[0], null);
    }

    /**
     * Test for deserialize method with class that is not assignable from the deserialized object.
     */
    @Test(expected = ClassCastException.class)
    public void testDeserializeMismatchedClass() {
        byte[] serializedObject = SerializationUtils.serialize("serializedObject".getClass());
        SerializationUtils.deserialize(serializedObject, String[].class);
    }

    /**
     * Test for deserialize method with Serializable class.
     */
    @Test
    public void testDeserializeSerializableClass() {
        String input = "input";
        byte[] serialized = SerializationUtils.serialize(input);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(input, deserialized);
    }

    /**
     * Test for deserialize method with custom ClassLoader.
     */
    @Test
    public void testDeserializeCustomClassLoader() {
        Map<String, Object> objects = new HashMap<>();
        objects.put("String", "String");
        objects.put("Integer", 1);

        SerializationUtils.serialize(objects, new ByteArrayOutputStream());

        byte[] serialized = ((ByteArrayOutputStream) new ByteArrayOutputStream()).toByteArray();
        Object input = SerializationUtils.deserialize(serialized, new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serialized), Thread.currentThread().getContextClassLoader()));
        assertEquals(objects.getClass(), input.getClass());
        assertEquals(objects, input);
    }

}