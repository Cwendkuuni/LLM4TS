import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical case
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key", "value");
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        assertEquals(originalMap, clonedMap);
        
        // Edge case - test with null object
        Map<String, String> nullMap = null;
        assertNull(SerializationUtils.clone(nullMap));
        
        // Edge case - test with empty map
        Map<String, String> emptyMap = new HashMap<>();
        Map<String, String> clonedEmptyMap = SerializationUtils.clone(emptyMap);
        assertEquals(emptyMap, clonedEmptyMap);
    }

    @Test
    public void testSerialize() {
        // Typical case
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        byte[] serializedMap = SerializationUtils.serialize(map);
        assertNotNull(serializedMap);
        
        // Edge case - test with null object
        byte[] serializedNull = SerializationUtils.serialize(null);
        assertNull(serializedNull);
        
        // Edge case - test with empty map
        Map<String, String> emptyMap = new HashMap<>();
        byte[] serializedEmptyMap = SerializationUtils.serialize(emptyMap);
        assertNotNull(serializedEmptyMap);
    }

    @Test
    public void testDeserialize() {
        // Typical case
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        byte[] serializedMap = SerializationUtils.serialize(map);
        Map<String, String> deserializedMap = (Map<String, String>) SerializationUtils.deserialize(serializedMap);
        assertEquals(map, deserializedMap);
        
        // Edge case - test with null array
        byte[] nullArray = null;
        assertNull(SerializationUtils.deserialize(nullArray));
        
        // Edge case - test with empty array
        byte[] emptyArray = new byte[0];
        assertNotNull(SerializationUtils.deserialize(emptyArray));
        
        // Error scenario - test with invalid data
        byte[] invalidData = "Invalid Data".getBytes();
        try {
            SerializationUtils.deserialize(invalidData);
            fail("SerializationException should have been thrown");
        } catch (SerializationException e) {
            // SerializationException expected
        }
    }
}