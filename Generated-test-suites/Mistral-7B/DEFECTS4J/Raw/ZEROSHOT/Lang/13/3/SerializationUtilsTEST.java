package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {

    private Serializable objectToSerialize;
    private Map<String, Serializable> mapToSerialize;

    @Before
    public void setUp() {
        objectToSerialize = new HashMap<>();
        mapToSerialize = new HashMap<>();
        mapToSerialize.put("key1", "value1");
        mapToSerialize.put("key2", "value2");
    }

    @After
    public void tearDown() {
        objectToSerialize = null;
        mapToSerialize = null;
    }

    @Test
    public void testClone() throws SerializationException, InstantiationException, IllegalAccessException, CloneNotSupportedException {
        Serializable clonedObject = SerializationUtils.clone(objectToSerialize);
        assertNotNull(clonedObject);
        assertEquals(objectToSerialize.getClass(), clonedObject.getClass());
    }

    @Test(expected = SerializationException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeAndDeserializeObject() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(objectToSerialize, outputStream);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(deserializedObject);
        assertEquals(objectToSerialize.getClass(), deserializedObject.getClass());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeAndDeserializeObjectWithIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        throw new IOException("IOException during serialization");
        // SerializationUtils.serialize(objectToSerialize, outputStream);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
    }

    @Test
    public void testSerializeAndDeserializeMap() throws SerializationException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(mapToSerialize, outputStream);
        Map<String, Serializable> deserializedMap = (Map<String, Serializable>) SerializationUtils.deserialize(new ByteArrayInputStream(outputStream.toByteArray()));
        assertNotNull(deserializedMap);
        assertEquals(mapToSerialize.size(), deserializedMap.size());

        Serializable value1 = deserializedMap.get("key1");
        assertNotNull(value1);
        assertEquals("value1", value1);

        Serializable value2 = deserializedMap.get("key2");
        assertNotNull(value2);
        assertEquals("value2", value2);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeAndDeserializeMapWithIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        throw new IOException("IOException during serialization");
        // SerializationUtils.serialize(mapToSerialize, outputStream);

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(mapToSerialize);
        objectOutputStream.close();

        byte[] serializedData = outputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = new ClassLoaderAwareObjectInputStream(byteArrayInputStream, getClass().getClassLoader());
        Object deserializedObject = classLoaderAwareObjectInputStream.readObject();
        assertNotNull(deserializedObject);
        assertEquals(mapToSerialize.getClass(), deserializedObject.getClass());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testClassLoaderAwareObjectInputStreamWithWrongClassLoader() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(mapToSerialize);
        objectOutputStream.close();

        byte[] serializedData = outputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ClassLoaderAwareObjectInputStream classLoaderAwareObjectInputStream = new ClassLoaderAwareObjectInputStream(byteArrayInputStream, SerializationUtils.class.getClassLoader());
        Object deserializedObject = classLoaderAwareObjectInputStream.readObject();
        assertNull(deserializedObject);
    }
}