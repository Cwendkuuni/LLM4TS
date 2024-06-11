import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null)); // Null input
        assertEquals("1111111111", caverphone.caverphone("")); // Empty input
        assertEquals("k2na12f1", caverphone.caverphone("Koala")); // Typical input
    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            assertEquals("k2na12f1", caverphone.encode("Koala")); // Typical input
        } catch (EncoderException e) {
            fail("Exception thrown for valid input");
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("k2na12f1", caverphone.encode("Koala")); // Typical input
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("Koala", "Koala")); // Same input
        assertFalse(caverphone.isCaverphoneEqual("Koala", "Koal")); // Different input
    }

    @Test
    public void testEdgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("2nough1111", caverphone.caverphone("Enough")); // Edge case with special handling
        assertEquals("cAR3LL2", caverphone.caverphone("Carroll")); // Edge case with multiple replacements
    }

    @Test
    public void testExceptions() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(123); // Non-string input should throw EncoderException
            fail("No exception thrown for non-string input");
        } catch (EncoderException e) {
            // Exception should be thrown
        }
    }
}