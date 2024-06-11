import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello, world!";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case: null object
        assertNull(SerializationUtils.clone(null));
        
        // Exception handling: SerializationException
        // Not possible to test if serialization fails using standard Java serialization. 
        // An example could be trying to clone an object that has non-serializable fields.
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String obj = "Serialize me!";
        byte[] serialized = SerializationUtils.serialize(obj);
        assertNotNull(serialized);
        
        // Edge case: null object
        byte[] nullSerialized = SerializationUtils.serialize(null);
        assertEquals(0, nullSerialized.length);
        
        // Exception handling: IllegalArgumentException
        try {
            SerializationUtils.serialize(obj, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Exception handling: SerializationException
        // Not possible to test if serialization fails using standard Java serialization. 
        // An example could be trying to serialize an object that has non-serializable fields.
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        byte[] data = SerializationUtils.serialize("Deserialize me!");
        Object deserialized = SerializationUtils.deserialize(data);
        assertEquals("Deserialize me!", deserialized);
        
        // Edge case: null byte array
        try {
            SerializationUtils.deserialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Exception handling: IllegalArgumentException
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            SerializationUtils.deserialize((InputStream) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Exception handling: SerializationException
        // Not possible to test if deserialization fails using standard Java serialization. 
        // An example could be trying to deserialize data that does not represent a valid serialized object.
    }

}