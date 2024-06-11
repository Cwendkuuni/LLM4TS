package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * LookupTranslator Test Cases
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorWithValidLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with valid input
        String input = "abcxyz";
        int expectedLength = 4; // longest sequence length in lookup table
        int actualLength = translator.translate(new String(input), 0, writer);
        String actualResult = writer.toString();
        String expectedResult = "defpqr";

        assertEquals(expectedLength, actualLength);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLookupTranslatorWithEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringWriter writer = new StringWriter();

        // Test translation with empty lookup table
        String input = "test";
        int expectedLength = 0;
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookupTranslatorWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }

    @Test
    public void testLookupTranslatorWithEmptyInput() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with empty input
        String input = "";
        int expectedLength = 0;
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithInputBeyondLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with input longer than lookup table
        String input = "abcxyzdef";
        int expectedLength = 6; // sum of longest sequences in lookup table
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("defdef", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithInputBeyondFirstSequenceInLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with input longer than first sequence in lookup table
        String input = "abcdxyz";
        int expectedLength = 7; // sum of longest sequences in lookup table + 1 (for the character after the longest sequence)
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("defpqr", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithInputBeyondLastSequenceInLookupTable() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with input longer than the last sequence in lookup table
        String input = "abcdefxyz";
        int expectedLength = 8; // sum of longest sequences in lookup table + 2 (for the two characters after the longest sequence)
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("defpqrdef", writer.toString());
    }

    @Test
    public void testLookupTranslatorWithInvalidInputSequenceLength() {
        CharSequence[][] lookup = {{"abc", "def"}, {"xyz", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter writer = new StringWriter();

        // Test translation with a sequence length that is not present in the lookup table
        String input = "abcx";
        int expectedLength = 3; // length of input
        int actualLength = translator.translate(new String(input), 0, writer);

        assertEquals(expectedLength, actualLength);
        assertEquals("", writer.toString());
    }
}