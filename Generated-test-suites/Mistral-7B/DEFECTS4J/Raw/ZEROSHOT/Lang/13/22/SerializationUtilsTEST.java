package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.Serializable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.SerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.SerializationUtils.clone;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class SerializationUtilsTest {

    private static final byte[] SERIALIZED_BYTE_ARRAY = new byte[] { 3, 2, 1, 0, 1, 2, 3 };
    private static final String SERIALIZED_STRING = "Hello, World!";
    private static final Map<String, Integer> SERIALIZED_MAP = new HashMap<>();

    static {
        SERIALIZED_MAP.put("One", 1);
        SERIALIZED_MAP.put("Two", 2);
        SERIALIZED_MAP.put("Three", 3);
    }

    @Parameterized.Parameters(name = "{index}: Testing serialization of {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Integer(1)},
                {new Double(1.0)},
                {new String("Hello, World!")},
                {new ArrayList<>(Arrays.asList(1, 2, 3))},
                {SERIALIZED_MAP},
                {new MySerializableClass()}
        });
    }

    private Serializable object;

    public SerializationUtilsTest(Serializable object) {
        this.object = object;
    }

    @Test
    public void testClone() throws SerializationException {
        Object cloned = clone(object);
        assertEquals(object, cloned);
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithNullInput() {
        SerializationUtils.clone(null);
    }

    @Test
    public void testSerializeDeserializeWithByteArray() throws SerializationException, IOException {
        byte[] serialized = serialize(object);
        Object deserialized = deserialize(new ByteArrayInputStream(serialized));
        assertEquals(object, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeWithNullInputStream() {
        deserialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeWithIOException() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(SERIALIZED_BYTE_ARRAY);
        byteArrayInputStream.close();
        deserialize(byteArrayInputStream);
    }

    @Test
    public void testSerializeDeserializeWithSerializableException() throws SerializationException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(new Exception("Test Exception"));
        byteArrayOutputStream.close();
        byte[] serializedException = byteArrayOutputStream.toByteArray();
        Object deserializedException = deserialize(new ByteArrayInputStream(serializedException));
        assertTrue(deserializedException instanceof SerializableException);
    }

    @Test(expected = StreamCorruptedException.class)
    public void testSerializeDeserializeWithStreamCorruptedException() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeInt(10);
        byteArrayOutputStream.close();
        Object deserialized = deserialize(new ByteArrayInputStream(SERIALIZED_BYTE_ARRAY));
        assertFalse(deserialized instanceof Integer);
    }

    @Test
    public void testSerializeDeserializeWithString() throws SerializationException, IOException {
        byte[] serialized = serialize(SERIALIZED_STRING);
        String deserialized = (String) deserialize(new ByteArrayInputStream(serialized));
        assertEquals(SERIALIZED_STRING, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeWithNullString() {
        deserialize(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testSerializeDeserializeWithMap() throws SerializationException, IOException {
        byte[] serialized = serialize(SERIALIZED_MAP);
        Map<String, Integer> deserialized = (Map<String, Integer>) deserialize
                (new ByteArrayInputStream(serialized));
        assertEquals(SERIALIZED_MAP, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeWithNullMap() {
        deserialize(new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testSerializeDeserializeWithMySerializableClass() throws SerializationException, IOException {
        byte[] serializedMySerializableClass = serialize(new MySerializableClass());
        MySerializableClass deserializedMySerializableClass = (MySerializableClass) deserialize
                (new ByteArrayInputStream(serializedMySerializableClass));
        assertEquals(deserializedMySerializableClass.value, 42);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeDeserializeWithNullMySerializableClass() {
        deserialize(new ByteArrayInputStream(new byte[0]));
    }

    private static class MySerializableClass implements Serializable {
        private int value = 42;

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.writeInt(value);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            value = in.readInt();
        }
    }
}