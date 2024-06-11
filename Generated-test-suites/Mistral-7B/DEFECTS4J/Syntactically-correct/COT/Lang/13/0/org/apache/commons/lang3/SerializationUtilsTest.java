package org.apache.commons.lang3.test;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        Assert.assertEquals("Cloned String should be equal to original", original, cloned);
    }

    @Test
    public void testSerializeDeserializeString() {
        String original = "Hello World";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        Assert.assertEquals("Deserialized String should be equal to original", original, deserialized);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        SerializationUtils.serialize(null, new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        });
    }

    @Test(expected = SerializationException.class)
    public void testSerializeException() {
        String obj = new String("Hello World") {
            @Override
            public String toString() {
                throw new RuntimeException("Test Exception");
            }
        };

        try {
            SerializationUtils.serialize(obj, new ByteArrayOutputStream());
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        }
    }

    @Test
    public void testDeserializeInputStreamNull() {
        try {
            SerializationUtils.deserialize(null);
        } catch (IOException e) {
            // expected
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Unexpected ClassNotFoundException", e);
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDeserializeClassNotFoundException() {
        try {
            SerializationUtils.deserialize(new ByteArrayInputStream(new byte[0]));
        } catch (IOException e) {
            throw new SerializationException("Unexpected IOException", e);
        }
    }

    @Test
    public void testDeserializeHashMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("One", 1);
        original.put("Two", 2);
        original.put("Three", 3);

        byte[] serialized = SerializationUtils.serialize(original);
        Map<String, Integer> deserialized = (Map<String, Integer>) SerializationUtils.deserialize(serialized);

        Assert.assertEquals("Deserialized Map should be equal to original", original, deserialized);
    }

    @Test
    public void testDeserializeSerializableObject() {
        SerializableTestObject original = new SerializableTestObject(1);
        byte[] serialized = SerializationUtils.serialize(original);
        SerializableTestObject deserialized = (SerializableTestObject) SerializationUtils.deserialize(serialized);

        Assert.assertEquals("Deserialized Object should be equal to original", original, deserialized);
    }

    static class SerializableTestObject implements Serializable {
        private int value;

        public SerializableTestObject(int value) {
            this.value = value;
        }

        private Object writeReplace() throws ObjectStreamException {
            return new SerializableTestObject(this.value);
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            this.value = in.readInt();
        }
    }

}