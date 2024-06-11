package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifferenceUSEnglish() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals(0, soundex.difference("cat", "bat"));
        assertEquals(1, soundex.difference("cat", "cap"));
        assertEquals(2, soundex.difference("cat", "cut"));
        assertEquals(2, soundex.difference("bat", "cat"));
        assertEquals(3, soundex.difference("bat", "bat"));
        assertEquals(3, soundex.difference("cat", "cat"));
        assertEquals(4, soundex.difference("cat", "cat"));

        // Test with empty strings
        assertEquals(0, soundex.difference("", ""));
        assertEquals(0, soundex.difference("", "cat"));
        assertEquals(0, soundex.difference("cat", ""));
    }

    @Test
    public void testEncodeUSEnglish() throws EncoderException {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("K123", soundex.encode("cat"));
        assertEquals("K323", soundex.encode("bat"));
        assertEquals("K133", soundex.encode("cut"));
        assertEquals("K123", soundex.encode("CAP"));
        assertEquals("K123", soundex.encode("CAT"));
        assertEquals("K133", soundex.encode("CUT"));
        assertEquals("K000", soundex.encode(""));

        // Test encoding special characters
        assertEquals("A261", soundex.encode("Apple"));
        assertEquals("A261", soundex.encode("Apple's"));
        assertEquals("A261", soundex.encode("Apple's delicious"));
        assertEquals("A261", soundex.encode("Apple\u00B1s delicious"));
        assertEquals("A261", soundex.encode("Apple’s delicious"));
        assertEquals("A261", soundex.encode("Apple’s delicious"));
        assertEquals("A261", soundex.encode("Apple’s delicious"));
    }

    @Test
    public void testEncodeCustomMapping() throws EncoderException {
        Soundex soundex = new Soundex(US_ENGLISH_MAPPING_STRING.substring(0, 5).toCharArray());
        assertEquals("B123", soundex.encode("bat"));
        assertEquals("B123", soundex.encode("cat"));
        assertEquals("B123", soundex.encode("cut"));
        assertEquals("B133", soundex.encode("bat"));
        assertEquals("B133", soundex.encode("cat"));
        assertEquals("B133", soundex.encode("cut"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }
}