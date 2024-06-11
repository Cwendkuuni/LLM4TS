package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SerializationUtilsTest {
    private ByteArrayOutputStream out;
    private ByteArrayInputStream in;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream(512);
        in = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }

    @Test
    public void testCloneObject() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "one");
        map.put("two", "two");

        Map<String, String> clonedMap = SerializationUtils.clone(map);
        assertNotEquals(map, clonedMap);
        assertEquals(map.keySet(), clonedMap.keySet());
        assertEquals(map.values(), clonedMap.values());
    }

    @Test
    public void testSerializeDeserializeObject() throws IOException, ClassNotFoundException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "one");
        map.put("two", "two");

        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));
        assertNotEquals(map, deserializedMap);
        assertEquals(map.keySet(), deserializedMap.keySet());
        assertEquals(map.values(), deserializedMap.values());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeObjectWithIOException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject("not a serializable object");
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeObjectWithClassNotFoundException() throws IOException, ClassNotFoundException {
        byte[] serializedMap = new byte[0];
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serializedMap));
        objIn.readObject();
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeObjectWithSerializationException() throws IOException, ClassNotFoundException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "one");
        map.put("two", "two");

        ByteArrayOutputStream out = new ByteArrayOutputStream(512);
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(map);
        objOut.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream objIn = new ObjectInputStream(in);
        Object readObject = objIn.readObject();
        objIn.close();

        throw new SerializationException("Thrown for testing");
    }

    @Test(expected = StreamCorruptedException.class)
    public void testDeserializeObjectWithStreamCorruptedException() throws IOException, ClassNotFoundException {
        byte[] serializedMap = SerializationUtils.serialize(new Object());
        SerializationUtils.deserialize(new ByteArrayInputStream(serializedMap));
        SerializationUtils.deserialize(new ByteArrayInputStream(new byte[] { 0x00 }));
    }
}