package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#x41;#x42;#x61;#x63;";
        int index = 0;

        int result = unescaper.translate(new StringCharSequence(input), index, writer);
        assertEquals("ABc", outputStream.toString());
        assertEquals(11, result);
    }

    @Test
    public void testTranslate_ValidDec() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#105;#110;#103;";
        int index = 0;

        int result = unescaper.translate(new StringCharSequence(input), index, writer);
        assertEquals("abc", outputStream.toString());
        assertEquals(10, result);
    }

    @Test
    public void testTranslate_ValidHexWithSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#x41;#x42;&#xA0;&#x61;&#x63;&#x3B;";
        int index = 0;

        int result = unescaper.translate(new StringCharSequence(input), index, writer);
        assertEquals("ABc", outputStream.toString());
        assertEquals(11, result);
    }

    @Test
    public void testTranslate_InvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#x41;#xZX;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), index, writer);
        });
    }

    @Test
    public void testTranslate_InvalidHexLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#x1;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), index, writer);
        });
    }

    @Test
    public void testTranslate_InvalidDec() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#z;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), index, writer);
        });
    }

    @Test
    public void testTranslate_InvalidDecLength() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#1;";
        int index = 0;

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), index, writer);
        });
    }

    @Test
    public void testTranslate_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "";
        int index = 0;

        int result = unescaper.translate(new StringCharSequence(input), index, writer);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_InvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#&";
        int index = 0;

        assertThrows(IOException.class, () -> {
            unescaper.translate(new StringCharSequence(input), index, writer);
        });
    }

    @Test
    public void testTranslate_InputEndReached() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
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
            }
        };

        String input = "&amp;#123;";
        int index = 7; // past the end of the entity

        int result = unescaper.translate(new StringCharSequence(input), index, writer);
        assertEquals(0, result);
    }
}