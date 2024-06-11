package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

    @SuppressWarnings("unchecked")
    @Test
    public void testClone() {
        Object original = new HashMap<>();
        Object cloned = SerializationUtils.clone(original);
        assertEquals(original.getClass(), cloned.getClass());
        assertEquals(original, cloned);

        // test null input
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerializeDeserialize() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("foo", "bar");

        byte[] serialized = SerializationUtils.serialize(map);
        Map<String, Object> deserialized = (Map<String, Object>) SerializationUtils.deserialize(new ByteArrayInputStream(serialized));
        assertEquals(map, deserialized);
    }

    @Test
    public void testSerializeDeserializeWithException() throws IOException, ClassNotFoundException {
        byte[] objectData = new byte[0];
        assertThrows(ClassNotFoundException.class, () -> SerializationUtils.deserialize(new ByteArrayInputStream(objectData)));
    }

    @Test
    public void testSerializeStreamClosing() throws IOException {
        Object obj = new Object() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        SerializationUtils.serialize(obj, baos);
        assertThrows(IOException.class, baos::close);
    }

    @Test
    public void testDeserializeStreamClosing() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        assertThrows(IOException.class, bais::close);
    }

    @SuppressWarnings("result")
    @Test
    public void testDeserializeStreamCorruptedException() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        bais.mark(1);
        bais.read();
        bais.reset();

        assertThrows(StreamCorruptedException.class, () -> SerializationUtils.deserialize(bais));
    }
}