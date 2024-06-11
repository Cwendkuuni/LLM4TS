package org.apache.commons.lang3;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() throws Exception {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);

        assertEquals(original, cloned);

        // Test modification of original does not affect cloned
        ((Map) original).put("newKey", "newValue");
        assertNotSame(original, cloned);
        assertEquals(original, original);
        assertEquals(cloned, cloned);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCloneNullObject() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserializeObject() throws Exception {
        Object original = new HashMap<>();
        byte[] objectData = SerializationUtils.serialize(original);
        Object cloned = SerializationUtils.deserialize(new ByteArrayInputStream(objectData));

        assertEquals(original, cloned);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test
    public void testSerializeDeserializeStream() throws Exception {
        Object original = new HashMap<>();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(original, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object cloned = SerializationUtils.deserialize(inputStream);

        assertEquals(original, cloned);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeDeserializeNullStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() throws Exception {
        Object testObject = new TestClass();

        byte[] objectData = SerializationUtils.serialize(testObject);
        ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
        ObjectInputStream input = new ObjectInputStream(bais);

        Object deserialized = input.readObject();
        assertEquals(testObject.getClass(), deserialized.getClass());

        input.close();
    }

    @Test(expected = StreamCorruptedException.class)
    public void testClassLoaderAwareObjectInputStreamInvalidData() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{0x00});
        ObjectInputStream input = new SerializationUtils.ClassLoaderAwareObjectInputStream(bais, null);

        input.readObject();
    }

    static class TestClass implements Serializable {

    }
}