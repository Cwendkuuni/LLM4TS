package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class CaverphoneTest {
    private static final String TEST_CAVPHONE_1 = "1154675327";
    private static final String TEST_CAVPHONE_2 = "2135627113";

    @Test
    public void testCaverphoneEncode() {
        Caverphone caverphone = new Caverphone();

        String testString = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String encodedCaverphone = caverphone.caverphone(testString);
        assertEquals(TEST_CAVPHONE_1, encodedCaverphone);
    }

    @Test
    public void testCaverphoneEncodeNullInput() {
        Caverphone caverphone = new Caverphone();

        assertNull(caverphone.caverphone(null));
    }

    @Test
    public void testCaverphoneEncodeEmptyInput() {
        Caverphone caverphone = new Caverphone();

        String encodedCaverphone = caverphone.caverphone("");
        assertEquals(TEST_CAVPHONE_1, encodedCaverphone);
    }

    @Test
    public void testCaverphoneEncodeSingleChar() {
        Caverphone caverphone = new Caverphone();

        String testString = "a";
        String encodedCaverphone = caverphone.caverphone(testString);
        assertArrayEquals(new String[]{"A", "1"}, encodedCaverphone.split(""));
    }

    @Test
    public void testCaverphoneEncodeMixedString() {
        Caverphone caverphone = new Caverphone();

        String testString = "The quick brown fox jumps over the lazy dog";
        String encodedCaverphone = caverphone.caverphone(testString);
        assertEquals(TEST_CAVPHONE_1, encodedCaverphone);
    }

    @Test
    public void testCaverphoneEncodeTwoStrings() {
        Caverphone caverphone = new Caverphone();

        String str1 = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String str2 = "the quick brown fox jumps over the lazy dog";
        String encodedCaverphone1 = caverphone.caverphone(str1);
        String encodedCaverphone2 = caverphone.caverphone(str2);

        assertEquals(encodedCaverphone1, encodedCaverphone2);
    }

    @Test
    public void testCaverphoneEncodeDifferentCases() {
        Caverphone caverphone = new Caverphone();

        String testString1 = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String testString2 = "tHE qUICK bROWN fox jumps over the lazy DOG";
        String encodedCaverphone1 = caverphone.caverphone(testString1);
        String encodedCaverphone2 = caverphone.caverphone(testString2);

        assertEquals(encodedCaverphone1, encodedCaverphone2);
    }

    @Test
    public void testCaverphoneEncodeEncoderAndDecode() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        String testString = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String encodedCaverphone = caverphone.caverphone(testString);
        String decodedCaverphone = (String) caverphone.encode(encodedCaverphone);

        assertEquals(testString, decodedCaverphone);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        String str1 = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String str2 = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String str3 = "The quick brown fox jumps over the lazy dog";

        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
        assertTrue(caverphone.isCaverphoneEqual(str1, str3));
        assertFalse(caverphone.isCaverphoneEqual(str1, "The Quick Brown Fox Jumps Over The Lazy Cat"));
    }

    @Test
    public void testIsCaverphoneEqualDifferentCases() {
        Caverphone caverphone = new Caverphone();

        String str1 = "The Quick Brown Fox Jumps Over The Lazy Dog";
        String str2 = "tHE qUICK bROWN fox jumps over the lazy dog";

        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }
}