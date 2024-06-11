import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testEmptyString() {
        String emptyString = "";
        String caverphoneCode = caverphone.caverphone(emptyString);
        String expectedCaverphoneCode = "1111111111";
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test
    public void testNullString() {
        String nullString = null;
        String caverphoneCode = "";
        try {
            caverphoneCode = caverphone.caverphone(nullString);
        } catch (EncoderException e) {
            caverphoneCode = e.getMessage();
        }
        String expectedCaverphoneCode = "1111111111";
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test
    public void testSingleCharacterString() {
        String singleCharacterString = "a";
        String caverphoneCode = caverphone.caverphone(singleCharacterString);
        String expectedCaverphoneCode = "A";
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test
    public void testUpperCaseString() {
        String upperCaseString = "Hello";
        String caverphoneCode = caverphone.caverphone(upperCaseString);
        String expectedCaverphoneCode = "H2L3L2O";
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test
    public void testStringWithDigits() {
        String stringWithDigits = "The quick brown fox jumps over the lazy dog 12345";
        String caverphoneCode = caverphone.caverphone(stringWithDigits);
        String expectedCaverphoneCode = "Th3 q1ck br0wn f6x j2umps 0v3r th3 l4zy d0g";
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test
    public void testLongString() {
        String longString = "The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345 The quick brown fox jumps over the lazy dog 12345";
        String caverphoneCode = caverphone.caverphone(longString);
        String expectedCaverphoneCode = StringUtils.repeat("Th3 q1ck br0wn f6x j2umps 0v3r th3 l4zy d0g ", 11);
        assertEquals(expectedCaverphoneCode, caverphoneCode);
    }

    @Test(expected = EncoderException.class)
    public void testInvalidCharacter() {
        String invalidCharacterString = "The quick brown fox jumps over the lazy dog #%^&";
        caverphone.caverphone(invalidCharacterString);
    }

    @Test
    public void testEncodeDecode() {
        String originalString = "The quick brown fox jumps over the lazy dog 12345";
        String caverphoneCode = caverphone.caverphone(originalString);
        String decodedString = (String) caverphone.decode(caverphoneCode);
        assertEquals(originalString, decodedString);
    }

    @Test
    public void testDecodeInvalidCaverphoneCode() {
        String invalidCaverphoneCode = "Th3 q1ck br0wn f6x j2umps 0v3r th3 l4zy d0g 123456";
        try {
            caverphone.decode(invalidCaverphoneCode);
            fail();
        } catch (DecoderException e) {
            // Expected
        }
    }
}