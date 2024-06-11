package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang3.CharSequences;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test cases for the NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test the translate method with a valid numeric entity.
     */
    @Test
    public void testTranslateValidNumericEntity() {
        String input = "&#[xCA]FE;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        String expected = "öt";
        assertEquals(expected.length(), actual);
        assertEquals(expected, output.toString());
    }

    /**
     * Test the translate method with a valid decimal numeric entity.
     */
    @Test
    public void testTranslateValidDecimalNumericEntity() {
        String input = "&#[123];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        String expected = "?";
        assertEquals(expected.length(), actual);
        assertEquals(expected, output.toString());
    }

    /**
     * Test the translate method with an invalid numeric entity.
     */
    @Test
    public void testTranslateInvalidNumericEntity() {
        String input = "&#[xyz];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(0, actual);
        assertEquals("", output.toString());
    }

    /**
     * Test the translate method with an empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        String input = "";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(0, actual);
        assertEquals("", output.toString());
    }

    /**
     * Test the translate method with an input containing multiple numeric entities.
     */
    @Test
    public void testTranslateMultipleNumericEntities() {
        String input = "&#[xCA]FE;&#[123];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        String expected = "öt?";
        assertEquals(expected.length(), actual);
        assertEquals(expected, output.toString());
    }

    /**
     * Test the translate method with an input containing a numeric entity after a non-numeric character.
     */
    @Test
    public void testTranslateNumericEntityAfterNonNumericCharacter() {
        String input = "a&#[123];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(1, actual);
        assertEquals("a", output.toString());
    }

    /**
     * Test the translate method with an input containing a numeric entity before a non-numeric character.
     */
    @Test
    public void testTranslateNumericEntityBeforeNonNumericCharacter() {
        String input = "&#[123];a";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(1, actual);
        assertEquals("?", output.toString());
    }

    /**
     * Test the translate method with an input containing a numeric entity with leading whitespace.
     */
    @Test
    public void testTranslateNumericEntityWithLeadingWhitespace() {
        String input = " \t&#[123];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(1, actual);
        assertEquals("?", output.toString());
    }

    /**
     * Test the translate method with an input containing a numeric entity with trailing whitespace.
     */
    @Test
    public void testTranslateNumericEntityWithTrailingWhitespace() {
        String input = "&#[123]; \t";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(1, actual);
        assertEquals("?", output.toString());
    }

    /**
     * Test the translate method with an input containing a numeric entity that is out of bounds.
     */
    @Test
    public void testTranslateOutOfBoundsNumericEntity() {
        String input = "&#[1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz+;-_]&#[10FFFF];";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> unescaper.translate(CharSequences.quotedAscii(input), 0, output));
    }

    /**
     * Test the translate method with an input containing an unescaped '&' character.
     */
    @Test
    public void testTranslateUnescapedAmpersandCharacter() {
        String input = "&amp;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        assertEquals(1, actual);
        assertEquals("&", output.toString());
    }

    /**
     * Test the translate method with an input containing an unescaped ';' character.
     */
    @Test
    public void testTranslateUnescapedSemicolonCharacter() {
        String input = "&amp;#xCA;FE;&amp;#123;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int actual = unescaper.translate(CharSequences.quotedAscii(input), 0, output);
        String expected = "&öt?";
        assertEquals(expected.length(), actual);
        assertEquals(expected, output.toString());
    }
}