package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        String original = "test";
        String cloned = SerializationUtils.clone(original);
        
        assertNotNull(cloned);
        assertEquals(original, cloned);
    }
    
    @Test
    public void testSerializeDeserialize() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key", "value");
        
        byte[] serializedData = SerializationUtils.serialize(originalMap);
        assertNotNull(serializedData);
        
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedMap);
        assertEquals(originalMap, deserializedMap);
    }
    
    @Test
    public void testSerializeNullObject() {
        byte[] serializedData = SerializationUtils.serialize(null);
        assertNull(serializedData);
    }
    
    @Test(expected = SerializationException.class)
    public void testSerializeWithIOException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.close();
            SerializationUtils.serialize("test", outputStream);
        } catch (IOException e) {
            // Should throw SerializationException
            throw new SerializationException(e); 
        }
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeWithIOException() {
        byte[] serializedData = new byte[10];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        
        try {
            inputStream.close();
            SerializationUtils.deserialize(inputStream);
        } catch (IOException e) {
            // Should throw SerializationException
            throw new SerializationException(e); 
        }
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidObject() {
        byte[] serializedData = "invalid".getBytes();
        SerializationUtils.deserialize(serializedData);
    }

}