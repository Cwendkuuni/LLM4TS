package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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

    @Test
    public void testCloneSerializableObject() {
        SerializableSerializable serializableObject = new SerializableSerializable();
        Serializable clonedObject = SerializationUtils.clone(serializableObject);
        assertEquals(serializableObject, clonedObject);
    }

    @Test
    public void testSerializeAndDeserializeSerializableObject() {
        SerializableSerializable serializableObject = new SerializableSerializable();
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);
        Serializable deserializedObject = SerializationUtils.deserialize(serializedObject);
        assertEquals(serializableObject, deserializedObject);
    }

    @Test
    public void testSerializeAndDeserializeByteArray() {
        SerializableSerializable serializableObject = new SerializableSerializable();
        byte[] serializedObject = SerializationUtils.serialize(serializableObject);
        byte[] deserializedObject = SerializationUtils.deserialize(serializedObject);
        Serializable deserializedObject2 = SerializationUtils.deserialize(deserializedObject);
        assertEquals(serializableObject, deserializedObject2);
    }

    @Test
    public void testCloneNonSerializableObjectThrowsSerializationException() {
        NonSerializableObject nonSerializableObject = new NonSerializableObject();
        assertThrows(SerializationException.class, () -> SerializationUtils.clone(nonSerializableObject));
    }

    @Test
    public void testSerializeNullObjectThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserializeNullInputStreamThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeInputStreamWithInvalidDataThrowsStreamCorruptedException() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        assertThrows(StreamCorruptedException.class, () -> SerializationUtils.deserialize(bais));
    }

    @Test
    public void testCloneObjectWithHashMap() {
        ObjectWithHashMap objectWithHashMap = new ObjectWithHashMap();
        ObjectWithHashMap clonedObject = SerializationUtils.clone(objectWithHashMap);
        assertEquals(objectWithHashMap, clonedObject);
    }

    // Add more test cases as needed

    private static class SerializableSerializable implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    private static class NonSerializableObject {

    }

    private static class ObjectWithHashMap implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Map<String, String> map = new HashMap<>();

        public ObjectWithHashMap() {
            map.put("foo", "bar");
        }
    }
}