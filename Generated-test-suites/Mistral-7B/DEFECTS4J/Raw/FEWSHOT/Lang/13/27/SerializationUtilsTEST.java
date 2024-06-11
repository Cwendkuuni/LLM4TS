package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone_Object() {
        Object obj = new Object() {
            int value = 42;
        };

        Object cloned = SerializationUtils.clone(obj);
        assertEquals(obj.getClass(), cloned.getClass());
        assertEquals(obj.hashCode(), cloned.hashCode());
        assertEquals(obj.value, ((Object) cloned).value);
    }

    @Test
    public void testClone_Map() {
        Map<String, Integer> map = new HashMap<>();
        map.put("foo", 1);
        map.put("bar", 2);

        Map<String, Integer> cloned = SerializationUtils.clone(map);
        assertEquals(map.getClass(), cloned.getClass());
        assertEquals(map.hashCode(), cloned.hashCode());
        assertEquals(map.size(), cloned.size());
        assertEquals(map.get("foo"), cloned.get("foo"));
        assertEquals(map.get("bar"), cloned.get("bar"));
    }

    @Test
    public void testSerialize_Object() {
        Object obj = new Object() {
            int value = 42;
        };

        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized));

        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj.hashCode(), deserialized.hashCode());
        assertEquals(obj.value, ((Object) deserialized).value);
    }

    @Test
    public void testSerialize_ByteArray() {
        Object obj = new Object() {
            int value = 42;
        };

        byte[] serialized = SerializationUtils.serialize(obj);
        Object deserialized = SerializationUtils.deserialize(serialized);

        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj.hashCode(), deserialized.hashCode());
        assertEquals(obj.value, ((Object) deserialized).value);
    }

    @Test
    public void testSerialize_NullObject() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null));
    }

    @Test
    public void testDeserialize_InputStream() {
        Object obj = new Object() {
            int value = 42;
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Object deserialized = SerializationUtils.deserialize(inputStream);

        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj.hashCode(), deserialized.hashCode());
        assertEquals(obj.value, ((Object) deserialized).value);
    }

    @Test
    public void testDeserialize_ByteArray() {
        Object obj = new Object() {
            int value = 42;
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        byte[] serialized = outputStream.toByteArray();

        Object deserialized = SerializationUtils.deserialize(serialized);

        assertEquals(obj.getClass(), deserialized.getClass());
        assertEquals(obj.hashCode(), deserialized.hashCode());
        assertEquals(obj.value, ((Object) deserialized).value);
    }

    @Test
    public void testDeserialize_NullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserialize_NullByteArray() {
        assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
    }

    @Test
    public void testDeserialize_StreamCorruptedException() {
        byte[] serialized = new byte[0];

        assertThrows(StreamCorruptedException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(serialized)));
    }

}