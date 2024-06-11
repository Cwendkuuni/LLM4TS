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

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
        Assert.assertNotSame(original, cloned);
    }

    @Test
    public void testSerializeDeserializeString() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputstream() {
        SerializationUtils.serialize(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeThrowsException() {
        Exception exception = new SerializationException("Test Exception");
        try {
            SerializationUtils.serialize(exception);
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeClassNotFoundException() {
        SerializationUtils.deserialize(new byte[1]);
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(new Object(), outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object object = SerializationUtils.deserialize(inputStream);
        inputStream.close();
        outputStream.close();
        try {
            SerializationUtils.deserialize(inputStream);
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        }
    }

    @Test
    public void testSerializeBigObject() {
        Map<String, Serializable> map = new HashMap<>();
        for (int i = 0; i <= 10000; i++) {
            map.put("Key " + i, i);
        }
        byte[] serialized = SerializationUtils.serialize(map);
        Map<String, Serializable> deserialized = (Map<String, Serializable>) SerializationUtils.deserialize(serialized);
        Assert.assertEquals(map, deserialized);
    }

    @Test
    public void testSerializeSerializableArray() {
        Serializable[] serializableArray = new Serializable[5];
        for (int i = 0; i < serializableArray.length; i++) {
            serializableArray[i] = Integer.valueOf(i);
        }
        byte[] serialized = SerializationUtils.serialize(serializableArray);
        Serializable[] deserialized = (Serializable[]) SerializationUtils.deserialize(serialized);
        Assert.assertArrayEquals(serializableArray, deserialized);
    }

    @Test
    public void testSerializeCustomClassLoader() {
        TestClass testClass = new TestClass();
        byte[] serialized = SerializationUtils.serialize(testClass);
        Object deserialized = SerializationUtils.deserialize(serialized);
        Assert.assertEquals(testClass.toString(), deserialized.toString());
    }

}

class TestClass implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "TestClass";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}