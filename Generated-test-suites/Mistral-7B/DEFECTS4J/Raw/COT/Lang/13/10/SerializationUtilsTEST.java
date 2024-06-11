package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.test.TestSerializer;
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
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        Serializable object = map;
        Serializable clonedObject = SerializationUtils.clone(object);

        Assert.assertNotSame(object, clonedObject);
        Assert.assertEquals(map, (Map) clonedObject);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws IOException, ClassNotFoundException {
        TestSerializer obj = new TestSerializer("test");
        byte[] objectData = SerializationUtils.serialize(obj);
        TestSerializer deserializedObj = (TestSerializer) SerializationUtils.deserialize(objectData);

        Assert.assertEquals("test", deserializedObj.getName());
        Assert.assertEquals("test", obj.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeException() throws IOException {
        TestSerializer obj = new TestSerializer("test");
        OutputStream outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(obj, outputStream);
        outputStream.close();
        outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.deserialize(outputStream);
        throw new IOException();
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeException() throws IOException, ClassNotFoundException {
        byte[] objectData = SerializationUtils.serialize(new TestSerializer("test"));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(objectData);
        throw new IOException();
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        TestSerializer testSerializer = new TestSerializer("test");

        Serializable object = map;
        Serializable clonedObject = SerializationUtils.clone(object);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(testSerializer, outputStream);
        ObjectInputStream objectInputStream = new SerializationUtils.ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()), getClass().getClassLoader());

        Serializable deserializedObject = objectInputStream.readObject();
        Assert.assertEquals(testSerializer.getClass(), deserializedObject.getClass());
    }

}

class TestSerializer implements Serializable {
    private String name;

    public TestSerializer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}