package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test cases for Soundex class
 */
public class SoundexTest {

    /**
     * Test encoding and decoding using default US_ENGLISH_MAPPING
     */
    @Test
    public void testDefaultEncoding() {
        Soundex soundex = Soundex.US_ENGLISH;
        String input = "apple, banana, cat, dog, apple, eagle";
        String expected = "A113, B261, C123, D253, A113, E117";
        String encoded = SoundexUtils.join(SoundexUtils.encode(soundex, input.split(", ")), ", ");
        assertEquals(expected, encoded);
        String decoded = SoundexUtils.join(SoundexUtils.decode(soundex, SoundexUtils.decode(soundex, encoded).split(", ")), ", ");
        assertEquals(input, decoded);
    }

    /**
     * Test encoding and decoding using custom mapping
     */
    @Test
    public void testCustomMappingEncoding() {
        char[] customMapping = {'B', 'C', 'A'};
        Soundex soundex = new Soundex(customMapping);
        String input = "apple, banana, cat, dog, apple, eagle";
        String expected = "A11, B12, A11, D25, A11, E11";
        String encoded = SoundexUtils.join(SoundexUtils.encode(soundex, input.split(", ")), ", ");
        assertEquals(expected, encoded);
        String decoded = SoundexUtils.join(SoundexUtils.decode(soundex, SoundexUtils.decode(soundex, encoded).split(", ")), ", ");
        assertEquals(input, decoded);
    }

    /**
     * Test encoding null input
     */
    @Test
    public void testEncodingNullInput() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertThrows(NullPointerException.class, () -> soundex.encode(null));
    }

    /**
     * Test decoding null input
     */
    @Test
    public void testDecodingNullInput() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertNull(soundex.decode(null));
    }

    /**
     * Test encoding empty input
     */
    @Test
    public void testEncodingEmptyInput() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertEquals("", soundex.encode(""));
    }

    /**
     * Test encoding single letter input
     */
    @Test
    public void testEncodingSingleLetterInput() {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        String input = "a, e, i, o, u";
        String expected = "A000, E000, I000, O000, U000";
        String encoded = SoundexUtils.join(SoundexUtils.encode(soundex, input.split(", ")), ", ");
        assertEquals(expected, encoded);
    }
}