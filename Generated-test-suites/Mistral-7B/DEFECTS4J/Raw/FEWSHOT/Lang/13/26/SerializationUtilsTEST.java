package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Object obj = new HashMap<>();
        @SuppressWarnings("serial")
        HashMap<String, String> clonedObj = (HashMap<String, String>) SerializationUtils.clone(obj);
        assertEquals(obj, clonedObj);
        assertNotNull(clonedObj);
    }

    @Test
    public void testCloneNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        String str = "Test";
        byte[] serialized = SerializationUtils.serialize(str);
        String deserialized = (String) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(str, deserialized);
    }

    @Test
    public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException {
        Object obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj, deserialized);
    }

    @Test
    public void testSerializeDeserializeArray() throws IOException, ClassNotFoundException {
        String[] strArray = {"Test1", "Test2"};
        byte[] serialized = SerializationUtils.serialize(strArray);
        String[] deserialized = (String[]) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertArrayEquals(strArray, deserialized);
    }

    @Test
    public void testDeserializeNullStream() {
        assertThrows(NullPointerException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeInvalidStream() {
        assertThrows(StreamCorruptedException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[] { 0x00 })));
    }

    @Test
    public void testDeserializeInvalidObject() throws IOException, ClassNotFoundException {
        byte[] serialized = SerializationUtils.serialize(new byte[0]);
        assertThrows(ClassNotFoundException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(serialized)));
    }

    @Test
    public void testDeserializeInvalidObject2() throws IOException, ClassNotFoundException {
        byte[] serialized = SerializationUtils.serialize(new String("Test"));
        assertThrows(ClassNotFoundException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[] { 0x01 })));
    }
}