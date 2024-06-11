import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("WT11111111", caverphone.caverphone("What's Up"));
        assertEquals("BTL1111111", caverphone.caverphone("Bottle"));
        // Edge Cases
        assertEquals("Y311111111", caverphone.caverphone("Yyy")); // 1.0 only case
        assertEquals("B111111111", caverphone.caverphone("B")); // 1.0 only case
        assertEquals("A111111111", caverphone.caverphone("")); // Handling empty string
    }

    @Test
    public void testEncodeObject() {
        try {
            caverphone.encode(123);
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        assertEquals("1111111111", caverphone.encode("nonalphabetic123")); // Handling non-alphabetic characters
        assertEquals("TT11111111", caverphone.encode("Testing")); 
        // Edge Cases
        assertEquals("A111111111", caverphone.encode("")); // Handling empty string
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("abc", "ABC"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
        // Edge Cases
        assertFalse(caverphone.isCaverphoneEqual("", "abc")); // Handling empty string
    }

}