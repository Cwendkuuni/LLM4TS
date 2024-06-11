package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

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
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertNotNull(cloned);
        assertEquals(original, cloned);
        assertNotNull(cloned.getClass().getSuperclass());
        assertNotSame(original, cloned);
        assertNotSame(original.getClass(), cloned.getClass());
    }

    @Test
    public void testSerializeDeserializeByteArray() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(objectData);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeByteArrayWithClassLoader() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        ClassLoader classLoader = HashMap.class.getClassLoader();
        byte[] objectData = SerializationUtils.serialize(original);
        Object deserialized = SerializationUtils.deserialize(objectData, classLoader);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeObjectStream() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, baos);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Object deserialized = SerializationUtils.deserialize(inputStream);
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeObjectStreamWithClassLoader() throws IOException, ClassNotFoundException {
        Object original = new HashMap<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(original);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new SerializationUtils.ClassLoaderAwareObjectInputStream(inputStream, HashMap.class.getClassLoader());
        Object deserialized = in.readObject();
        assertEquals(original, deserialized);
    }

    @Test
    public void testSerializeDeserializeCustomClass() throws IOException, ClassNotFoundException {
        TestSerializable testSerializable = new TestSerializable();
        byte[] objectData = SerializationUtils.serialize(testSerializable);
        Object deserialized = SerializationUtils.deserialize(objectData);
        assertEquals(testSerializable, deserialized);
    }

    @Test
    public void testSerializeDeserializeCustomClassWithClassLoader() throws IOException, ClassNotFoundException {
        TestSerializable testSerializable = new TestSerializable();
        byte[] objectData = SerializationUtils.serialize(testSerializable);
        Object deserialized = SerializationUtils.deserialize(objectData, TestSerializable.class.getClassLoader());
        assertEquals(testSerializable, deserialized);
    }

}

class TestSerializable implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public TestSerializable() {
        name = "TestSerializable";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}