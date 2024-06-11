package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.Cloneable;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.serialization.ClassLoaderAwareObjectInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        Assert.assertNotSame(original, cloned);
        Assert.assertEquals(original, cloned);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(new Object(), null);
    }

    @Test
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        SerializationUtils.deserialize(inputStream);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(original);
        Object deserializedObject = SerializationUtils.deserialize(objectData);
        Assert.assertEquals(original, deserializedObject);
    }

    @Test
    public void testDeserializeClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(original);
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ClassLoader classLoader = getClass().getClassLoader();
        ObjectInputStream inputStream = new ClassLoaderAwareObjectInputStream(bais, classLoader);
        Object deserializedObject = inputStream.readObject();
        Assert.assertEquals(original, deserializedObject);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidObject() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object(), baos);
        byte[] objectData = baos.toByteArray();
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(objectData));
        inputStream.readObject(); // This line should throw SerializationException
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeClassNotFoundException() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(new Object(), baos);
        byte[] objectData = baos.toByteArray();
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(objectData));
        inputStream.readObject(); // This line should throw ClassNotFoundException
    }

    @Test
    public void testSerializeAndDeserializeCloneableObject() throws CloneNotSupportedException, IOException, ClassNotFoundException {
        Cloneable cloneableObject = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(cloneableObject);
        Object deserializedObject = SerializationUtils.deserialize(objectData);
        Assert.assertNotSame(cloneableObject, deserializedObject);
        Assert.assertEquals(cloneableObject, deserializedObject);
    }

}