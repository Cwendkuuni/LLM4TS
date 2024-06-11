package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello World";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case - null input
        assertNull(SerializationUtils.clone(null));
        
        // Error scenario - ClassNotFoundException
        // Not applicable for now with the given class
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String object = "Serialize Me!";
        byte[] serializedBytes = SerializationUtils.serialize(object);
        assertNotNull(serializedBytes);
        
        // Edge case - null input
        try {
            SerializationUtils.serialize(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Error scenario - IOException
        // Not applicable for now with the given class
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String object = "Deserialize Me!";
        byte[] serializedBytes = SerializationUtils.serialize(object);
        String deserialized = (String) SerializationUtils.deserialize(serializedBytes);
        assertEquals(object, deserialized);
        
        // Edge case - null input
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Error scenario - ClassNotFoundException
        // Not applicable for now with the given class
    }

}