package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullInput() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        byte[] serializedObject = SerializationUtils.serialize(originalMap);
        Map<String, Integer> deserializedObject = (Map<String, Integer>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        assertEquals(originalMap, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInput() {
        byte[] serializedObject = SerializationUtils.serialize(null);
    }

    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(originalMap, baos);
        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        Map<String, Integer> deserializedObject = (Map<String, Integer>) SerializationUtils.deserialize(inputStream);
        assertEquals(originalMap, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInput() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        Map<String, Object> originalMap = new HashMap<>();
        originalMap.put("one", new MyClass());

        byte[] serializedObject = SerializationUtils.serialize(originalMap);
        InputStream inputStream = new ByteArrayInputStream(serializedObject);
        ClassLoader classLoader = this.getClass().getClassLoader();
        Object deserializedObject = SerializationUtils.deserialize(new ClassLoaderAwareObjectInputStream(inputStream, classLoader));
        assertEquals(originalMap.get("one"), deserializedObject);
    }

}

class MyClass implements Serializable {
}