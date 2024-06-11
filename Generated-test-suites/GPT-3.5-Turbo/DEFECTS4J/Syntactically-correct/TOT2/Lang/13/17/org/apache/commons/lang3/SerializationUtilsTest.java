import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical Use Case
        Serializable object = new SerializableObject();
        Serializable clonedObject = SerializationUtils.clone(object);
        assertEquals(object, clonedObject);

        // Edge Cases
        object = null;
        clonedObject = SerializationUtils.clone(object);
        assertNull(clonedObject);

        // Error Scenarios
        // Invalid input for serialization
        try {
            SerializationUtils.clone(new NonSerializableObject());
            fail("SerializationException not thrown for non-serializable object");
        } catch (SerializationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSerialize() {
        // Typical Use Case
        Serializable object = new SerializableObject();
        byte[] serializedData = SerializationUtils.serialize(object);
        assertNotNull(serializedData);

        // Edge Cases
        object = null;
        serializedData = SerializationUtils.serialize(object);
        assertNotNull(serializedData);

        // Error Scenarios
        // Invalid output stream
        try {
            SerializationUtils.serialize(new SerializableObject(), null);
            fail("IllegalArgumentException not thrown for null output stream");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDeserialize() {
        // Typical Use Case
        Serializable object = new SerializableObject();
        byte[] serializedData = SerializationUtils.serialize(object);
        Serializable deserializedObject = (Serializable) SerializationUtils.deserialize(serializedData);
        assertEquals(object, deserializedObject);

        // Edge Cases
        serializedData = null;
        deserializedObject = (Serializable) SerializationUtils.deserialize(serializedData);
        assertNull(deserializedObject);

        // Error Scenarios
        // Invalid input stream
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    private static class NonSerializableObject {
    }
}