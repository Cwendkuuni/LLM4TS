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
    public void testCaverphone_WithNullString_ReturnsDefaultCode() {
        String result = caverphone.caverphone(null);

        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithEmptyString_ReturnsDefaultCode() {
        String result = caverphone.caverphone("");

        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_WithValidString_ReturnsCorrectCode() {
        String result = caverphone.caverphone("hello");

        assertEquals("HL11111111", result);
    }

    @Test
    public void testCaverphone_WithSpecialCharacters_ReturnsCodeWithSpecialCharactersRemoved() {
        String result = caverphone.caverphone("h^e#l*l)o?");

        assertEquals("HL11111111", result);
    }

    @Test
    public void testEncode_WithNonStringObject_ThrowsEncoderException() {
        try {
            caverphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // success
        }
    }

    @Test
    public void testEncode_WithStringObject_ReturnsCaverphoneCode() {
        String result = caverphone.encode("hello");

        assertEquals("HL11111111", result);
    }

    @Test
    public void testIsCaverphoneEqual_WithSameStrings_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");

        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithDifferentStrings_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("hello", "world");

        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithNullString_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual(null, "hello");

        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithEmptyStrings_ReturnsFalse() {
        boolean result = caverphone.isCaverphoneEqual("", "");

        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_WithNullStrings_ReturnsTrue() {
        boolean result = caverphone.isCaverphoneEqual(null, null);

        assertTrue(result);
    }

}