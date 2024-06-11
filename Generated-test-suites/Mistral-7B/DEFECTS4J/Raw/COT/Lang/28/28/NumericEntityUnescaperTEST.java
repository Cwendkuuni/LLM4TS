package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslateNormalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#160; &#x201c; &#x201d; &#x00A9; &#x00AE;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };

        int length = unescaper.translate(new String(input.getBytes(StandardCharsets.UTF_8)), 0, writer);
        String expectedOutput = "  “” © §";

        assertEquals(expectedOutput.length(), outputStream.size());
        assertEquals(expectedOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        assertEquals(length, expectedOutput.length());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with invalid input.
     */
    @Test
    public void testTranslateInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#xabc; &#x201c&";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };

        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String(input.getBytes(StandardCharsets.UTF_8)), 0, writer));
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };

        int length = unescaper.translate(new String(input.getBytes(StandardCharsets.UTF_8)), 0, writer);
        assertEquals(0, length);
        assertEquals(0, outputStream.size());
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with negative index.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateNegativeIndex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#160;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };

        unescaper.translate(new String(input.getBytes(StandardCharsets.UTF_8)), -1, writer);
    }

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)} with out of bounds length.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateOutOfBoundsLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;#160;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // empty
            }

            @Override
            public void close() throws IOException {
                // empty
            }
        };

        unescaper.translate(new String(input.getBytes(StandardCharsets.UTF_8)), input.length(), writer);
    }

}