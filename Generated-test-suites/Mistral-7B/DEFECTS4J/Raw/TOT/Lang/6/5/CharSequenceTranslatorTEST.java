package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    public CharSequenceTranslatorTest(CharSequenceTranslator translator) {
        this.translator = translator;
    }

    @Parameterized.Parameters(name = "{index}: Testing translator class {0}")
    public static Collection<Object> data() {
        return Arrays.asList(new Object[]{new IdentityTranslator()}, new Object[]{new TranslateHTMLTranslator()}, new Object[]{new TranslateURLTranslator()});
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequence input = "Hello, 世界! 你好！";
        String expectedOutput = "Hello, \\u4e16\\u754c! \\u8216\\u4f60! \\u4f60\\u597d!";
        Writer out = new ByteArrayWriter();
        int consumed = translator.translate(input, 0, out);
        assertNotNull(out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(input.length(), consumed);
    }

    @Test
    public void testTranslateStringMethod() throws IOException {
        CharSequence input = "Hello, 世界! 你好！";
        String expectedOutput = "Hello, \\u4e16\\u754c! \\u8216\\u4f60! \\u4f60\\u597d!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriterException() throws IOException {
        CharSequence input = "Hello, 世界! 你好！";
        translator.translate(input, new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        });
    }

    @Test
    public void testAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new TranslateHTMLTranslator();
        CharSequenceTranslator translator3 = new TranslateURLTranslator();
        CharSequenceTranslator aggregatedTranslator = translator1.with(translator2, translator3);
        CharSequence input = "Hello <a href=\"http://www.example.com\">link</a>! 世界! 你好！";
        String expectedOutput = "Hello <a href=\"http://www.example.com\">link</a>! \\u4e16\\u754c! \\u8216\\u4f60! \\u4f60\\u597d!";
        Writer out = new ByteArrayWriter();
        int consumed = aggregatedTranslator.translate(input, 0, out);
        assertNotNull(out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(input.length(), consumed);
    }

    private static class ByteArrayWriter implements Writer {
        private ByteArrayOutputStream outputStream;

        public ByteArrayWriter() {
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public void write(int c) throws IOException {
            outputStream.write(c);
        }

        @Override
        public void write(char[] c, int off, int len) throws IOException {
            outputStream.write(c, off, len);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public String toString() {
            return outputStream.toString();
        }
    }

    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isHighSurrogate(c)) {
                int next = Character.codePointAt(input, index + 1);
                if (Character.isLowSurrogate(next)) {
                    out.write(Character.toChars(Character.toCodePoint(c, next)));
                    index += Character.charCount(c) + Character.charCount(next);
                    return Character.charCount(c) + Character.charCount(next);
                } else {
                    out.write(Character.toChars(c));
                    index++;
                    return Character.charCount(c);
                }
            } else {
                out.write(Character.toChars(c));
                index++;
                return Character.charCount(c);
            }
        }
    }

    private static class TranslateHTMLTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                char c = Character.codePointAt(input, index);
                if (c == '<') {
                    out.write("&lt;".toCharArray());
                    index++;
                    return 4;
                } else if (c == '>') {
                    out.write("&gt;".toCharArray());
                    index++;
                    return 4;
                } else {
                    out.write(Character.toChars(c));
                    index++;
                    return Character.charCount(c);
                }
            }
            return 0;
        }
    }

    private static class TranslateURLTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                char c = Character.codePointAt(input, index);
                if (c == '%') {
                    if (index + 3 < input.length()) {
                        String hex = "" + Character.toChars(Character.codePointAt(input, index + 1))
                                + Character.toChars(Character.codePointAt(input, index + 2));
                        out.write(Character.toChar(Integer.parseInt(hex, 16)));
                        index += 3;
                        return 1;
                    }
                }
                out.write(Character.toChars(c));
                index++;
                return Character.charCount(c);
            }
            return 0;
        }
    }
}