import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String original = "Serialize";
        byte[] serialized = SerializationUtils.serialize(original);
        assertNotNull(serialized);
        
        // Edge case
        try {
            SerializationUtils.serialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String original = "Deserialize";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);
        
        // Edge case
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

}