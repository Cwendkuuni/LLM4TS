import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Caverphone.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        String result = caverphone("");
        String expected = "1111111111";
        assertEquals(expected, result);
    }

    @Test
    public void testNullString() {
        try {
            caverphone(null);
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleString() {
        String result = caverphone("hello");
        String expected = "h3l3l3o3";
        assertEquals(expected, result);
    }

    @Test
    public void testUpperCaseString() {
        String result = caverphone("HELLO");
        String expected = "h3l3l3o3";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithDigits() {
        String result = caverphone("hello123");
        String expected = "h3l3l3o3123";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithSpecialCharacters() {
        String result = caverphone("hello@world");
        String expected = "h3l3l3o3e2w1r1d1";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithWhiteSpace() {
        String result = caverphone("hello world");
        String expected = "h3l3l3o3e2l2w1r1d1";
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testStringWithLongerThanTenChars() {
        String result = caverphone("longerStringThanTenChars");
        fail("Should have thrown an EncoderException");
    }

    @Test
    public void testStringWithLeadingZero() {
        String result = caverphone("0hello");
        String expected = "h3l3l3o3";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithTrailingZero() {
        String result = caverphone("hello0");
        String expected = "h3l3l3o3";
        assertEquals(expected, result);
    }

    @Test
    public void testStringWithMultipleLeadingAndTrailingZeros() {
        String result = caverphone("0hello0");
        String expected = "h3l3l3o3";
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeAndDecode() {
        String original = "hello world";
        String encoded = caverphone(original);
        String decoded = caverphone(encoded);
        assertTrue(isCaverphoneEqual(original, decoded));
    }

    @Test
    public void testEncodeAndDecodeWithSpecialCharacters() {
        String original = "hello@world";
        String encoded = caverphone(original);
        String decoded = caverphone(encoded);
        assertTrue(isCaverphoneEqual(original, decoded));
    }

    @Test
    public void testEncodeAndDecodeWithDigits() {
        String original = "hello123";
        String encoded = caverphone(original);
        String decoded = caverphone(encoded);
        assertTrue(isCaverphoneEqual(original, decoded));
    }

    @Test
    public void testEncodeAndDecodeWithLongerStrings() {
        String original = "longerStringThanTenChars";
        String encoded = caverphone(original);
        String decoded = caverphone(encoded);
        fail("Should have thrown an EncoderException");
    }

    @Test
    public void testEncodeAndDecodeWithEmptyStrings() {
        String original = "";
        String encoded = caverphone(original);
        String decoded = caverphone(encoded);
        assertEquals(original, decoded);
    }

    @Test
    public void testEncodeAndDecodeWithNullString() {
        try {
            String original = null;
            String encoded = caverphone(original);
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected
        }

        try {
            String decoded = caverphone(null);
            fail("Should have thrown an EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
}