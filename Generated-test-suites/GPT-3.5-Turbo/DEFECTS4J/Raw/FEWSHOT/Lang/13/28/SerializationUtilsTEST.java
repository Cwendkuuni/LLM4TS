import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;

import org.apache.commons.lang3.SerializationException;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test with null object
        assertNull(SerializationUtils.clone(null));
        
        // Test with Serializable object
        SerializableObject obj = new SerializableObject(10, "test");
        SerializableObject clonedObj = SerializationUtils.clone(obj);
        assertEquals(obj, clonedObj);
        assertNotSame(obj, clonedObj);
        
        // Test with non-serializable object
        NonSerializableObject nonSerializableObj = new NonSerializableObject(10, "test");
        try {
            SerializationUtils.clone(nonSerializableObj);
            fail("SerializationException should be thrown for non-serializable object");
        } catch (SerializationException e) {
            // Exception should be caught
        }
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Test with null object
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SerializationUtils.serialize(null, baos);
            byte[] result = baos.toByteArray();
            assertEquals(0, result.length);
        }

        // Test with Serializable object
        SerializableObject obj = new SerializableObject(10, "test");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SerializationUtils.serialize(obj, baos);
            byte[] result = baos.toByteArray();
            assertNotNull(result);
            assertNotEquals(0, result.length);
        }

        // Test with non-serializable object
        NonSerializableObject nonSerializableObj = new NonSerializableObject(10, "test");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try {
                SerializationUtils.serialize(nonSerializableObj, baos);
                fail("SerializationException should be thrown for non-serializable object");
            } catch (SerializationException e) {
                // Exception should be caught
            }
        }
    }
    
    @Test
    public void testDeserialize() throws IOException {
        // Test with null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException should be thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Exception should be caught
        }

        // Test with serialized object
        SerializableObject obj = new SerializableObject(10, "test");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SerializationUtils.serialize(obj, baos);
            byte[] serializedData = baos.toByteArray();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedData)) {
                Object deserializedObj = SerializationUtils.deserialize(bais);
                assertNotNull(deserializedObj);
                assertTrue(deserializedObj instanceof SerializableObject);
                assertEquals(obj, deserializedObj);
            }
        }

        // Test with invalid serialized data
        byte[] invalidData = { 1, 2, 3, 4, 5 };
        try (ByteArrayInputStream bais = new ByteArrayInputStream(invalidData)) {
            try {
                SerializationUtils.deserialize(bais);
                fail("SerializationException should be thrown for invalid serialized data");
            } catch (SerializationException e) {
                // Exception should be caught
            }
        }
    }

    @Test
    public void testDeserializeByteArray() {
        // Test with null byte array
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException should be thrown for null byte array");
        } catch (IllegalArgumentException e) {
            // Exception should be caught
        }

        // Test with serialized object
        SerializableObject obj = new SerializableObject(10, "test");
        byte[] serializedData = SerializationUtils.serialize(obj);
        Object deserializedObj = SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObj);
        assertTrue(deserializedObj instanceof SerializableObject);
        assertEquals(obj, deserializedObj);

        // Test with invalid serialized data
        byte[] invalidData = { 1, 2, 3, 4, 5 };
        try {
            SerializationUtils.deserialize(invalidData);
            fail("SerializationException should be thrown for invalid serialized data");
        } catch (SerializationException e) {
            // Exception should be caught
        }
    }
    
    private static class SerializableObject implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        private int value;
        private String name;
        
        public SerializableObject(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SerializableObject other = (SerializableObject) obj;
            return value == other.value && name.equals(other.name);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }
    }
    
    private static class NonSerializableObject {
        private int value;
        private String name;

        public NonSerializableObject(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}