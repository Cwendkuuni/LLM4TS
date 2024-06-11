package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
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

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("one", "one");
        originalMap.put("two", "two");

        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);

        clonedMap.put("three", "three");
        Assert.assertNotEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("one", "one");
        originalMap.put("two", "two");

        byte[] serializedData = SerializationUtils.serialize(originalMap);

        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedData);

        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void testSerializationException() {
        try {
            SerializationUtils.deserialize(null);
            Assert.fail("Should have thrown SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }
}