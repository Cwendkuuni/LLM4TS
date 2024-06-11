package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.junit.Test;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test cloning of Serializable objects
        
        // Test cloning a null object
        assertNull(SerializationUtils.clone(null));
        
        // Test cloning a non-null object
        TestObject obj = new TestObject("test");
        TestObject clone = SerializationUtils.clone(obj);
        assertNotSame(obj, clone);  // Object references should not be the same
        assertEquals(obj, clone);  // Cloned object should be equal to the original object

        // Test cloning a non-serializable object (should throw a SerializationException)
        try {
            SerializationUtils.clone(new NonSerializableObject());
            fail("Expected SerializationException to be thrown");
        } catch (SerializationException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSerialize() {
        // Test serializing an object to an OutputStream
        
        // Test serializing a null object (should throw an IllegalArgumentException)
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test serializing an object to a non-null OutputStream
        TestObject obj = new TestObject("test");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        byte[] serializedBytes = baos.toByteArray();
        assertNotNull(serializedBytes);
        assertTrue(serializedBytes.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test deserializing an object from an InputStream
        
        // Test deserializing an object from a null InputStream (should throw an IllegalArgumentException)
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test deserializing an object from a non-null InputStream
        TestObject obj = new TestObject("test");
        byte[] serializedBytes = serializeObject(obj);
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
        TestObject deserializedObject = (TestObject) SerializationUtils.deserialize(bais);
        assertNotNull(deserializedObject);
        assertEquals(obj, deserializedObject);
    }
    
    private byte[] serializeObject(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        return baos.toByteArray();
    }
    
    private class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        
        public TestObject(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
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
            TestObject other = (TestObject) obj;
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }
    }
    
    private class NonSerializableObject {
        // This class is not Serializable
    }    
}