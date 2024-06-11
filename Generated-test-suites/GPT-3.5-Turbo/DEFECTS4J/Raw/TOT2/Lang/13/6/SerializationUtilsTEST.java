import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical Use Case
        String original = "Original String";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Edge Case
        assertNull(SerializationUtils.clone(null));

        // Error Scenario
        try {
            SerializationUtils.clone(new Object());
            fail("SerializationException expected");
        } catch (SerializationException e) {
            // Success
        }
    }

    @Test
    public void testSerialize() {
        // Typical Use Case
        String original = "Original String";
        byte[] serialized = SerializationUtils.serialize(original);
        assertNotNull(serialized);

        // Edge Case
        byte[] emptySerialized = SerializationUtils.serialize(null);
        assertEquals(0, emptySerialized.length);

        // Error Scenario
        try {
            SerializationUtils.serialize(new Object(), null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testDeserialize() {
        // Typical Use Case
        String original = "Original String";
        byte[] serialized = SerializationUtils.serialize(original);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(original, deserialized);

        // Edge Case
        assertNull(SerializationUtils.deserialize(null));

        // Error Scenario
        try {
            SerializationUtils.deserialize(new byte[0]);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }
}