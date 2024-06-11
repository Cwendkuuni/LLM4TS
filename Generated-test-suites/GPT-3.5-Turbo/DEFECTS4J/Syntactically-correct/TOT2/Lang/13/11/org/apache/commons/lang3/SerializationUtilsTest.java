import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "test";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Edge case
        assertNull(SerializationUtils.clone(null));

        // Error scenario
        try {
            SerializationUtils.clone(new Object());
            fail("SerializationException should have been thrown");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerialize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Typical use case
        String obj = "test";
        SerializationUtils.serialize(obj, outputStream);
        byte[] result = outputStream.toByteArray();
        assertNotNull(result);

        // Edge case
        try {
            SerializationUtils.serialize(null, outputStream);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario
        try {
            SerializationUtils.serialize(new Object(), null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        String obj = "test";
        byte[] data = SerializationUtils.serialize(obj);
        String result = (String) SerializationUtils.deserialize(data);
        assertEquals(obj, result);

        // Edge case
        try {
            SerializationUtils.deserialize(new byte[0]);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Error scenario
        try {
            SerializationUtils.deserialize((byte[]) null);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}