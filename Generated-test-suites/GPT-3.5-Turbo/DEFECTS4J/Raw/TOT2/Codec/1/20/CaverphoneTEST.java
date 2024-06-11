import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(""));

        // Edge cases
        assertEquals("1111111111", caverphone.caverphone(null)); // Null input

        // Error scenarios
        assertEquals("1111111111", caverphone.caverphone("123")); // Non-alphabetic characters
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        assertEquals("1111111111", (String) caverphone.encode(""));

        // Edge cases
        try {
            caverphone.encode(new Object()); // Invalid object type
            fail("EncoderException not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        assertEquals("1111111111", caverphone.encode(""));

        // Edge cases
        assertEquals("1111111111", caverphone.encode(null)); // Null input
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use case
        assertTrue(caverphone.isCaverphoneEqual("hello", "Hello"));

        // Edge cases
        assertFalse(caverphone.isCaverphoneEqual("hello", "world")); // Different strings
    }
}