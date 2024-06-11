import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    private StringEncoder caverphoneEncoder = new Caverphone();
    private StringDecoder caverphoneDecoder = new Caverphone();

    /**
     * Test encoding and decoding of valid strings using the Caverphone algorithm.
     */
    @Test
    public void testEncodeAndDecode() {
        String original = "The quick brown fox jumps over the lazy dog";
        String caverphoneCode = caverphoneEncoder.encode(original);
        String decoded = caverphoneDecoder.decode(caverphoneCode);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding of an empty string using the Caverphone algorithm.
     */
    @Test
    public void testEncodeEmptyString() {
        String input = "";
        String output = caverphoneEncoder.encode(input);
        Assert.assertEquals("1111111111", output);
    }

    /**
     * Test encoding of a null string using the Caverphone algorithm.
     */
    @Test(expected = NullPointerException.class)
    public void testEncodeNullString() {
        String input = null;
        caverphoneEncoder.encode(input);
    }

    /**
     * Test decoding of a caverphone code that doesn't correspond to a valid string.
     */
    @Test(expected = DecoderException.class)
    public void testDecodeInvalidCaverphoneCode() {
        String caverphoneCode = "1122331111";
        caverphoneDecoder.decode(caverphoneCode);
    }

    /**
     * Test encoding and decoding of a string containing only digits using the Caverphone algorithm.
     */
    @Test
    public void testEncodeDecodeDigits() {
        String original = "1234567890";
        String caverphoneCode = caverphoneEncoder.encode(original);
        String decoded = caverphoneDecoder.decode(caverphoneCode);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding and decoding of a string containing only uppercase letters using the Caverphone algorithm.
     */
    @Test
    public void testEncodeDecodeUppercase() {
        String original = "HELLOTHERE";
        String caverphoneCode = caverphoneEncoder.encode(original);
        String decoded = caverphoneDecoder.decode(caverphoneCode);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding and decoding of a string containing only lowercase letters using the Caverphone algorithm.
     */
    @Test
    public void testEncodeDecodeLowercase() {
        String original = "hellothere";
        String caverphoneCode = caverphoneEncoder.encode(original);
        String decoded = caverphoneDecoder.decode(caverphoneCode);
        Assert.assertEquals(original, decoded);
    }

    /**
     * Test encoding of a string containing special characters using the Caverphone algorithm.
     */
    @Test
    public void testEncodeSpecialCharacters() {
        String original = "The quick brown fox! jumps over the lazy dog#";
        String caverphoneCode = caverphoneEncoder.encode(original);
        // The decoding part is not possible due to the special characters,
        // since the Caverphone algorithm only deals with alphabets.
    }
}