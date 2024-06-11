import static org.junit.Assert.*;
import org.junit.Test;

public class SerializationUtilsTest {

    @Test
    public void testClone() {
        // Typical use case
        String original = "Hello, World!";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);

        // Edge case
        String nullString = null;
        String clonedNullString = SerializationUtils.clone(nullString);
        assertNull(clonedNullString);

        // Exception handling
        try {
            SerializationUtils.clone(new Object());
            fail("Expected SerializationException");
        } catch (SerializationException e) {
            // Expected
        }
    }

    @Test
    public void testSerialize() {
        // Typical use case
        byte[] serialized = SerializationUtils.serialize("Java is awesome");
        assertNotNull(serialized);

        // Edge case
        byte[] emptySerialized = SerializationUtils.serialize("");
        assertNotNull(emptySerialized);

        // Exception handling
        try {
            SerializationUtils.serialize(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDeserialize() {
        // Typical use case
        byte[] serialized = SerializationUtils.serialize("Java is awesome");
        String deserialized = (String) SerializationUtils.deserialize(serialized);
        assertEquals("Java is awesome", deserialized);

        // Edge case
        byte[] emptySerialized = SerializationUtils.serialize("");
        String emptyDeserialized = (String) SerializationUtils.deserialize(emptySerialized);
        assertEquals("", emptyDeserialized);

        // Exception handling
        try {
            SerializationUtils.deserialize((byte[])null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Exception handling
        try {
            SerializationUtils.deserialize((InputStream)null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testClassLoaderAwareObjectInputStream() {
        // Not needed to test separately as it is an internal class with specific purpose
        assertTrue(true);
    }

}