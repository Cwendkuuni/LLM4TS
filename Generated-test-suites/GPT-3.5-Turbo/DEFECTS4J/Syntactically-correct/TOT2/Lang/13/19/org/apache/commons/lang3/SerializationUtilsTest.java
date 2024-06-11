import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        // Create a Serializable object
        SerializableObject original = new SerializableObject("test");
        // Clone the object
        SerializableObject cloned = SerializationUtils.clone(original);
        // Check if the objects are not the same reference
        assertNotSame(original, cloned);
        // Check if the objects have the same values
        assertEquals(original.getName(), cloned.getName());
    }

    @Test
    public void testSerializeAndDeserialize() {
        // Typical use case
        // Create a Serializable object
        SerializableObject original = new SerializableObject("test");
        // Serialize the object to a byte array
        byte[] serializedObject = SerializationUtils.serialize(original);
        assertNotNull(serializedObject);
        // Deserialize the object from the byte array
        SerializableObject deserialized = (SerializableObject) SerializationUtils.deserialize(serializedObject);
        // Check if the deserialized object has the same values as the original
        assertEquals(original.getName(), deserialized.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullObject() {
        // Edge case: Serialize with null object
        SerializationUtils.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithNullObject() {
        // Edge case: Clone with null object
        SerializationUtils.clone(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeWithNullInputStream() {
        // Edge case: Deserialize with null input stream
        SerializationUtils.deserialize(null);
    }

    @Test
    public void testDeserializeWithInvalidData() {
        // Error scenario: Deserialize with invalid data
        byte[] invalidData = new byte[]{0, 1, 2, 3};
        try {
            SerializationUtils.deserialize(invalidData);
            fail("Should have thrown a SerializationException");
        } catch (SerializationException e) {
            // Expected exception
            assertTrue(true);
        }
    }

    private static class SerializableObject implements Serializable {
        private String name;

        public SerializableObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}