package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test null object
        assertNull(SerializationUtils.clone(null));
        
        // Test non-null object
        SerializableObject obj = new SerializableObject();
        SerializableObject clonedObj = SerializationUtils.clone(obj);
        
        // Test if the object and cloned object are equal
        assertEquals(obj, clonedObj);
    }

    @Test
    public void testSerialize() {
        // Test null object
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        byte[] serializedData = baos.toByteArray();
        assertEquals(0, serializedData.length);
        
        // Test non-null object
        SerializableObject obj = new SerializableObject();
        baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, baos);
        serializedData = baos.toByteArray();
        
        // Test if the serialized data is not empty
        assertTrue(serializedData.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test null input stream
        assertNull(SerializationUtils.deserialize(null));
        
        // Test empty input stream
        byte[] emptyData = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(emptyData);
        assertNull(SerializationUtils.deserialize(bais));
        
        // Test non-empty input stream
        SerializableObject obj = new SerializableObject();
        ByteArrayInputStream bais2 = new ByteArrayInputStream(SerializationUtils.serialize(obj));
        SerializableObject deserializedObj = (SerializableObject) SerializationUtils.deserialize(bais2);
        
        // Test if the deserialized object is equal to the original object
        assertEquals(obj, deserializedObj);
    }

    // Inner class for testing serialization
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private int age;
        
        public SerializableObject() {
            this.name = "John Doe";
            this.age = 30;
        }
        
        public SerializableObject(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
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
            return name.equals(other.name) && age == other.age;
        }
        
        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + Integer.hashCode(age);
            return result;
        }
    }
}