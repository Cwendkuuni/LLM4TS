package org.apache.commons.lang3;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test deep cloning of a Serializable object
        SerializableObject obj1 = new SerializableObject("Test", 123);
        SerializableObject obj2 = SerializationUtils.clone(obj1);
        assertNotSame(obj1, obj2);
        assertEquals(obj1.getName(), obj2.getName());
        assertEquals(obj1.getValue(), obj2.getValue());

        // Test cloning of null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerialize() {
        // Test serialization to an output stream
        SerializableObject obj1 = new SerializableObject("Test", 123);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, outputStream);
        byte[] objectData = outputStream.toByteArray();
        assertNotNull(objectData);
        assertTrue(objectData.length > 0);

        // Test serialization to a byte array
        byte[] data = SerializationUtils.serialize(obj1);
        assertNotNull(data);
        assertTrue(data.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Test deserialization from an input stream
        SerializableObject obj1 = new SerializableObject("Test", 123);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj1, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object obj2 = SerializationUtils.deserialize(inputStream);
        assertNotNull(obj2);
        assertTrue(obj2 instanceof SerializableObject);
        SerializableObject deserializedObj = (SerializableObject) obj2;
        assertEquals(obj1.getName(), deserializedObj.getName());
        assertEquals(obj1.getValue(), deserializedObj.getValue());

        // Test deserialization from a byte array
        byte[] data = SerializationUtils.serialize(obj1);
        Object obj3 = SerializationUtils.deserialize(data);
        assertNotNull(obj3);
        assertTrue(obj3 instanceof SerializableObject);
        SerializableObject deserializedObj2 = (SerializableObject) obj3;
        assertEquals(obj1.getName(), deserializedObj2.getName());
        assertEquals(obj1.getValue(), deserializedObj2.getValue());
    }

    // Helper class for testing serialization
    private static class SerializableObject implements Serializable {
        private String name;
        private int value;

        public SerializableObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}