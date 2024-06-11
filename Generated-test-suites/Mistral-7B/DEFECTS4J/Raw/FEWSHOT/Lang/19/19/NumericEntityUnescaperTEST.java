package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char c) throws IOException {
                outputStream.write(Character.encode((int) c));
            }

            @Override
            public void write(char[] c, int off, int length) throws IOException {
                outputStream.write(c, off, length);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        String input = "&amp;&#x61;&#x61;&#x61;&#x65;&#x6e;&#x63;&#x6F;&#x6D;&#x65;&#x6E;&#x74;&#xA0;&#x26;&#x60;&#x61;&#x63;&#x65;&#x72;&#x73;&#x40;&#x61;&#x72;&#x73;&#x63;&#x73;&#x72;&#x69;&#x70;&#x75;&#x75;&#x3B;&#x26;&#x3C;&#x3E;&#x26;&#x41;&#x67;&#x65;&#x6E;&#x74;&#x3B;&#x26;&#x3C;&#x3E;&#x26;&#x53;&#x65;&#x61;&#x72;&#x65;&#x61;&#x75;&#x74;&#x3B;&#x26;&#x3C;&#x3E;";

        int result = unescaper.translate(new StringCharSequence(input), 0, writer);
        assertEquals("aaaamplestringxmlwebsupport", new String(outputStream.toByteArray()));
        assertEquals(54, result);
    }

    @Test
    public void testTranslate_MalformedEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char c) throws IOException {
                outputStream.write(Character.encode((int) c));
            }

            @Override
            public void write(char[] c, int off, int length) throws IOException {
                outputStream.write(c, off, length);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        String input = "&amp;&#x61;&#x61;&#x61;&#x65;&#x6e;&#x63;&#x6F;&#x6D;&#x65;&#x6E;&#x74;&#xA0;&#x26;&#x60;&#x61;&#x63;&#x65;&#x72;&#x73;&#x40;&#x61;&#x72;&#x73;&#x63;&#x73;&#x72;&#x69;&#x70;&#x75;&#x75;&#x3B;&#x26;&#x3C;&x3E;&#x26;&#x41;&#x67;&#x65;&#x6E;&#x74;&#x3B;&#x26;&#x3C;&#x3E;&#x26;&#x53;&#x65;&#x61;&#x72;&#x65;&#x61;&#x75;&#x74;&#x3B;&#x26;&#x3C;&#x3E;";

        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringCharSequence(input), 0, writer));
    }

    @Test
    public void testTranslate_NegativeIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char c) throws IOException {
                outputStream.write(Character.encode((int) c));
            }

            @Override
            public void write(char[] c, int off, int length) throws IOException {
                outputStream.write(c, off, length);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        String input = "&amp;";

        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new StringCharSequence(input), -1, writer));
    }

    @Test
    public void testTranslate_NullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertNull(unescaper.translate(null, 0, new Writer() {}));
    }

    @Test
    public void testTranslate_NullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;";

        assertThrows(NullPointerException.class, () -> unescaper.translate(new StringCharSequence(input), 0, null));
    }

    static class StringCharSequence implements CharSequence {
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
            return new StringCharSequence(string.subSequence(start, end));
        }
    }
}