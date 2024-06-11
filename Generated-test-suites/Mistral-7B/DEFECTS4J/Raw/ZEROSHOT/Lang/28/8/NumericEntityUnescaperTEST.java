package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with decimal entity.
     */
    @Test
    public void testDecimalEntity() {
        String input = "&amp;#123;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("&#123;", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hex entity.
     */
    @Test
    public void testHexEntity() {
        String input = "&amp;#x61;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("a", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid entity.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidEntity() {
        String input = "&amp;#abc;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty entity.
     */
    @Test
    public void testEmptyEntity() {
        String input = "&amp;#;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity that starts with '&' character.
     */
    @Test
    public void testEntityWithLeadingAmpersand() {
        String input = "&amp;#123;&";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("&#123;", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity that ends with ';' character.
     */
    @Test
    public void testEntityWithTrailingSemicolon() {
        String input = "&amp;#123;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("&#123;", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity in middle of input sequence.
     */
    @Test
    public void testEntityInMiddleOfInput() {
        String input = "abc&amp;#123;def";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);

        assertEquals("abc&#123;def", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty input.
     */
    @Test
    public void testEmptyInput() {
        String input = "";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(input, 0, out);

        assertEquals("", out.toString().substring(0, length));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        String input = null;
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();

        int length = unescaper.translate(input, 0, out);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with null output.
     */
    @Test(expected = NullPointerException.class)
    public void testNullOutput() {
        String input = "&amp;#123;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = null;

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, out);
    }
}