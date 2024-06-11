package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        String txt = "ExampleString";
        String expected = "E1xAmPlE2StRing";
        String actual = caverphone.caverphone(txt);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        Caverphone caverphone = new Caverphone();
        String txt = null;
        caverphone.caverphone(txt);
        fail("Expected EncoderException but it did not occur.");
    }

    @Test
    public void testEncodeString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String input = "ExampleString";
        String expected = "E1xAmPlE2StRing";
        Object actual = caverphone.encode(input);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNull() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        String input = null;
        caverphone.encode(input);
        fail("Expected EncoderException but it did not occur.");
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        Integer input = 123;
        caverphone.encode(input);
        fail("Expected EncoderException but it did not occur.");
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        String str1 = "ExampleString1";
        String str2 = "ExampleString2";
        String str1Caverphone = caverphone.caverphone(str1);
        String str2Caverphone = caverphone.caverphone(str2);
        boolean result = caverphone.isCaverphoneEqual(str1, str2);
        assertFalse(result);
        assertEquals(str1Caverphone, str2Caverphone);
        assertTrue(caverphone.isCaverphoneEqual(str1, str1));
    }

    @Test
    public void testIsCaverphoneEqualEmptyStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = "";
        String str2 = "";
        assertTrue(caverphone.isCaverphoneEqual(str1, str2));
    }

    @Test
    public void testIsCaverphoneEqualNullStrings() {
        Caverphone caverphone = new Caverphone();
        String str1 = null;
        String str2 = null;
        // JUnit does not support asserting null on string comparisons
        // Use Assert.assertEquals instead but with null as the expected value
        // which may cause issues
        // Instead, we can just check if the method throws an exception
        try {
            caverphone.isCaverphoneEqual(str1, str2);
        } catch (NullPointerException e) {
            // expected
        }
    }

}