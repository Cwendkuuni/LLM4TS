package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for SerializationUtils
 */
public class SerializationUtilsTest {

    /**
     * Test method for {@link SerializationUtils#clone(Object)}
     */
    @Test
    public void testClone() {
        Map<String, String> original = new HashMap<>();
        original.put("key1", "value1");
        original.put("key2", "value2");

        Map<String, String> cloned = SerializationUtils.clone(original);

        // check if cloned map is not the same as original
        assertNotSame(original, cloned);

        // check if cloned map has the same entries as original
        for (Map.Entry<String, String> entry : original.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String clonedValue = cloned.get(key);

            assertEquals("expected same value for key " + key, value, clonedValue);
        }

        // check if cloned map can be modified independently from original
        cloned.put("key3", "value3");
        assertNotSame(original, cloned);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}
     */
    @Test
    public void testSerialize() {
        String objToSerialize = "String object to serialize";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(objToSerialize, outputStream);

        byte[] serializedData = outputStream.toByteArray();
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedData));

        assertEquals("expected same object after deserialization", objToSerialize, deserializedObject);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)}
     */
    @Test
    public void testDeserialize() {
        String objToSerialize = "String object to serialize";
        byte[] serializedData = SerializationUtils.serialize(objToSerialize);

        InputStream inputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);

        assertEquals("expected same object after deserialization", objToSerialize, deserializedObject);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])}
     */
    @Test
    public void testDeserializeByteArray() {
        String objToSerialize = "String object to serialize";
        byte[] serializedData = SerializationUtils.serialize(objToSerialize);

        Object deserializedObject = SerializationUtils.deserialize(serializedData);

        assertEquals("expected same object after deserialization", objToSerialize, deserializedObject);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with null input stream
     */
    @Test(expected = NullPointerException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with null byte array
     */
    @Test(expected = NullPointerException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with empty input stream
     */
    @Test(expected = IOException.class)
    public void testDeserializeEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(emptyInputStream);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with empty byte array
     */
    @Test(expected = IOException.class)
    public void testDeserializeEmptyByteArray() {
        byte[] emptyByteArray = new byte[0];
        try {
            SerializationUtils.deserialize(emptyByteArray);
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)} with invalid byte data
     */
    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeInvalidByteData() throws IOException {
        byte[] invalidByteData = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xDE, (byte) 0xAD};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidByteData);
        SerializationUtils.deserialize(inputStream);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])} with invalid byte data
     */
    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeInvalidByteDataArray() {
        byte[] invalidByteData = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xDE, (byte) 0xAD};
        try {
            SerializationUtils.deserialize(invalidByteData);
        } catch (ClassNotFoundException e) {
            // expected
        }
    }
}