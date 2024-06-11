package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test case for translating numeric entities with semi-colon.
     */
    @Test
    public void testTranslateWithSemicolon() {
        String input = "&amp;#160;&#161;&#x00A9;Hello &#123;World&nbsp;&#125;&#163;";
        CharSequence charSequence = new StringCharSequence(input);
        StringWriter output = new StringWriter();
        new NumericEntityUnescaper().translate(charSequence, 0, output);
        String expectedOutput = "&amp; &nbsp; &copy;Hello &#123;World&nbsp;&#125; &pound;";
        assertEquals(expectedOutput, output.toString());
    }

    /**
     * Test case for translating numeric entities without semi-colon.
     */
    @Test
    public void testTranslateWithoutSemicolon() {
        String input = "&amp;#160&amp;#161&amp;#x00A9;Hello &amp;#123;World&nbsp;";
        CharSequence charSequence = new StringCharSequence(input);
        StringWriter output = new StringWriter();
        new NumericEntityUnescaper().translate(charSequence, 0, output);
        String expectedOutput = "&amp; &nbsp; &copy;Hello &#123;World&nbsp;";
        assertEquals(expectedOutput, output.toString());
    }

    /**
     * Test case for translating hexadecimal numeric entities.
     */
    @Test
    public void testTranslateHexadecimalEntity() {
        String input = "&amp;#xA9;";
        CharSequence charSequence = new StringCharSequence(input);
        StringWriter output = new StringWriter();
        new NumericEntityUnescaper().translate(charSequence, 0, output);
        String expectedOutput = "&copy;";
        assertEquals(expectedOutput, output.toString());
    }

    /**
     * Test case for translating decimal numeric entities.
     */
    @Test
    public void testTranslateDecimalEntity() {
        String input = "&amp;#128;";
        CharSequence charSequence = new StringCharSequence(input);
        StringWriter output = new StringWriter();
        new NumericEntityUnescaper().translate(charSequence, 0, output);
        String expectedOutput = "Δ";
        assertEquals(expectedOutput, output.toString());
    }

    /**
     * Test case for converting invalid numeric entities.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() {
        String input = "&amp;#abc;";
        CharSequence charSequence = new StringCharSequence(input);
        StringWriter output = new StringWriter();
        new NumericEntityUnescaper().translate(charSequence, 0, output);
    }
}

/**
 * Helper class for creating CharSequence from Strings.
 */
class StringCharSequence implements CharSequence {

    private final String string;

    public StringCharSequence(String string) {
        this.string = string;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringCharSequence(string.substring(start, end));
    }
}