package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

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
    public void testClone() {
        Object original = new HashMap<>();
        HashMap<String, String> cloned = SerializationUtils.clone(original);
        assertEquals(original.getClass(), cloned.getClass());
        assertEquals(original.hashCode(), cloned.hashCode());
        assertNotSame(original, cloned);
    }

    @Test
    public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
        Serializable obj = new HashMap<>();
        byte[] serialized = SerializationUtils.serialize(obj);
        HashMap<String, String> deserialized = (HashMap<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(obj, deserialized);
    }

    @Test
    public void testSerializeAndDeserializeWithCustomClassLoader() throws IOException, ClassNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        byte[] serialized = SerializationUtils.serialize(new Object() {});
        Object deserialized = SerializationUtils.deserialize(new ByteArrayInputStream(serialized), classLoader);
        assertNotNull(deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullInputStream() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserializeNullObject() {
        assertNull(SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
    }

    @Test
    public void testDeserializeNonSerializableObject() {
        assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0])));
    }

}