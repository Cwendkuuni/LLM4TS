import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodeDecode() {
        Caverphone caverphone = new Caverphone();

        String original = "The quick brown fox jumps over the lazy dog";
        String encoded = caverphone.encode(original);
        String decoded = caverphone.decode(Hex.decodeHex(encoded.split(" ")[0].getBytes()));

        Assert.assertEquals(original, decoded);
    }

    @Test
    public void testCaverphoneEncodeEmptyString() {
        Caverphone caverphone = new Caverphone();

        String original = "";
        String encoded = caverphone.encode(original);

        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testCaverphoneEncodeNullString() {
        Caverphone caverphone = new Caverphone();

        String original = null;

        try {
            String encoded = caverphone.encode(original);
            Assert.fail();
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testCaverphoneDecodeInvalidHex() {
        Caverphone caverphone = new Caverphone();

        String encoded = "invalid_hex_string";

        try {
            String decoded = caverphone.decode(Hex.decodeHex(encoded.getBytes()));
            Assert.fail();
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testCaverphoneEncodeDecodeEdgeCases() {
        Caverphone caverphone = new Caverphone();

        String allUppercase = StringUtils.upperCase("The quick brown fox jumps over the lazy dog");
        String allLowercase = StringUtils.toLowerCase("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG");
        String onlyDigits = "1234567890";
        String onlyVowels = "AaEeIiOoUu";
        String empty = "";
        String nullString = null;

        String encodedAllUppercase = caverphone.encode(allUppercase);
        String encodedAllLowercase = caverphone.encode(allLowercase);
        String encodedOnlyDigits = caverphone.encode(onlyDigits);
        String encodedOnlyVowels = caverphone.encode(onlyVowels);
        String encodedEmpty = caverphone.encode(empty);
        String encodedNull = null;

        String decodedAllUppercase = caverphone.decode(Hex.decodeHex(encodedAllUppercase.split(" ")[0].getBytes()));
        String decodedAllLowercase = caverphone.decode(Hex.decodeHex(encodedAllLowercase.split(" ")[0].getBytes()));
        String decodedOnlyDigits = caverphone.decode(Hex.decodeHex(encodedOnlyDigits.split(" ")[0].getBytes()));
        String decodedOnlyVowels = caverphone.decode(Hex.decodeHex(encodedOnlyVowels.split(" ")[0].getBytes()));
        String decodedEmpty = caverphone.decode(Hex.decodeHex(encodedEmpty.split(" ")[0].getBytes()));

        Assert.assertEquals(allUppercase, decodedAllUppercase);
        Assert.assertEquals(allLowercase, decodedAllLowercase);
        Assert.assertEquals(onlyDigits, decodedOnlyDigits);
        Assert.assertEquals(onlyVowels, decodedOnlyVowels);
        Assert.assertEquals(empty, decodedEmpty);

        encodedNull = caverphone.encode(nullString);
        decodedNull = caverphone.decode(Hex.decodeHex(encodedNull.split(" ")[0].getBytes()));

        Assert.assertNull(decodedNull);
    }
}