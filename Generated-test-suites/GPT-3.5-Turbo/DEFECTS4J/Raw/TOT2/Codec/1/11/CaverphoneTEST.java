import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null)); // Edge case: null input
        assertEquals("1111111111", caverphone.caverphone("")); // Edge case: empty input
        assertEquals("AA11111111", caverphone.caverphone("hello123")); // Typical use case
        assertEquals("K2ch3n2", caverphone.caverphone("kitchen")); // Typical use case
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Object());
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            assertTrue(true); // Error scenario: invalid input type
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode("")); // Edge case: empty input
        assertEquals("AA11111111", caverphone.encode("hello123")); // Typical use case
        assertEquals("K2ch3n2", caverphone.encode("kitchen")); // Typical use case
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello")); // Typical use case
        assertFalse(caverphone.isCaverphoneEqual("hello", "world")); // Typical use case
    }
}