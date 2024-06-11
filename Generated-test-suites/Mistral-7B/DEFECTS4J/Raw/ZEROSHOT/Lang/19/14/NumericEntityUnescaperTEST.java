package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Unit tests for {@link NumericEntityUnescaper} class.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translating &amp;#x41; character to 'A'.
     */
    @Test
    public void testTranslateHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#x41;";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
        assertEquals("A", out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating &amp;#x41; character to 'A' with semicolon.
     */
    @Test
    public void testTranslateHexWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#x41;&#x003B;";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
        assertEquals("A", out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating &amp;#x41; character to 'A' with non-hex character.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#xz";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
    }

    /**
     * Test translating &amp;#x41; character to 'A' with invalid hex length.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#x41";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
    }

    /**
     * Test translating &amp;#41; character to 'A'.
     */
    @Test
    public void testTranslateDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#41;";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
        assertEquals("A", out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating &amp;#41; character to 'A' with semicolon.
     */
    @Test
    public void testTranslateDecimalWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#41;&#x003B;";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
        assertEquals("A", out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating &amp;#41; character to 'A' with non-decimal character.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimal() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#z";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
    }

    /**
     * Test translating &amp;#41; character to 'A' with invalid decimal length.
     */
    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidDecimalLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        CharSequence input = "&#41";
        Writer out = new StringBuilder();

        int length = unescaper.translate(input, 0, out);
    }
}