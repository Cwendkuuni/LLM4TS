import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    // Test cases for caverphone(String txt) method

    @Test
    public void testCaverphone_typicalCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("hello");
        assertEquals("hA11111111", result);
    }

    @Test
    public void testCaverphone_emptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testCaverphone_nullString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    // Test cases for isCaverphoneEqual(String str1, String str2) method

    @Test
    public void testIsCaverphoneEqual_sameStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "hello");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_differentStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result);
    }

    @Test
    public void testIsCaverphoneEqual_emptyStrings() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual("", "");
        assertTrue(result);
    }

    @Test
    public void testIsCaverphoneEqual_nullString() {
        Caverphone caverphone = new Caverphone();
        boolean result = caverphone.isCaverphoneEqual(null, null);
        assertTrue(result);
    }

    // Test cases for encode(String pString) method

    @Test
    public void testEncode_typicalCase() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("hello");
        assertEquals("hA11111111", result);
    }

    @Test
    public void testEncode_emptyString() {
        Caverphone caverphone = new Caverphone();
        String result = caverphone.encode("");
        assertEquals("1111111111", result);
    }

    @Test(expected = EncoderException.class)
    public void testEncode_nonStringObject() {
        Caverphone caverphone = new Caverphone();
        Object obj = new Object();
        caverphone.encode(obj);
    }

}