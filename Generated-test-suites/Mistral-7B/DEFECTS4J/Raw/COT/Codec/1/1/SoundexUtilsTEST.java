package org.apache.commons.codec.language.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Locale;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Test class for SoundexUtils methods.
 *
 * @author YourName
 */
public class SoundexUtilsTest {

    private static final StringTestHelper STRING_TEST_HELPER = new StringTestHelper();

    @Test
    public void testClean() {
        // Test empty string
        assertEquals("", SoundexUtils.clean(""));
        // Test null string
        assertEquals("", SoundexUtils.clean(null));
        // Test clean up single letter
        assertEquals("A", SoundexUtils.clean("A"));
        // Test clean up multiple letters
        assertEquals("BC", SoundexUtils.clean("bC"));
        // Test clean up multiple letters with spaces
        assertEquals("AB CD EF", SoundexUtils.clean("a b c d e f"));
        // Test clean up multiple letters with special characters
        assertEquals("AB CD EF GH", SoundexUtils.clean("a b c d e f g h i!"));
    }

    @Test
    public void testDifferenceEncoded() {
        StringEncoder encoder = STRING_TEST_HELPER.getSoundexEncoder();

        // Test difference encoded with null strings
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        // Test difference encoded with empty strings
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        // Test difference encoded with same strings
        String soundexString1 = encoder.encode("Apple");
        String soundexString2 = encoder.encode("Apple");
        assertEquals(4, SoundexUtils.differenceEncoded(soundexString1, soundexString2));
        // Test difference encoded with different strings
        soundexString1 = encoder.encode("Apple");
        String soundexString3 = encoder.encode("Orange");
        assertEquals(0, SoundexUtils.differenceEncoded(soundexString1, soundexString3));
        // Test difference encoded with strings with different lengths
        soundexString1 = encoder.encode("Apple");
        String soundexString4 = encoder.encode("Banana");
        assertEquals(0, SoundexUtils.differenceEncoded(soundexString1, soundexString4));
        // Test difference encoded with strings having different codecs
        String refinedSoundexEncoder = STRING_TEST_HELPER.getRefinedSoundexEncoder().encode("Apple");
        assertEquals(1, SoundexUtils.differenceEncoded(soundexString1, refinedSoundexEncoder));
    }

    @Test
    public void testDifference() {
        StringEncoder encoder = STRING_TEST_HELPER.getSoundexEncoder();

        // Test difference with null strings
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, "Apple"));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, "Apple", null));
        assertThrows(NullPointerException.class, () -> SoundexUtils.difference(encoder, null, null));
        // Test difference with empty strings
        assertEquals(0, SoundexUtils.difference(encoder, "", "Apple"));
        assertEquals(0, SoundexUtils.difference(encoder, "Apple", ""));
        // Test difference with same strings
        String soundexString1 = encoder.encode("Apple");
        String soundexString2 = encoder.encode("Apple");
        assertEquals(4, SoundexUtils.difference(encoder, soundexString1, soundexString2));
        // Test difference with different strings
        soundexString1 = encoder.encode("Apple");
        String soundexString3 = encoder.encode("Orange");
        assertEquals(0, SoundexUtils.difference(encoder, soundexString1, soundexString3));
        // Test difference with strings having different lengths
        soundexString1 = encoder.encode("Apple");
        String soundexString4 = encoder.encode("Banana");
        assertEquals(0, SoundexUtils.difference(encoder, soundexString1, soundexString4));
        // Test difference with strings having different codecs
        soundexString1 = encoder.encode("Apple");
        String refinedSoundexString = STRING_TEST_HELPER.getRefinedSoundexEncoder().encode("Apple");
        assertEquals(1, SoundexUtils.difference(encoder, soundexString1, refinedSoundexString));
    }

}