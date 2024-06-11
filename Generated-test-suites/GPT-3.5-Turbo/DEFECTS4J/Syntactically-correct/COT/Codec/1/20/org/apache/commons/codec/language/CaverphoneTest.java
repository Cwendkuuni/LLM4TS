import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Basic functionality test
        assertEquals("KM11111111", caverphone.caverphone("kumar"));

        // Edge cases
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("M311111111", caverphone.caverphone("m3c1n3s"));
        assertEquals("B111111111", caverphone.caverphone("B00000000"));
        assertEquals("S311111116", caverphone.caverphone("s321 s3216"));

        // Exception handling
        // Test for null input
        try {
            caverphone.caverphone(null);
            fail("Null input should throw an exception");
        } catch (NullPointerException e) {
            // Exception successfully caught
        }
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Basic functionality test
        assertEquals("KM11111111", caverphone.encode("kumar"));

        // Exception handling
        // Test for non-String input
        try {
            caverphone.encode(123);
            fail("Non-String input should throw an exception");
        } catch (EncoderException e) {
            // Exception successfully caught
        }
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Basic functionality test
        assertTrue(caverphone.isCaverphoneEqual("kumar", "kumarr"));

        // Edge cases
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));

        // Exception handling
        // Test for null input
        try {
            caverphone.isCaverphoneEqual(null, "test");
            fail("Null input should throw an exception");
        } catch (NullPointerException e) {
            // Exception successfully caught
        }
    }

}