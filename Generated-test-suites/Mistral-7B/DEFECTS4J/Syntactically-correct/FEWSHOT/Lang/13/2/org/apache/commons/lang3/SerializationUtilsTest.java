package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        Object obj = new HashMap<>();
        HashMap<String, String> clonedObj = SerializationUtils.clone(obj);
        assertNotNull(clonedObj);
        assertEquals(obj, clonedObj);
    }

    @Test
    public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException {
        String testString = "TestString";
        Serializable obj = testString;
        byte[] serializedObject = SerializationUtils.serialize(obj);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        assertEquals(obj, deserializedObject);
        assertEquals(testString.getClass(), deserializedObject.getClass());
    }

    @Test
    public void testSerializeDeserializeArray() throws IOException, ClassNotFoundException {
        String testString = "TestString";
        Serializable[] objArray = { testString, testString };
        byte[] serializedObjectArray = SerializationUtils.serialize(objArray);
        Object deserializedObjectArray = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObjectArray));
        assertEquals(objArray.length, deserializedObjectArray.length);
        for (int i = 0; i < objArray.length; i++) {
            assertEquals(objArray[i], deserializedObjectArray[i]);
            assertEquals(testString.getClass(), deserializedObjectArray[i].getClass());
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws IOException, ClassNotFoundException {
        String testString = "TestString";
        Serializable obj = testString;
        byte[] serializedObject = SerializationUtils.serialize(obj);
        Object deserializedObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedObject));
        assertEquals(obj, deserializedObject);
        assertEquals(testString.getClass(), deserializedObject.getClass());

        String testStringInDifferentClassLoader = "TestStringInDifferentClassLoader";
        ClassLoader classLoader = getClass().getClassLoader();
        Serializable objInDifferentClassLoader = testStringInDifferentClassLoader;
        byte[] serializedObjectInDifferentClassLoader = SerializationUtils.serialize(objInDifferentClassLoader);
        Object deserializedObjectInDifferentClassLoader = new ClassLoaderAwareObjectInputStream(new ByteArrayInputStream(serializedObjectInDifferentClassLoader), classLoader).readObject();
        assertEquals(objInDifferentClassLoader, deserializedObjectInDifferentClassLoader);
        assertEquals(testStringInDifferentClassLoader.getClass(), deserializedObjectInDifferentClassLoader.getClass());
    }

    @Test
    public void testNullInput() {
        assertNull(SerializationUtils.clone(null));
        assertNull(SerializationUtils.deserialize(null));
    }

}