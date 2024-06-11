import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone;

    @Before
    public void setUp() {
        caverphone = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        // Typical use case
        assertEquals("1111111111", caverphone.caverphone(null));

        // Edge cases
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("1111111111", caverphone.caverphone(" "));
        assertEquals("1111111111", caverphone.caverphone("123"));

        // Error scenarios
        assertEquals("kf2a", caverphone.caverphone("Caverphone"));
        assertEquals("3kh3stmas", caverphone.caverphone("Christmas"));
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        // Typical use case
        assertEquals("1111111111", caverphone.encode(null));

        // Edge case
        assertEquals("kf2a", caverphone.encode("Caverphone"));

        // Error scenario
        try {
            caverphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode_String() {
        // Typical use case
        assertEquals("1111111111", caverphone.encode(null));

        // Edge case
        assertEquals("kf2a", caverphone.encode("Caverphone"));

        // Error scenario
        assertEquals("1111111111", caverphone.encode(123));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertFalse(caverphone.isCaverphoneEqual(null, ""));
        assertFalse(caverphone.isCaverphoneEqual("", " "));
        assertTrue(caverphone.isCaverphoneEqual("123", "123"));
        assertTrue(caverphone.isCaverphoneEqual("Caverphone", "kf2a"));
        assertFalse(caverphone.isCaverphoneEqual("Caverphone", "Christmas"));
    }

}