package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test file for NumericEntityUnescaper class.
 */
public class NumericEntityUnescaperTest {

    private ByteArrayOutputStream outputStream;
    private Writer writer;
    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };
        unescaper = new NumericEntityUnescaper();
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() {
        String input = "&amp;&#34;&#xA9;&#x20AC;&#x2013;";
        int index = 0;
        unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer);
        assertEquals("&amp;\"&copy;&euro;&#x2013;", outputStream.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input.
     */
    @Test
    public void testTranslateInvalidInput() {
        String input = "&";
        int index = 0;
        assertThrows(IOException.class, () -> unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with hexadecimal entity.
     */
    @Test
    public void testTranslateHexadecimalEntity() {
        String input = "&#x1F603;";
        int index = 0;
        unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer);
        assertEquals("\uD83C\uDE03", outputStream.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with octal entity.
     */
    @Test
    public void testTranslateOctalEntity() {
        String input = "&#0176;";
        int index = 0;
        unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer);
        assertEquals("Å", outputStream.toString());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with entity outside of input.
     */
    @Test
    public void testTranslateEntityOutsideOfInput() {
        String input = "&amp;";
        int index = 2;
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid hexadecimal entity.
     */
    @Test
    public void testTranslateInvalidHexadecimalEntity() {
        String input = "&&#xz;";
        int index = 0;
        assertThrows(NumberFormatException.class, () -> unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid octal entity.
     */
    @Test
    public void testTranslateInvalidOctalEntity() {
        String input = "&&#123;";
        int index = 0;
        assertThrows(NumberFormatException.class, () -> unescaper.translate(CharSequenceUtils.subsequence(input, index), index, writer));
    }
}