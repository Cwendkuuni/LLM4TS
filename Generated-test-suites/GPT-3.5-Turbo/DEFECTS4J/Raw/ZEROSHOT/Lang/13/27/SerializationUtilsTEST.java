import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test null object
        assertNull(SerializationUtils.clone(null));
        
        // Test cloning a Serializable object
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("key1", "value1");
        Map<String, String> clonedMap = SerializationUtils.clone(originalMap);
        assertNotSame(originalMap, clonedMap);
        assertEquals(originalMap, clonedMap);
    }

    @Test
    public void testSerialize() {
        // Test null object
        try {
            SerializationUtils.serialize(null, null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test serialization
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        SerializationUtils.serialize(map, outputStream);
        byte[] serializedData = outputStream.toByteArray();
        assertNotNull(serializedData);
        assertTrue(serializedData.length > 0);
    }

    @Test
    public void testDeserialize() {
        // Test null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test deserialization
        byte[] serializedData = new byte[10];
        Object deserializedObject = SerializationUtils.deserialize(serializedData);
        assertNotNull(deserializedObject);
    }

    @Test
    public void testDeserializeInputStream() {
        // Test null input stream
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Test deserialization
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Object deserializedObject = SerializationUtils.deserialize(inputStream);
        assertNotNull(deserializedObject);
    }

}