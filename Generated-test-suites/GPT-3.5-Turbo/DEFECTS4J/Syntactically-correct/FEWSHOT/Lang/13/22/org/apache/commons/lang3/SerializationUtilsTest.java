import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SerializationUtilsTest {
    @Test
    public void testClone() {
        // Test with null object
        assertNull(SerializationUtils.clone(null));
        
        // Test with Serializable object
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Map<String, String> clonedMap = SerializationUtils.clone(map);
        assertEquals(map, clonedMap);
    }
    
    @Test
    public void testSerialize() {
        // Test with null object and null output stream
        try {
            SerializationUtils.serialize(null, null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test with null object and valid output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        assertEquals(0, serializedData.length);
        
        // Test with Serializable object and output stream
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }
    
    @Test
    public void testDeserialize() {
        // Test with null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test with null byte array
        byte[] serializedData = null;
        assertNull(SerializationUtils.deserialize(serializedData));
        
        // Test with empty byte array
        serializedData = new byte[0];
        assertNull(SerializationUtils.deserialize(serializedData));
        
        // Test with valid serialized object
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(map, outputStream);
        serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedData);
        assertEquals(map, deserializedMap);
    }
}