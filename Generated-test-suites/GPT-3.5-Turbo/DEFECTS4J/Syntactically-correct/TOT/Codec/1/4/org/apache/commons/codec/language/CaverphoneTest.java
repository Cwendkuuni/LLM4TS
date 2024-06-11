import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone_NullInput_Returns1111111111() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_EmptyInput_Returns1111111111() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_OnlyLowerCaseLetters_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("example");
        assertEquals("XMP1L", result);
    }

    @Test
    public void testCaverphone_OnlyUpperCaseLetters_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("EXAMPLE");
        assertEquals("XMP1L", result);
    }

    @Test
    public void testCaverphone_MixedCaseLetters_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("ExAmPlE");
        assertEquals("XMP1L", result);
    }

    @Test
    public void testCaverphone_WithSpecialCharacters_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("ex@mple");
        assertEquals("XMP1L", result);
    }

    @Test
    public void testCaverphone_WithStartOptions_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("troughough");
        assertEquals("TRF", result);
    }

    @Test
    public void testCaverphone_WithReplacements_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("cqv");
        assertEquals("K2", result);
    }

    @Test
    public void testEncode_ValidString_ReturnsCaverphoneCode() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("example");
        assertEquals("XMP1L", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_InvalidInput_ExceptionThrown() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Object result = caverphone.encode(123);
    }

    @Test
    public void testIsCaverphoneEqual_IdenticalStrings_ReturnsTrue() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", "example");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_DifferentStrings_ReturnsFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", "different");
        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_NullString_ReturnsFalse() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("example", null);
        assertFalse(result);
    }
}