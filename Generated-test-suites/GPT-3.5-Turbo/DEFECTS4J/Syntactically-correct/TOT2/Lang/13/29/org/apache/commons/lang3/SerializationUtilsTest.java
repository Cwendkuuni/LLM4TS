import org.junit.Test;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello, world!";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
        
        // Edge case
        assertNull(SerializationUtils.clone(null));
        
        // Error scenario
        try {
            SerializationUtils.clone(new Object());
            fail("SerializationException not thrown for non-Serializable object");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        String obj = "Serialize me!";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SerializationUtils.serialize(obj, outputStream);
        byte[] serialized = outputStream.toByteArray();
        assertNotNull(serialized);
        
        // Error scenario
        try {
            SerializationUtils.serialize(null, new ByteArrayOutputStream());
            fail("IllegalArgumentException not thrown for null object");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String obj = "Deserialize me!";
        byte[] serialized = SerializationUtils.serialize(obj);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
        String deserialized = (String) SerializationUtils.deserialize(inputStream);
        assertEquals(obj, deserialized);
        
        // Error scenarios
        try {
            SerializationUtils.deserialize(null);
            fail("IllegalArgumentException not thrown for null input stream");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            SerializationUtils.deserialize(new byte[0]);
            fail("IllegalArgumentException not thrown for empty byte array");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            SerializationUtils.deserialize(new byte[1]);
            fail("SerializationException not thrown for invalid byte array");
        } catch (RuntimeException e) {
            // expected
        }
    }

}