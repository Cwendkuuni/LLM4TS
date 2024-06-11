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

    // Clone tests

    @Test
    public void testCloneNullInput() {
        SerializationUtils serializationUtils = new SerializationUtils();
        Assert.assertNull(serializationUtils.clone(null));
    }

    @Test
    public <T extends Serializable> void testCloneSerializableObject() {
        SerializationUtils serializationUtils = new SerializationUtils();
        String original = "Test String";
        String cloned = serializationUtils.clone(original);
        Assert.assertEquals(original, cloned);
    }

    @Test(expected = SerializationException.class)
    public <T extends Serializable> void testCloneNonSerializableObject() {
        SerializationUtils serializationUtils = new SerializationUtils();
        Object nonSerializable = new Object() {
            // mark as non-serializable
            private static final long serialVersionUID = 1L;
        };
        serializationUtils.clone(nonSerializable);
    }

    // Serialize tests

    @Test
    public void testSerializeNullInput() {
        SerializationUtils serializationUtils = new SerializationUtils();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertThrows(IllegalArgumentException.class, () -> serializationUtils.serialize(null, outputStream));
    }

    @Test
    public void testSerializeSerializableObject() throws IOException {
        SerializationUtils serializationUtils = new SerializationUtils();
        String original = "Test String";
        byte[] serialized = serializationUtils.serialize(original);
        String deserialized = (String) serializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNonSerializableObject() {
        SerializationUtils serializationUtils = new SerializationUtils();
        Object nonSerializable = new Object() {
            // mark as non-serializable
            private static final long serialVersionUID = 1L;
        };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializationUtils.serialize(nonSerializable, outputStream);
    }

    // Deserialize tests

    @Test
    public void testDeserializeNullInput() {
        SerializationUtils serializationUtils = new SerializationUtils();
        Assert.assertThrows(IllegalArgumentException.class, () -> serializationUtils.deserialize(null));
    }

    @Test
    public void testDeserializeSerializableObject() throws IOException, ClassNotFoundException {
        SerializationUtils serializationUtils = new SerializationUtils();
        String original = "Test String";
        byte[] serialized = serializationUtils.serialize(original);
        String deserialized = (String) serializationUtils.deserialize(new ByteArrayInputStream(serialized));
        Assert.assertEquals(original, deserialized);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeNonSerializableObject() {
        SerializationUtils serializationUtils = new SerializationUtils();
        Object nonSerializable = new Object() {
            // mark as non-serializable
            private static final long serialVersionUID = 1L;
        };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        serializationUtils.deserialize(inputStream);
    }

    // Edge cases and exceptions

    @Test
    public void testSerializeLargeObject() throws IOException {
        SerializationUtils serializationUtils = new SerializationUtils();
        Map<String, String> largeMap = new HashMap<>(100000);
        byte[] serialized = serializationUtils.serialize(largeMap);
        // check if the serialized byte[] is not empty
        Assert.assertNotEquals(0, serialized.length);
    }

    @Test
    public void testDeserializeLargeObject() throws IOException, ClassNotFoundException {
        SerializationUtils serializationUtils = new SerializationUtils();
        Map<String, String> largeMap = new HashMap<>();
        byte[] serialized = serializationUtils.serialize(largeMap);
        Map<String, String> deserialized = (Map<String, String>) serializationUtils.deserialize(new ByteArrayInputStream(serialized));
        // check if the deserialized map has the same size as the original one
        Assert.assertEquals(largeMap.size(), deserialized.size());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeRecursiveObject() throws IOException {
        class RecursiveObject implements Serializable {
            RecursiveObject field;

            public RecursiveObject() {
                this.field = new RecursiveObject();
            }
        }

        SerializationUtils serializationUtils = new SerializationUtils();
        RecursiveObject recursiveObject = new RecursiveObject();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Assert.assertThrows(SerializationException.class, () -> serializationUtils.serialize(recursiveObject, outputStream));
    }

    @Test(expected = StackOverflowError.class)
    public void testDeserializeRecursiveObject() throws IOException, ClassNotFoundException {
        class RecursiveObject implements Serializable {
            RecursiveObject field;

            public RecursiveObject() {
                this.field = this;
            }
        }

        SerializationUtils serializationUtils = new SerializationUtils();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializationUtils.serialize(new RecursiveObject()));
        Object deserialized = serializationUtils.deserialize(inputStream);
    }
}