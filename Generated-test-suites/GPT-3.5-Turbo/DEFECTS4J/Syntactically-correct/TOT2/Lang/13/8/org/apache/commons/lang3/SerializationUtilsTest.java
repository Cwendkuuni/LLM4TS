import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        Map<String, Integer> map = new HashMap<>();
        map.put("key1", 1);
        Map<String, Integer> clonedMap = SerializationUtils.clone(map);
        assertEquals(clonedMap, map);

        // Edge case - cloning null object
        assertNull(SerializationUtils.clone(null));

        // Error scenario - serialization failure
        Object obj = new Object();
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.clone(obj);
        });
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String str = "Test String";
        byte[] serializedData = SerializationUtils.serialize(str);
        assertNotNull(serializedData);

        // Edge case - serializing null object
        assertNull(SerializationUtils.serialize(null));

        // Error scenario - null output stream
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.serialize(str, null);
        });

        // Error scenario - serialization failure
        OutputStream outputStream = new ByteArrayOutputStream();
        assertThrows(SerializationException.class, () -> {
            SerializationUtils.serialize(new NoSerializableObject(), outputStream);
        });
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String str = "Test String";
        byte[] serializedData = SerializationUtils.serialize(str);
        String deserializedStr = (String) SerializationUtils.deserialize(serializedData);
        assertEquals(str, deserializedStr);

        // Edge case - deserializing null data
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(null);
        });

        // Error scenario - null input stream
        assertThrows(IllegalArgumentException.class, () -> {
            SerializationUtils.deserialize(null);
        });

        // Error scenario - deserialization failure
        assertThrows(SerializationException.class, () -> {
            byte[] invalidData = {1, 2, 3};
            SerializationUtils.deserialize(invalidData);
        });
    }

   static class NoSerializableObject {
        public NoSerializableObject() {
        }
    }
}