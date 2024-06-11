package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    private static final int TEST_DATA_SIZE = 10;
    private Serializable testObject;
    private byte[] serializedObject;
    private Map<Integer, String> map;

    @Before
    public void setUp() {
        testObject = new HashMap<Integer, String>() {{
            for (int i = 0; i < TEST_DATA_SIZE; i++) {
                put(i, "Test Value " + i);
            }
        }};

        serializedObject = SerializationUtils.serialize(testObject);
    }

    @After
    public void tearDown() {
        testObject = null;
        serializedObject = null;
    }

    @Test
    public void testClone() {
        Serializable clonedObject = SerializationUtils.clone(testObject);
        Assert.assertNotNull(clonedObject);
        Assert.assertEquals(testObject, clonedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerialize() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(testObject, bos);
        byte[] serializedTestObject = bos.toByteArray();
        Assert.assertNotNull(serializedTestObject);
        Assert.assertNotEquals(0, serializedTestObject.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void testDeserialize() {
        Map<Integer, String> deserializedObject = (Map<Integer, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        Assert.assertNotNull(deserializedObject);
        Assert.assertEquals(testObject, deserializedObject);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        SerializationUtils deserializer = new SerializationUtils();
        try {
            deserializer.deserialize(bais);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeserializeCustomClassLoader() {
        SerializationUtils serializationUtils = new SerializationUtils();
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals("java.lang.String")) {
                    return String.class;
                }
                throw new ClassNotFoundException();
            }
        };
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
        try (ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, classLoader)) {
            Object object = ois.readObject();
            Assert.assertNotNull(object);
            Assert.assertTrue(object instanceof Map);
            Map<Integer, String> deserializedObject = (Map<Integer, String>) object;
            Assert.assertEquals(testObject, deserializedObject);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}