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
    public void testCaverphone_withNull_returns1111111111() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_withEmptyString_returns1111111111() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_withLowercaseString_returnsCaverphoneCode() {
        String result = caverphone.caverphone("example");
        assertEquals("3XAMPL111", result);
    }

    // Edge cases

    @Test
    public void testCaverphone_withNumbersInString_returnsCaverphoneCode() {
        String result = caverphone.caverphone("1example2");
        assertEquals("3XAMPL111", result);
    }

    @Test
    public void testCaverphone_withSpecialCharactersInString_returnsCaverphoneCode() {
        String result = caverphone.caverphone("@ex#am$ple!");
        assertEquals("3XAMPL111", result);
    }

    // Error scenarios

    @Test
    public void testCaverphone_withInvalidStartOptions_returnsCaverphoneCode() {
        String result = caverphone.caverphone("ncough");
        assertEquals("NUFF", result);
    }

    @Test
    public void testEncode_withNonStringObject_throwsEncoderException() {
        Object object = new Object();
        assertThrows(EncoderException.class, () -> {
            caverphone.encode(object);
        });
    }

    @Test
    public void testEncode_withValidStringObject_returnsCaverphoneCode() throws EncoderException {
        String result = (String) caverphone.encode("example");
        assertEquals("3XAMPL111", result);
    }

    @Test
    public void testEncode_withLowercaseString_returnsCaverphoneCode() {
        String result = caverphone.encode("example");
        assertEquals("3XAMPL111", result);
    }

    @Test
    public void testIsCaverphoneEqual_withIdenticalStrings_returnsTrue() {
        String str1 = "example";
        String str2 = "example";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_withDifferentStrings_returnsFalse() {
        String str1 = "example";
        String str2 = "example2";
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
    }

}