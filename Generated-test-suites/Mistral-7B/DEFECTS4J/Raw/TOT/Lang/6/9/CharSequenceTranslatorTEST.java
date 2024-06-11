package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator and its implementations.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the abstract class CharSequenceTranslator without any specific implementation.
     */
    @Test
    public void testAbstractCharSequenceTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Test null input
        assertNull(translator.translate(null));

        // Test empty input
        String emptyInput = "";
        assertEquals("", translator.translate(emptyInput));

        // Test normal characters
        String normalInput = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String actualOutput = translator.translate(normalInput);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for a specific implementation of CharSequenceTranslator - IdentityTranslator.
     */
    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();

        // Test null input
        assertNull(identityTranslator.translate(null));

        // Test empty input
        String emptyInput = "";
        String expectedOutput = "";
        String actualOutput = identityTranslator.translate(emptyInput);
        assertEquals(expectedOutput, actualOutput);

        // Test normal characters
        String normalInput = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String actualOutput = identityTranslator.translate(normalInput);
        assertEquals(expectedOutput, actualOutput);

        // Test surrogate pairs
        String surrogateInput = "\uD800\uDC00";
        String expectedOutputSurrogate = "\uD800\uDC00";
        String actualOutputSurrogate = identityTranslator.translate(surrogateInput);
        assertEquals(expectedOutputSurrogate, actualOutputSurrogate);
    }

    /**
     * Test for AggregateTranslator with IdentityTranslator and EscapeHtml4Translator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator escapeHtml4Translator = new EscapeHtml4Translator();

        CharSequenceTranslator aggregatedTranslator = new CharSequenceTranslator[] {identityTranslator, escapeHtml4Translator}
                .length > 0 ? new AggregateTranslator(new CharSequenceTranslator[] {identityTranslator, escapeHtml4Translator})
                : identityTranslator;

        // Test null input
        assertNull(aggregatedTranslator.translate(null));

        // Test empty input
        String emptyInput = "";
        String expectedOutput = "";
        String actualOutput = aggregatedTranslator.translate(emptyInput);
        assertEquals(expectedOutput, actualOutput);

        // Test normal characters
        String normalInput = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String actualOutput = aggregatedTranslator.translate(normalInput);
        assertEquals(expectedOutput, actualOutput);

        // Test surrogate pairs
        String surrogateInput = "\uD800\uDC00";
        String expectedOutputSurrogate = "\uD800\uDC00";
        String actualOutputSurrogate = aggregatedTranslator.translate(surrogateInput);
        assertEquals(expectedOutputSurrogate, actualOutputSurrogate);

        // Test special characters
        String inputWithSpecialCharacters = "<>&\"'";
        String expectedOutputWithSpecialCharacters = "&lt;>&amp;\"'";
        String actualOutputWithSpecialCharacters = aggregatedTranslator.translate(inputWithSpecialCharacters);
        assertEquals(expectedOutputWithSpecialCharacters, actualOutputWithSpecialCharacters);
    }
}