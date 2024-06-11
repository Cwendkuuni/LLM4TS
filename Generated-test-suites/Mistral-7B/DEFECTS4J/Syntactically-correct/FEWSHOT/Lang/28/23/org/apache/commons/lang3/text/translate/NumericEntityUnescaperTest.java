package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_DecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }
        };

        String input = "&amp;123;";
        int result = unescaper.translate(new StringCharSequence(input), 0, writer);
        assertEquals(1, result);
        assertEquals("\u0001\u0002\u0003", outputStream.toString());
    }

    @Test
    public void testTranslate_HexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }
        };

        String input = "&amp;#x1F44D;";
        int result = unescaper.translate(new StringCharSequence(input), 0, writer);
        assertEquals(1, result);
        assertEquals("\uD83D\uDD4D", outputStream.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }
        };

        String input = "&amp;#ABC;";
        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), 0, writer);
        });
    }

    @Test
    public void testTranslate_InvalidHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }
        };

        String input = "&amp;#xABC;";
        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(new StringCharSequence(input), 0, writer);
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
                // No-op
            }

            @Override
            public void close() throws IOException {
                // No-op
            }
        };

        String input = "&amp;";
        int result = unescaper.translate(new StringCharSequence(input), 0, writer);
        assertEquals(0, result);
        assertEquals("", outputStream.toString());
    }

    // Add more test cases as needed

    private static class StringCharSequence implements CharSequence {
        private final String cs;

        public StringCharSequence(String cs) {
            this.cs = cs;
        }

        @Override
        public int length() {
            return cs.length();
        }

        @Override
        public char charAt(int index) {
            return cs.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return cs.subSequence(start, end);
        }
    }
}