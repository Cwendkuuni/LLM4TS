package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for Soundex encoder.
 */
public class SoundexTest {

    /**
     * Test for encode method with valid inputs.
     */
    @Test
    public void testEncodeValidInput() {
        Soundex encoder = new Soundex();
        String expected = "K321";
        String input = "Knowledge";
        String result = encoder.encode(input);
        assertEquals(expected, result);
    }

    /**
     * Test for encode method with empty string input.
     */
    @Test
    public void testEncodeEmptyString() {
        Soundex encoder = new Soundex();
        String result = encoder.encode("");
        assertEquals("", result);
    }

    /**
     * Test for encode method with null input.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeNullInput() {
        Soundex encoder = new Soundex();
        encoder.encode(null);
    }

    /**
     * Test for encode method with internationalized mapping.
     */
    @Test
    public void testEncodeInternationalizedMapping() {
        Soundex encoder = new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.substring(0, 10));
        String expected = "B122";
        String input = "Banana";
        String result = encoder.encode(input);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with identical inputs.
     */
    @Test
    public void testDifferenceIdenticalInputs() {
        Soundex encoder = new Soundex();
        String input1 = "Apple";
        String input2 = "Apple";
        int result = encoder.difference(input1, input2);
        assertEquals(4, result);
    }

    /**
     * Test for difference method with different inputs having some similarities.
     */
    @Test
    public void testDifferenceSomeSimilarities() {
        Soundex encoder = new Soundex();
        String input1 = "Apple";
        String input2 = "Apricot";
        int result = encoder.difference(input1, input2);
        assertEquals(3, result);
    }

    /**
     * Test for difference method with different inputs having no similarities.
     */
    @Test
    public void testDifferenceNoSimilarities() {
        Soundex encoder = new Soundex();
        String input1 = "Apple";
        String input2 = "Banana";
        int result = encoder.difference(input1, input2);
        assertEquals(0, result);
    }
}