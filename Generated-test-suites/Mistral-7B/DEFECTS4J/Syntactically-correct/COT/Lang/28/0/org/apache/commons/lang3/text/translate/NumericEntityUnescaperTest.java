package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    private final Writer writer = new Writer() {
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();

        @Override
        public void write(int c) throws IOException {
            output.write(c);
        }

        @Override
        public void flush() throws IOException {
            // No-op
        }

        @Override
        public void close() throws IOException {
            // No-op
        }

        public String getResult() {
            return output.toString();
        }
    };

    @Test
    public void testTranslate_ValidNumericEntity() throws Exception {
        String input = "&amp;#x41;abcd&amp;#123;efgh";
        String expectedOutput = "Abcd\u007Befgh";

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence inputCS = CharSequenceUtils.subsequence(input, 0, input.length());
        int length = unescaper.translate(inputCS, 0, writer);
        String output = writer.getResult();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslate_EmptyString() throws Exception {
        String input = "";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(input, 0, writer);
        assertEquals(0, length);
        assertEquals("", writer.getResult());
    }

    @Test
    public void testTranslate_NullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertThrows(NullPointerException.class, () -> unescaper.translate(null, 0, writer));
    }

    @Test
    public void testTranslate_InputOutOfBounds() throws Exception {
        String input = "&amp;#x41;abcd&amp;#123;efgh";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 10, input.length()), 0, writer);
        assertEquals(0, length);
        assertEquals("", writer.getResult());
    }

    @Test
    public void testTranslate_InvalidNumericEntity() throws Exception {
        String input = "&amp;#abc;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
        assertEquals(0, length);
        assertEquals("", writer.getResult());
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_InvalidHexEntity() throws Exception {
        String input = "&amp;#xabcX;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_InvalidDecimalEntity() throws Exception {
        String input = "&amp;#1234567890123;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslate_EntityStartAfterEntityEnd() throws Exception {
        String input = "&amp;#x41;abcd&amp;#x41;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, writer);
    }

    @Test(expected = IOException.class)
    public void testTranslate_WriterException() throws Exception {
        String input = "&amp;#x41;abcd&amp;#123;efgh";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer brokenWriter = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        int length = unescaper.translate(CharSequenceUtils.subsequence(input, 0, input.length()), 0, brokenWriter);
    }
}