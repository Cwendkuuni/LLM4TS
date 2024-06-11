package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class SerializationUtilsTest {

    @Test
    public void testClone() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerializeDeserializeByteArray() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedMap = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = SerializationUtils.deserialize(serializedMap);
        assertEquals(originalMap, deserializedMap);
    }

    @Test
    public void testSerializeDeserializeStream() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalMap, byteArrayOutputStream);
        byte[] serializedMap = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedMap);
        Map<String, Integer> deserializedMap = SerializationUtils.deserialize(byteArrayInputStream);
        assertEquals(originalMap, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationException() throws IOException, ClassNotFoundException {
        String input = "Hello World!";
        byte[] serializedInput = SerializationUtils.serialize(input);
        SerializationUtils.deserialize(new ByteArrayInputStream(serializedInput), String.class);
    }

    @Test(expected = SerializationException.class)
    public void testSerializationExceptionInput() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(byteArrayInputStream);
    }
}