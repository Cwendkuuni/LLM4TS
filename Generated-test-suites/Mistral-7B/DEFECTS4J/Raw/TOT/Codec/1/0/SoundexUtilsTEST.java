package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "aBc123dEf456gH";
        String expected = "ABCDEF";
        String cleaned = SoundexUtils.clean(input);
        Assert.assertEquals(expected, cleaned);

        // Test null input
        Assert.assertEquals("", SoundexUtils.clean(null));

        // Test empty string input
        Assert.assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int diff = SoundexUtils.difference(encoder, str1, str2);
        Assert.assertEquals(2, diff);

        // Test identical strings
        String str11 = "Apple";
        String str22 = "Apple";
        int diff11 = SoundexUtils.difference(encoder, str11, str22);
        Assert.assertEquals(4, diff11);

        // Test null input
        String str3 = null;
        try {
            SoundexUtils.difference(encoder, str3, "Apple");
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test empty string input
        String str4 = "";
        try {
            SoundexUtils.difference(encoder, "Apple", str4);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundex();
        String str1 = "Apple";
        String str2 = "Orange";
        int diff = SoundexUtils.difference(encoder, str1, str2);
        Assert.assertEquals(3, diff);

        // Test identical strings
        String str11 = "Apple";
        String str22 = "Apple";
        int diff11 = SoundexUtils.difference(encoder, str11, str22);
        Assert.assertEquals(5, diff11);

        // Test null input
        String str3 = null;
        try {
            SoundexUtils.difference(encoder, str3, "Apple");
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test empty string input
        String str4 = "";
        try {
            SoundexUtils.difference(encoder, "Apple", str4);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDifferenceEncodedMethod() {
        String es1 = "A123";
        String es2 = "B456";
        int diff = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(0, diff);

        // Test identical encoded strings
        String es11 = "A123";
        String es22 = "A123";
        int diff11 = SoundexUtils.differenceEncoded(es11, es22);
        Assert.assertEquals(4, diff11);

        // Test null input
        String es3 = null;
        try {
            SoundexUtils.differenceEncoded(es3, "A123");
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test empty string input
        String es4 = "";
        try {
            SoundexUtils.differenceEncoded("A123", es4);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}