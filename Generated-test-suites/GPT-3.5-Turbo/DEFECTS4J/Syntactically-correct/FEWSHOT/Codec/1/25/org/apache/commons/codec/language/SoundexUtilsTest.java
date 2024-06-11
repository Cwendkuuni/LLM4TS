package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test null input
        assertNull(SoundexUtils.clean(null));

        // Test empty input
        assertEquals("", SoundexUtils.clean(""));

        // Test input with no letters
        assertEquals("", SoundexUtils.clean("123"));

        // Test input with uppercase letters
        assertEquals("HELLO", SoundexUtils.clean("Hello123"));

        // Test input with mixed uppercase and lowercase letters
        assertEquals("WORLD", SoundexUtils.clean("w0rlD"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();

        // Test null input
        assertEquals(0, SoundexUtils.difference(encoder, null, null));

        String s1 = "Hello123";
        String s2 = "hello321";

        // Test input with different lengths
        assertEquals(1, SoundexUtils.difference(encoder, s1, "hello"));
        assertEquals(2, SoundexUtils.difference(encoder, s1, "Hello!"));
        assertEquals(3, SoundexUtils.difference(encoder, s1, "HeLLo!1"));
        assertEquals(4, SoundexUtils.difference(encoder, s1, "HELLOWORLD"));

        // Test input with same lengths
        assertEquals(4, SoundexUtils.difference(encoder, s1, s2));
    }

    @Test
    public void testDifferenceEncoded() {
        // Test null input
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        String es1 = "H400";
        String es2 = "h401";

        // Test input with different lengths
        assertEquals(1, SoundexUtils.differenceEncoded(es1, "h4"));
        assertEquals(2, SoundexUtils.differenceEncoded(es1, "h41"));
        assertEquals(3, SoundexUtils.differenceEncoded(es1, "h402"));
        assertEquals(4, SoundexUtils.differenceEncoded(es1, "H4001"));

        // Test input with same lengths
        assertEquals(4, SoundexUtils.differenceEncoded(es1, es2));
    }

}