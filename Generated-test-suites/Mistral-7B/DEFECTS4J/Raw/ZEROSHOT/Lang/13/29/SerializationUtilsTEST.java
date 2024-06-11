package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.serialization.ClassLoaderAwareObjectInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Test file for SerializationUtils class.
 */
public class SerializationUtilsTest {

    @Test
    public void testClone() throws SerializationException, IOException {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(original);

        Assert.assertNotNull(cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test
    public void testSerializeAndDeserialize() throws SerializationException, IOException {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        byte[] serializedData = SerializationUtils.serialize(original);
        Map<String, Integer> deserializedData = (Map<String, Integer>) SerializationUtils.deserialize(serializedData);

        Assert.assertNotNull(deserializedData);
        Assert.assertEquals(original, deserializedData);
    }

    @Test
    public void testSerializeAndDeserializeStream() throws SerializationException, IOException {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);

        OutputStream outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(original, outputStream);

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Map<String, Integer> deserializedData = (Map<String, Integer>) SerializationUtils.deserialize(inputStream);

        Assert.assertNotNull(deserializedData);
        Assert.assertEquals(original, deserializedData);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws SerializationException, IOException {
        String objectToSerialize = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(objectToSerialize, baos);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializationException() throws SerializationException, IOException {
        byte[] objectData = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        SerializationUtils.deserialize(bais);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws SerializationException, IOException, ClassNotFoundException {
        SerializationUtils.clone(new TestSerializableClass());
    }

    static class TestSerializableClass implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}