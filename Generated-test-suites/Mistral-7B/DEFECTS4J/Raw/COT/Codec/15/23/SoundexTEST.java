package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex.
 */
public class SoundexTest {
    

    @Test
    public void testDefaultConstructor() {
        final Soundex soundex = new Soundex();
        assertEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNullString() {
        final Soundex soundex = new Soundex();
        soundex.encode(null);
    }

    @Test
    public void testEncodeWithEmptyString() {
        final Soundex soundex = new Soundex();
        assertEquals("", soundex.encode(""));
    }

    @Test
    public void testEncodeWithSingleCharacter() {
        final Soundex soundex = new Soundex();
        assertEquals("A", soundex.encode("A"));
    }

    @Test
    public void testEncodeWithMultipleCharacters() {
        final Soundex soundex = new Soundex();
        assertEquals("K234", soundex.encode("Kate"));
        assertEquals("T420", soundex.encode("Tommy"));
        assertEquals("S250", soundex.encode("Stephen"));
    }

    @Test
    public void testEncodeWithInternationalizedMapping() {
        final char[] internationalMapping = new char[]{'0', '0', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '2', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
        final Soundex soundex = new Soundex(internationalMapping);
        assertEquals("Q12", soundex.encode("Quentin"));
    }

    @Test
    public void testDifferenceWithSameStrings() {
        final Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Tommy", "Tommy"));
    }

    @Test
    public void testDifferenceWithDifferentStrings() {
        final Soundex soundex = new Soundex();
        assertEquals(1, soundex.difference("Tommy", "Timmy"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceWithNullStrings() {
        final Soundex soundex = new Soundex();
        soundex.difference(null, null);
    }

    
}