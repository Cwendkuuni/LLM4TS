package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };
        String original = "Hello, World!";
        String translated = translator.translate(original);
        assertEquals("Hello, World!", original);
        assertEquals("Hello, World!", translated);
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identity = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };

        CharSequenceTranslator upperCase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = Character.codePointAt(input, index);
                out.write(Character.toUpperCase(c));
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {identity, upperCase});
        String original = "Hello, World!";
        String translated = translator.translate(original);
        assertEquals("Hello, World!", original);
        assertEquals("HELLO, WORLD!", translated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                fail("Should not be called with a null Writer");
                return 0;
            }
        };
        translator.translate(null, 0, null);
    }

    @Test(expected = IOException.class)
    public void testWriterIOError() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index == 0) {
                    throw new IOException("Writer error");
                }
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };
        String original = "Hello, World!";
        StringWriter writer = new StringWriter();
        translator.translate(original, 0, writer);
    }
}