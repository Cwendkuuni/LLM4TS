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
 * Test class for SerializationUtils.
 */
public class SerializationUtilsTest {

    /**
     * Test method for {@link SerializationUtils#clone(Object)}.
     */
    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertNotSame(originalMap, clonedMap);
        Assert.assertEquals(originalMap, clonedMap);

        TestUtils.testDeepClone(originalMap, clonedMap);
    }

    /**
     * Test method for {@link SerializationUtils#serialize(Object, OutputStream)}.
     */
    @Test
    public void testSerializeObjectToOutputStream() {
        Object obj = new SerializableObject();
        byte[] serializedData;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SerializationUtils.serialize(obj, baos);
            serializedData = baos.toByteArray();
        } catch (IOException e) {
            Assert.fail("Failed during serialization");
        }

        Assert.assertNotNull(serializedData);
        Assert.assertFalse(serializedData.length == 0);
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(InputStream)}.
     */
    @Test
    public void testDeserializeObjectFromInputStream() {
        SerializableObject objectToSerialize = new SerializableObject();
        byte[] serializedData = SerializationUtils.serialize(objectToSerialize);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedData)) {
            Object deserializedObject = SerializationUtils.deserialize(bais);
            Assert.assertEquals(objectToSerialize.getClass(), deserializedObject.getClass());
            Assert.assertEquals(objectToSerialize, deserializedObject);
        } catch (IOException e) {
            Assert.fail("Failed during deserialization");
        }
    }

    /**
     * Test method for {@link SerializationUtils#deserialize(byte[])}.
     */
    @Test
    public void testDeserializeObjectFromByteArray() {
        SerializableObject objectToSerialize = new SerializableObject();
        byte[] serializedData = SerializationUtils.serialize(objectToSerialize);

        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        Assert.assertEquals(objectToSerialize.getClass(), deserializedObject.getClass());
        Assert.assertEquals(objectToSerialize, deserializedObject);
    }

    /**
     * Test class for SerializableObject.
     */
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String testString = "test";

        @Override
        public String toString() {
            return "SerializableObject [testString=" + testString + "]";
        }
    }
}