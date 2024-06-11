import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Test clone with a Serializable object
        SerializableObject obj1 = new SerializableObject("Test", 10);
        SerializableObject obj2 = SerializationUtils.clone(obj1);
        
        assertNotSame(obj1, obj2);
        assertEquals(obj1.getName(), obj2.getName());
        assertEquals(obj1.getValue(), obj2.getValue());
        
        // Test clone with a null object
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testSerialize() {
        // Test serialize with a Serializable object
        SerializableObject obj = new SerializableObject("Test", 10);
        byte[] serialized = SerializationUtils.serialize(obj);

        assertNotNull(serialized);
        assertTrue(serialized.length > 0);

        // Test serialize with a null object
        byte[] nullSerialized = SerializationUtils.serialize(null);
        assertNotNull(nullSerialized);
        assertEquals(0, nullSerialized.length);
    }

    @Test
    public void testDeserialize() {
        // Test deserialize with a serialized object
        SerializableObject obj1 = new SerializableObject("Test", 10);
        byte[] serialized = SerializationUtils.serialize(obj1);
        SerializableObject obj2 = (SerializableObject) SerializationUtils.deserialize(serialized);

        assertNotNull(obj2);
        assertNotSame(obj1, obj2);
        assertEquals(obj1.getName(), obj2.getName());
        assertEquals(obj1.getValue(), obj2.getValue());

        // Test deserialize with a null serialized object
        assertNull(SerializationUtils.deserialize(null));

        // Test deserialize with an invalid serialized object
        byte[] invalidSerialized = {1, 2, 3, 4, 5};
        try {
            SerializationUtils.deserialize(invalidSerialized);
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Exception is expected
        }
    }

    // Helper class for testing serialization/deserialization
    private static class SerializableObject implements Serializable {
        private String name;
        private int value;

        public SerializableObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}