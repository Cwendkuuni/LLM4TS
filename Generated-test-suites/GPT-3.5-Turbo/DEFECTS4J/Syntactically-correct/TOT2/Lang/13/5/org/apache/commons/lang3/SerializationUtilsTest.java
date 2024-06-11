import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        Map<String, Integer> originalMap = new HashMap<>();
        originalMap.put("key1", 1);
        Map<String, Integer> clonedMap = SerializationUtils.clone(originalMap);
        assertNotSame(originalMap, clonedMap);
        assertEquals(originalMap, clonedMap);

        // Edge case - Null object
        assertNull(SerializationUtils.clone(null));

        // Exception handling scenario - IOException
        try {
            SerializationUtils.clone(new Object()); // Non-serializable object
            fail("SerializationException not thrown");
        } catch (SerializationException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        Serializable object = "Test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(object, outputStream);
        assertEquals("Test", SerializationUtils.deserialize(outputStream.toByteArray()));

        // Edge case - Null object
        try {
            SerializationUtils.serialize(null, outputStream);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }

        // Exception handling scenario - SerializationException
        try {
            SerializationUtils.serialize(new Object(), null); // Null outputStream
            fail("SerializationException not thrown");
        } catch (SerializationException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        byte[] objectData = SerializationUtils.serialize("Test");
        assertEquals("Test", SerializationUtils.deserialize(objectData));

        // Edge case - Null byte array
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }

        // Exception handling scenario - ClassNotFoundException
        try {
            // Use a custom object that does not exist in the classpath
            byte[] customObjectData = SerializationUtils.serialize(new CustomObject());
            SerializationUtils.deserialize(customObjectData);
            fail("SerializationException not thrown");
        } catch (SerializationException e) {
            // Expected exception thrown
        }
    }

    // Custom object for testing ClassNotFoundException
    private static class CustomObject implements Serializable {
        private static final long serialVersionUID = 1L;
    }

}