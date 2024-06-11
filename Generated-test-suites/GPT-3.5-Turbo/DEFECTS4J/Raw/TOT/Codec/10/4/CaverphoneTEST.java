import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    // Test for caverphone() method

    // Typical use case: Valid input string
    @Test
    public void testCaverphone_typical() {
        String expected = "3511111111";
        String actual = caverphone.caverphone("example");
        assertEquals(expected, actual);
    }

    // Edge case: Empty string
    @Test
    public void testCaverphone_emptyString() {
        String expected = "1111111111";
        String actual = caverphone.caverphone("");
        assertEquals(expected, actual);
    }

    // Error scenario: Null input string
    @Test
    public void testCaverphone_nullString() {
        String expected = "1111111111";
        String actual = caverphone.caverphone(null);
        assertEquals(expected, actual);
    }

    // Test for encode(Object) method

    // Typical use case: Valid input object of type String
    @Test
    public void testEncode_typicalObject() throws EncoderException {
        String expected = "3511111111";
        Object object = "example";
        String actual = (String) caverphone.encode(object);
        assertEquals(expected, actual);
    }

    // Error scenario: Invalid input object of type Integer
    @Test(expected = EncoderException.class)
    public void testEncode_invalidObject() throws EncoderException {
        Object object = 123;
        caverphone.encode(object);
    }

    // Test for encode(String) method

    // Typical use case: Valid input string
    @Test
    public void testEncode_typicalString() {
        String expected = "3511111111";
        String actual = caverphone.encode("example");
        assertEquals(expected, actual);
    }

    // Test for isCaverphoneEqual() method

    // Typical use case: Caverphones of two strings are equal
    @Test
    public void testIsCaverphoneEqual_equalCaverphones() {
        String str1 = "example";
        String str2 = "exampl";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    // Typical use case: Caverphones of two strings are not equal
    @Test
    public void testIsCaverphoneEqual_notEqualCaverphones() {
        String str1 = "example1";
        String str2 = "example2";
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }

    // Error scenario: Null input string
    @Test
    public void testIsCaverphoneEqual_nullString() {
        String str1 = "example";
        String str2 = null;
        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
}