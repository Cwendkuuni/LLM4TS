package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.exception.SerializationException;
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

    @Test
    public void testClone() {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);

        Assert.assertEquals(originalMap, clonedMap);
        Assert.assertNotSame(originalMap, clonedMap);

        // Check that the cloned map can be modified without affecting the original
        clonedMap.put("three", 3);
        Assert.assertFalse(originalMap.containsKey("three"));
    }

    @Test
    public void testSerializeAndDeserializeMap() throws SerializationException, IOException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedMap = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(serializedMap);

        Assert.assertEquals(originalMap, deserializedMap);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidClass() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject("not a valid class");
        objStream.close();

        SerializationUtils.deserialize(new ByteArrayInputStream(byteStream.toByteArray()));
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeExceptionInFinally() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(new Exception("this is a test"));
        objStream.close();

        SerializationUtils.deserialize(new ByteArrayInputStream(byteStream.toByteArray()));
    }
}