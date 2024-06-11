import static org.hamcrest.CoreMatchers.*;
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

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

public class SerializationUtilsTest {
    
    // Test cases for clone method
    @Test
    public void testClone_typicalCase() {
        // Arrange
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        map1.put("b", 2);
        
        // Act
        Map<String, Integer> map2 = SerializationUtils.clone(map1);
        
        // Assert
        assertThat(map2, is(equalTo(map1)));
    }
    
    @Test
    public void testClone_nullInput() {
        // Arrange
        Map<String, Integer> map1 = null;
        
        // Act
        Map<String, Integer> map2 = SerializationUtils.clone(map1);
        
        // Assert
        assertThat(map2, is(nullValue()));
    }
    
    @Test(expected = SerializationException.class)
    public void testClone_serializationException() {
        // Arrange
        UnserializableObject obj = new UnserializableObject();
        
        // Act
        SerializationUtils.clone(obj);
        
        // Assert
        // Expecting SerializationException to be thrown
    }
    
    // Test cases for serialize method
    @Test
    public void testSerialize_typicalCase() {
        // Arrange
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(map, baos);
        byte[] serialized = baos.toByteArray();
        
        // Assert
        assertThat(serialized, is(notNullValue()));
        assertThat(serialized.length, is(not(0)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_nullObject() {
        // Arrange
        Map<String, Integer> map = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // Act
        SerializationUtils.serialize(map, baos);
        
        // Assert
        // Expecting IllegalArgumentException to be thrown
    }
    
    // Test cases for deserialize method
    @Test
    public void testDeserialize_typicalCase() {
        // Arrange
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        byte[] serialized = serializeObject(map);
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        
        // Act
        Map<String, Integer> deserialized = SerializationUtils.deserialize(bais);
        
        // Assert
        assertThat(deserialized, is(equalTo(map)));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserialize_nullInputStream() {
        // Arrange
        InputStream inputStream = null;
        
        // Act
        SerializationUtils.deserialize(inputStream);
        
        // Assert
        // Expecting IllegalArgumentException to be thrown
    }
    
    @Test(expected = SerializationException.class)
    public void testDeserialize_serializationException() {
        // Arrange
        byte[] serialized = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
        
        // Act
        SerializationUtils.deserialize(bais);
        
        // Assert
        // Expecting SerializationException to be thrown
    }
    
    // Helper method to serialize an object
    private byte[] serializeObject(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }
    
    // Helper class for testing serialization exceptions
    private class UnserializableObject {}
}