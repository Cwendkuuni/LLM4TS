package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testCloneObject() {
        Object obj = new HashMap<>();
        Object cloned = SerializationUtils.clone(obj);
        assertNotNull(cloned);
        assertEquals(obj, cloned);
        assertNotSame(obj.hashCode(), cloned.hashCode());
    }

    @Test
    public void testCloneNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeDeserializeByteArray() {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(serialized);
        assertNotNull(deserialized);
        assertEquals(obj, deserialized);
    }

    @Test
    public void testSerializeDeserializeInputStream() {
        Object obj = new HashMap<>();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, bos);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        Object deserialized = SerializationUtils.deserialize(bis);
        assertNotNull(deserialized);
        assertEquals(obj, deserialized);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
        ClassLoader classLoader = getClass().getClassLoader();
        ObjectInputStream ois = new SerializationUtils.ClassLoaderAwareObjectInputStream(bis, classLoader);
        Object deserialized = ois.readObject();
        assertNotNull(deserialized);
        assertEquals(obj, deserialized);
    }

    @Test
    public void testClassNotFoundException() {
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
            fail("Should not reach this point.");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testIOException() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(new byte[0]);
            bis.close();
            SerializationUtils.deserialize(bis);
            fail("Should not reach this point.");
        } catch (IOException e) {
            // Expected
        }
    }
}