import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class CaverphoneTest {

    private static final Caverphone caverphone = new Caverphone();

    @Test
    public void testEmptyString() {
        String encoded = caverphone.encode("");
        Assert.assertEquals("1111111111", encoded);
    }

    @Test
    public void testNullString() {
        try {
            caverphone.encode(null);
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleString() {
        String input = "hello";
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testMixedCaseString() {
        String input = "HeLLo WOrLd";
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testNonAlphabeticString() {
        String input = "12345";
        try {
            caverphone.encode(input);
            Assert.fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testLongString() {
        String input = StringUtils.repeat("a", 100);
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testStartingWithVowels() {
        String input = "eat";
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testStartingWithCough() {
        String input = "cough";
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals("cou2f", decoded);
    }

    @Test
    public void testEndingWithE() {
        String input = "enough";
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    @Test
    public void testDifferentCasing() {
        String input1 = "HELLO";
        String input2 = "hello";
        String encoded1 = caverphone.encode(input1);
        String encoded2 = caverphone.encode(input2);
        Assert.assertFalse(encoded1.equals(encoded2));
    }

    @Test
    public void testDecodeInvalidCaverphone() {
        String input = "1111111111111"; // longer than expected length
        try {
            caverphone.decode(input);
            Assert.fail("Expected DecoderException");
        } catch (DecoderException e) {
            // Expected
        }
    }

    @Test
    public void testDecodeRandomString() {
        String input = generateRandomString();
        String encoded = caverphone.encode(input);
        String decoded = caverphone.decode(encoded);
        Assert.assertEquals(input, decoded);
    }

    private String generateRandomString() {
        Random random = new Random();
        int length = random.nextInt(50) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomChar = random.nextInt(26) + 'a';
            stringBuilder.append((char) randomChar);
        }
        return stringBuilder.toString();
    }

}