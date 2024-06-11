package org.apache.commons.lang3;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerializationUtilsTest {

    // Clone method test cases
    @Test
    public void testClone() {
        SerializableObject obj = new SerializableObject("Test");
        SerializableObject clonedObj = SerializationUtils.clone(obj);
        assertEquals(obj, clonedObj);
    }

    // Serialize method test cases
    @Test
    public void testSerialize() {
        SerializableObject obj = new SerializableObject("Test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedData = baos.toByteArray();
        assertNotNull(serializedData);
    }

    // Deserialize method test cases
    @Test
    public void testDeserialize() {
        SerializableObject obj = new SerializableObject("Test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        SerializableObject deserializedObj = (SerializableObject) SerializationUtils.deserialize(bais);
        assertEquals(obj, deserializedObj);
    }

    // Edge cases and error scenarios
    @Test(expected = SerializationException.class)
    public void testCloneWithNullObject() {
        SerializationUtils.clone(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullObject() {
        SerializationUtils.serialize(null, new ByteArrayOutputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        SerializationUtils.deserialize((ByteArrayInputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullByteArray() {
        SerializationUtils.deserialize((byte[]) null);
    }

    // Helper SerializableObject class for testing
    private static class SerializableObject implements Serializable {
        private String name;

        public SerializableObject(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SerializableObject that = (SerializableObject) obj;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}