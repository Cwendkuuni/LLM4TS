import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone("   "));

        // Edge case: txt consists of only non-alphabetic characters
        assertEquals("1111111111", caverphone.caverphone("!@#$%^&*()"));

        // Edge case: txt consists of only vowels
        assertEquals("AAAAAAAAAA", caverphone.caverphone("aeiou"));

        // Edge case: txt consists of only consonants
        assertEquals("1111111111", caverphone.caverphone("bcdfghjklmnpqrstvwxyz"));

    }

    @Test
    public void testEncode() throws Exception {
        try {
          caverphone.encode(12345);
          fail("Expected EncoderException");
        } catch (EncoderException e) {
          // Exception expected, do nothing
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
    }

}