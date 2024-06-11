package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
        // Test cloning of a null object
        assertNull(SerializationUtils.clone(null));

        // Create a Serializable object for testing
        SerializableObject obj = new SerializableObject("Test", 100);
        
        // Test cloning of a SerializableObject object
        SerializableObject clonedObj = SerializationUtils.clone(obj);
        assertNotNull(clonedObj);
        assertNotSame(obj, clonedObj);
        assertEquals(obj.getName(), clonedObj.getName());
        assertEquals(obj.getValue(), clonedObj.getValue());
    }
    
    @Test
    public void testSerialize() throws IOException {
        // Test serialization of a null object
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertEquals(0, serializedData.length);
        
        // Create a Serializable object for testing
        SerializableObject obj = new SerializableObject("Test", 100);
        
        // Test serialization of a SerializableObject object
        outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertNotEquals(0, serializedData.length);
        
        // Deserialize the serialized data and compare with original object
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof SerializableObject);
        SerializableObject deserializedObj = (SerializableObject)deserializedObject;
        assertEquals(obj.getName(), deserializedObj.getName());
        assertEquals(obj.getValue(), deserializedObj.getValue());
    }
    
    @Test
    public void testDeserialize() throws IOException {
        // Test deserialization of empty serialized data
        Object obj = SerializationUtils.deserialize(new byte[0]);
        assertNull(obj);
        
        // Create a Serializable object for testing
        SerializableObject originalObj = new SerializableObject("Test", 100);
        
        // Serialize the object to get serialized data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(originalObj, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertNotEquals(0, serializedData.length);
        
        // Test deserialization of the serialized data
        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObject);
        assertTrue(deserializedObject instanceof SerializableObject);
        SerializableObject deserializedObj = (SerializableObject)deserializedObject;
        assertEquals(originalObj.getName(), deserializedObj.getName());
        assertEquals(originalObj.getValue(), deserializedObj.getValue());
    }

    // Custom Serializable class for testing
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
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