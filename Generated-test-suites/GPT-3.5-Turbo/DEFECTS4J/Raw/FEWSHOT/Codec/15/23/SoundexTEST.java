
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();

        // Test null input
        assertNull(soundex.encode(null));

        // Test empty string input
        assertEquals("0000", soundex.encode(""));

        // Test input with non-alphabetic characters
        assertEquals("C416", soundex.encode("C4.16!"));

        // Test input with upper-case characters
        assertEquals("D400", soundex.encode("DOGS"));

        // Test input with lower-case characters
        assertEquals("D400", soundex.encode("dogs"));

        // Test input with spaces
        assertEquals("D400", soundex.encode(" d o g s "));

        // Test input with numbers
        assertEquals("D400", soundex.encode("D0G5"));

        // Test input with special characters
        assertEquals("D400", soundex.encode("D@GS"));

        // Test input with non-English characters
        assertEquals("V532", soundex.encode("VÜtaleä"));

        // Test input with non-English characters and spaces
        assertEquals("V532", soundex.encode(" v ü t a l e ä "));

        // Test input with non-English characters and numbers
        assertEquals("V532", soundex.encode("VÜt4leä"));

        // Test input with non-English characters and special characters
        assertEquals("V532", soundex.encode("V@täleh"));

        // Test input with maximum length
        assertEquals("D441", soundex.encode("DOGS DOGS DOGS DOGS"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();

        // Test null input
        assertEquals(0, soundex.difference(null, null));

        // Test empty string input
        assertEquals(4, soundex.difference("", ""));

        // Test input with same soundex code
        assertEquals(4, soundex.difference("DOGS", "DOGS"));

        // Test input with one character difference
        assertEquals(3, soundex.difference("DOGS", "BOGS"));

        // Test input with two character difference
        assertEquals(2, soundex.difference("DOGS", "BATS"));

        // Test input with three character difference
        assertEquals(1, soundex.difference("DOGS", "BATH"));

        // Test input with four character difference
        assertEquals(0, soundex.difference("DOGS", "CATS"));
    }
}
