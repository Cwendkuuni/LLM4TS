package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        originalMap.put("key2", "value2");
        
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        
        assertEquals(originalMap, clonedMap);
    }
    
    @Test
    public void testSerialize() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        
        byte[] expected = outputStream.toByteArray();
        byte[] actual = SerializationUtils.serialize(map);
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testDeserialize() {
        byte[] serializedData = {1, 2, 3, 4, 5};
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        
        assertNull(deserializedObject);
    }
    
    @Test
    public void testDeserializeByteArray() {
        byte[] serializedData = {1, 2, 3, 4, 5};
        
        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        
        assertNull(deserializedObject);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullOutputStream() {
        Map<String, String> map = new HashMap<>();
        
        SerializationUtils.serialize(map, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullInputStream() {
        SerializationUtils.deserialize(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeNullByteArray() {
        SerializationUtils.deserialize(null);
    }
}