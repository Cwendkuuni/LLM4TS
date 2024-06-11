package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundexWithUS_ENGLISH_MAPPING() {
        Soundex soundex = Soundex.US_ENGLISH;

        // Test encoding with basic English words
        assertEquals("K250", soundex.encode("cat"));
        assertEquals("K320", soundex.encode("kit"));
        assertEquals("K150", soundex.encode("bat"));
        assertEquals("K230", soundex.encode("cut"));
        assertEquals("K350", soundex.encode("but"));
        assertEquals("K500", soundex.encode("about"));

        // Test encoding with words containing multiple consonants
        assertEquals("R252", soundex.encode("rhythm"));
        assertEquals("R256", soundex.encode("apprehension"));

        // Test encoding with words containing multiple vowels
        assertEquals("A200", soundex.encode("aardvark"));
        assertEquals("A220", soundex.encode("oceana"));
        assertEquals("A230", soundex.encode("canaan"));

        // Test encoding with special characters
        assertEquals("N500", soundex.encode("anesthesia"));
        assertEquals("N510", soundex.encode("anesthetic"));
        assertEquals("N520", soundex.encode("anesthetist"));

        // Test encoding with empty and null strings
        assertNull(soundex.encode(null));
        assertEquals("", soundex.encode(""));

        // Test encoding with whitespaces
        assertEquals("K000", soundex.encode(" "));
        assertEquals("K250", soundex.encode("cat "));
        assertEquals("K230", soundex.encode(" cut "));

        // Test encoding with multiple words
        assertEquals("K2500K320", soundex.encode("cat kit"));
        assertEquals("K2302K350", soundex.encode("cut but"));
        assertEquals("K2500K2302K350", soundex.encode("cat cut but"));

        // Test difference method
        assertEquals(3, soundex.difference("cat", "cut"));
        assertEquals(0, soundex.difference("cat", "bat"));
        assertEquals(4, soundex.difference("cat", "batat"));
    }

    @Test(expected = EncoderException.class)
    public void testIllegalArgumentException() {
        Soundex soundex = new Soundex(new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'});
        soundex.encode("äöé");
    }

    @Test
    public void testSoundexWithCustomMapping() {
        Soundex soundex = new Soundex(new String("123456789012345").toCharArray());
        assertEquals("1234", soundex.encode("apple"));
        assertEquals("1234", soundex.encode("application"));
        assertEquals("1234", soundex.encode("application_form"));
    }
}