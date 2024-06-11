
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SerializationUtilsTest {
    
    @Test
    public void testClone() {
        // Test basic clone functionality
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);
        
        assertEquals("Clone should be equal to original map", map, clonedMap);
        assertNotSame("Clone should be a separate instance", map, clonedMap);
    }
    
    @Test
    public void testCloneWithNull() {
        // Test clone with null input
        assertNull("Clone of null should be null", SerializationUtils.clone(null));
    }
    
    @Test
    public void testSerialize() {
        // Test basic serialize functionality
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, baos);
        
        byte[] serializedData = baos.toByteArray();
        
        assertNotNull("Serialized data should not be null", serializedData);
        assertTrue("Serialized data should not be empty", serializedData.length > 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullOutputStream() {
        // Test serialize with null OutputStream
        Map<String, Integer> map = new HashMap<>();
        
        SerializationUtils.serialize(map, null);
    }
    
    @Test
    public void testSerializeWithNull() {
        // Test serialize with null input
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        
        byte[] serializedData = baos.toByteArray();
        
        assertNotNull("Serialized data should not be null", serializedData);
        assertTrue("Serialized data should not be empty", serializedData.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test basic deserialize functionality
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, baos);
        
        byte[] serializedData = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        Map<String, Integer> deserializedMap = (Map<String, Integer>) SerializationUtils.deserialize(bais);
        
        assertEquals("Deserialized map should be equal to original map", map, deserializedMap);
        assertNotSame("Deserialized map should be a separate instance", map, deserializedMap);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeWithNullInputStream() {
        // Test deserialize with null InputStream
        SerializationUtils.deserialize(null);
    }
    
    @Test
    public void testDeserializeWithNullSerializedData() {
        // Test deserialize with null serialized data
        Object deserializedObject = SerializationUtils.deserialize(null);
        
        assertNull("Deserialized object should be null", deserializedObject);
    }
    
    static class TestClass implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int value;
        
        public TestClass(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        public void setValue(int value) {
            this.value = value;
        }
    }
}
