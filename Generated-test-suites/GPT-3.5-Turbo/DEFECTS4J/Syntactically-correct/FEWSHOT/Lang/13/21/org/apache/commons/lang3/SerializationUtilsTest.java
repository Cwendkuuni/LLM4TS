package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test cloning a non-null object
        SerializableObject original = new SerializableObject("Hello");
        SerializableObject cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Test cloning a null object
        SerializableObject nullObject = null;
        SerializableObject clonedNullObject = SerializationUtils.clone(nullObject);
        assertNull(clonedNullObject);
    }
    
    @Test
    public void testSerialize() {
        // Test serializing a non-null object
        SerializableObject original = new SerializableObject("Hello");
        byte[] serialized = SerializationUtils.serialize(original);
        SerializableObject deserialized = (SerializableObject) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
        
        // Test serializing a null object
        SerializableObject nullObject = null;
        byte[] serializedNull = SerializationUtils.serialize(nullObject);
        SerializableObject deserializedNull = (SerializableObject) SerializationUtils.deserialize(serializedNull);
        assertNull(deserializedNull);
    }
    
    @Test
    public void testDeserialize() {
        // Test deserializing a non-null object
        byte[] serialized = new byte[]{};
        SerializableObject deserialized = (SerializableObject) SerializationUtils.deserialize(serialized);
        assertEquals(new SerializableObject(""), deserialized);
        
        // Test deserializing a null object
        byte[] serializedNull = null;
        SerializableObject deserializedNull = (SerializableObject) SerializationUtils.deserialize(serializedNull);
        assertNull(deserializedNull);
    }
    
    // Helper class for testing
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private String message;
        
        public SerializableObject(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
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
            
            if (message == null) {
                return other.message == null;
            } else {
                return message.equals(other.message);
            }
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(message);
        }
    }
}