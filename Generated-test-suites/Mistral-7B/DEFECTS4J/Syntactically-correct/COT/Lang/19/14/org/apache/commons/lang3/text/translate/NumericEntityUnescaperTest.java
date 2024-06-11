package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with valid input.
     */
    @Test
    public void testTranslateValidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // Not used in this test
            }

            @Override
            public void close() throws IOException {
                // Not used in this test
            }
        };
        when(out).thenReturn(writer);

        String input = "&amp;&#xA1;&#x41F;&#x43E;&#x431;&#x435;&#x444; & &#x61;&#x6C;&#x65;&#x72;&#x2032;&#x2014; & #x1F600; & &#xFFFC;";
        CharSequence charSequence = CharSequenceUtils.subsequence(input, 0, input.length() - 1);

        int length = unescaper.translate(charSequence, 0, out);
        assertEquals(45, length);
        String expectedOutput = "\u00A1\u041F\u043E\u0431\u0435\u0444 \u2014 \uD83C\uDD12\ufffc";
        assertEquals(expectedOutput, outputStream.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input - no & symbol.
     */
    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);

        CharSequence charSequence = "";

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(charSequence, 0, out));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input - no "#" symbol.
     */
    @Test
    public void testTranslateNoHashSymbol() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);

        CharSequence charSequence = "&amp;xyz";

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(charSequence, 0, out));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input - no ";" symbol.
     */
    @Test
    public void testTranslateNoSemicolonSymbol() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);

        CharSequence charSequence = "&amp;#x123";

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(charSequence, 0, out));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hexadecimal value outside the Unicode range.
     */
    @Test
    public void testTranslateHexadecimalOutsideUnicodeRange() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);

        CharSequence charSequence = "&amp;#x10FFFF";

        assertThrows(IOException.class, () -> unescaper.translate(charSequence, 0, out));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with decimal value outside the Unicode range.
     */
    @Test
    public void testTranslateDecimalOutsideUnicodeRange() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = mock(Writer.class);

        CharSequence charSequence = "&amp;#65536";

        assertThrows(IOException.class, () -> unescaper.translate(charSequence, 0, out));
    }
}