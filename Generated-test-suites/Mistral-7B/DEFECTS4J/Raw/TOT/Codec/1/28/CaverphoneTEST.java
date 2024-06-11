import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Caverphone class.
 */
public class CaverphoneTest {

    /**
     * Test encode method with empty string.
     */
    @Test
    public void testEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        String expected = "1111111111";
        String actual = caverphone.encode("");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with single character string.
     */
    @Test
    public void testEncodeSingleCharacterString() {
        Caverphone caverphone = new Caverphone();
        String expected = "A";
        String actual = caverphone.encode("a");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with lowercase string.
     */
    @Test
    public void testEncodeLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String input = "hello";
        String expected = "h2l2o2";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with uppercase string.
     */
    @Test
    public void testEncodeUpperCaseString() {
        Caverphone caverphone = new Caverphone();
        String input = "HELLO";
        String expected = "H2L2O2";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with string containing digits.
     */
    @Test
    public void testEncodeStringWithDigits() {
        Caverphone caverphone = new Caverphone();
        String input = "12345";
        String expected = "11111";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with string containing special characters.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeStringWithSpecialCharacters() {
        Caverphone caverphone = new Caverphone();
        String input = "!@#$%^&*()_+=";
        caverphone.encode(input);
    }

    /**
     * Test encode method with string containing whitespace.
     */
    @Test
    public void testEncodeStringWithWhitespace() {
        Caverphone caverphone = new Caverphone();
        String input = " ca vers ho ne ";
        String expected = "c2 a2 v2 e2 r2 s2 h2 n2 e2";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test encode method with string containing multiple words.
     */
    @Test
    public void testEncodeStringWithMultipleWords() {
        Caverphone caverphone = new Caverphone();
        String input = "this is a test";
        String expected = "t2 h2 i2 s2 i2 a2 t2 e2 s2 t2";
        String actual = caverphone.encode(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with empty string.
     */
    @Test
    public void testDecodeEmptyString() {
        Caverphone caverphone = new Caverphone();
        String encoded = "1111111111";
        String expected = "";
        String actual = caverphone.decode(encoded);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with single character string.
     */
    @Test
    public void testDecodeSingleCharacterString() {
        Caverphone caverphone = new Caverphone();
        String encoded = "A";
        String expected = "a";
        String actual = caverphone.decode(encoded);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with lowercase string.
     */
    @Test
    public void testDecodeLowerCaseString() {
        Caverphone caverphone = new Caverphone();
        String encoded = "h2l2o2";
        String expected = "hello";
        String actual = caverphone.decode(encoded);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with uppercase string.
     */
    @Test
    public void testDecodeUpperCaseString() {
        Caverphone caverphone = new Caverphone();
        String encoded = "H2L2O2";
        String expected = "HELLO";
        String actual = caverphone.decode(encoded);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with string containing digits.
     */
    @Test
    public void testDecodeStringWithDigits() {
        Caverphone caverphone = new Caverphone();
        String encoded = "11111";
        String expected = "12345";
        String actual = caverphone.decode(encoded);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test decode method with invalid encoding.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidEncoding() {
        Caverphone caverphone = new Caverphone();
        String encoded = "I3amnotavalidencoding";
        caverphone.decode(encoded);
    }

    /**
     * Test isCaverphoneEqual method with equal strings.
     */
    @Test
    public void testIsCaverphoneEqualEqualStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "hello";
        boolean expected = true;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test isCaverphoneEqual method with different strings.
     */
    @Test
    public void testIsCaverphoneEqualDifferentStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "hello";
        String str2 = "world";
        boolean expected = false;
        boolean actual = caverphone.isCaverphoneEqual(str1, str2);
        Assert.assertEquals(expected, actual);
    }
}