package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequences;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private static final NumericEntityUnescaper UNESCAPER = new NumericEntityUnescaper();

    /**
     * Test translating a simple numeric entity.
     */
    @Test
    public void testSimpleNumericEntity() {
        String input = "&#160;";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append((char) 160);

        StringBuilder output = new StringBuilder();
        int length = UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating a hexadecimal numeric entity.
     */
    @Test
    public void testHexadecimalNumericEntity() {
        String input = "&#[x61];";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append('a');

        StringBuilder output = new StringBuilder();
        int length = UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(expectedOutput.toString(), output.toString());
        assertEquals(1, length);
    }

    /**
     * Test handling an entity with an invalid character after the ';'.
     */
    @Test
    public void testInvalidCharacterAfterEntity() {
        String input = "&#[x61];x";
        StringBuilder output = new StringBuilder();

        try {
            UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
            fail("Expected NumberFormatException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test handling an entity with an invalid start character.
     */
    @Test
    public void testInvalidStartCharacter() {
        String input = "%&#160;";
        StringBuilder output = new StringBuilder();

        try {
            UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
            fail("Expected NumberFormatException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test handling an entity with an invalid format.
     */
    @Test
    public void testInvalidFormat() {
        String input = "&foo;";
        StringBuilder output = new StringBuilder();

        try {
            UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
            fail("Expected NumberFormatException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test handling an empty input.
     */
    @Test
    public void testEmptyInput() {
        String input = "";
        StringBuilder output = new StringBuilder();

        int length = UNESCAPER.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(0, length);
    }
}