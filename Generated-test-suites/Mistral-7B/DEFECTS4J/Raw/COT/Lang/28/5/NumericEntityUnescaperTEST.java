package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

    private static final String TEST_INPUT = "&amp;example&lt; &#x304;hello&apos;&#x26;world &#xff;&#x41; &#x{10}ef &#x{1F600} &#x{1F601};";

    @Test
    public void testTranslateNormalCase() throws IOException {
        String expectedOutput = "example\u0304hello\'&world\uffffA\uD835\uDD1E\uD835\uDD2F;";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(TEST_INPUT), 0, writer);
        assertEquals(expectedOutput, outputStream.toString());
        assertEquals(TEST_INPUT.length(), length);
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        String input = "&invalid;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("", outputStream.toString());
        assertEquals(0, length);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateInvalidHexEntity() throws IOException {
        String input = "&#xAAA;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslateNegativeDecimalEntity() throws IOException {
        String input = "&#-1;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateArrayIndexOutOfBounds() throws IOException {
        String input = "&abcd;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 2, writer);
    }

    @Test
    public void testTranslateEmptyString() throws IOException {
        String input = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("", outputStream.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateEmptyEntity() throws IOException {
        String input = "&;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("", outputStream.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateEntityWithTrailingSemiColon() throws IOException {
        String input = "&amp;\u003B";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("&amp;", outputStream.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslateEntityWithLeadingSemiColon() throws IOException {
        String input = ";&amp;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("&amp;", outputStream.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslateEntityWithoutHash() throws IOException {
        String input = "&amp;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        };

        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals("&amp;", outputStream.toString());
        assertEquals(3, length);
    }

}