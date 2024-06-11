package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testEncode_CharacterSet_US() {
        Soundex soundex = new Soundex();

        // Typical use cases
        assertEquals("A234", soundex.encode("Awesome"));
        assertEquals("L530", soundex.encode("Lion"));
        assertEquals("R200", soundex.encode("Rain"));

        // Edge cases
        assertEquals("C600", soundex.encode("Cabbage"));
        assertEquals("C000", soundex.encode("ccc")); // No encoded character is mapped
        
        // Error scenarios
        try {
            soundex.encode(null); // Null input
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Exception caught, test passed
        }
    }

    @Test
    public void testDifference_CharacterSet_US() throws EncoderException {
        Soundex soundex = new Soundex();

        // Typical use cases
        assertEquals(0, soundex.difference("Awesome", "Lion"));
        assertEquals(1, soundex.difference("Lion", "Lyon"));
        assertEquals(2, soundex.difference("Lion", "Rain"));

        // Edge cases
        assertEquals(0, soundex.difference("Cabbage", "cbbge")); // Case insensitivity
        assertEquals(0, soundex.difference("Cabbage", "cabbage")); // Case insensitivity
        assertEquals(0, soundex.difference("Cabbage", "C000"));
        
        // Error scenarios
        try {
            soundex.difference("Cat", null); // Null input
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Exception caught, test passed
        }
    }
}