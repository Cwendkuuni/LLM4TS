package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        Integer original = new Integer(10);
        Integer cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Edge case: null object
        assertNull(SerializationUtils.clone(null));

        // Error scenario: ClassNotFoundException
        // Not testing here as it requires a custom object with an unknown class
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String input = "Hello, World!";
        byte[] serialized = SerializationUtils.serialize(input);
        assertNotNull(serialized);

        // Edge case: null object
        assertNull(SerializationUtils.serialize(null));

        // Error scenario: IOException
        // Not testing here as it's handled internally
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String input = "Hello, World!";
        byte[] serialized = SerializationUtils.serialize(input);
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals(input, deserialized);

        // Edge case: empty byte array
        byte[] emptyArray = new byte[0];
        assertNull(SerializationUtils.deserialize(emptyArray));

        // Error scenario: ClassNotFoundException
        // Not testing here as it requires a custom object with an unknown class
    }
}