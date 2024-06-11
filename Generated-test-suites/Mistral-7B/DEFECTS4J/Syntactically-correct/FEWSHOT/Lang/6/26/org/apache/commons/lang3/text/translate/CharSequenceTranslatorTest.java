package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.TranslatorAdapter;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for empty input.
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new TranslatorAdapter() {};
        assertNull(translator.translate(null));
        assertNull(translator.translate(""));
    }

    /**
     * Test for single character input.
     */
    @Test
    public void testSingleCharacterInput() {
        CharSequenceTranslator translator = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('A');
                return 1;
            }
        };
        assertEquals("A", translator.translate("A"));
    }

    /**
     * Test for multiple characters input.
     */
    @Test
    public void testMultipleCharactersInput() {
        CharSequenceTranslator translator1 = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('A');
                return 1;
            }
        };
        CharSequenceTranslator translator2 = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('B');
                return 1;
            }
        };

        CharSequenceTranslator translator = translator1.with(translator2);
        assertEquals("AB", translator.translate("AB"));
    }

    /**
     * Test for StringWriter output.
     */
    @Test
    public void testStringWriterOutput() {
        CharSequenceTranslator translator = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('A');
                return 1;
            }
        };
        assertEquals("A", translator.translate("A"));
    }

    /**
     * Test for IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testIOException() {
        CharSequenceTranslator translator = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        translator.translate("A", new StringWriter(10), Locale.ENGLISH);
    }

    /**
     * Test for surrogate pairs.
     */
    @Test
    public void testSurrogatePairs() {
        CharSequenceTranslator translator = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                int codePoint = Character.codePointAt(input, index);
                if (Character.isHighSurrogate(codePoint)) {
                    int nextCodePoint = Character.codePointAt(input, index + 1);
                    if (Character.isLowSurrogate(nextCodePoint)) {
                        codePoint = ((codePoint << 10) | (nextCodePoint & 0x3FF));
                        index++;
                    }
                }
                out.write(Character.toChars(codePoint)[0]);
                return Character.charCount(codePoint);
            }
        };
        assertEquals("\uD800\uDC00", translator.translate("\uD800\uDC00"));
    }

    /**
     * Test for AggregateTranslator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('A');
                return 1;
            }
        };
        CharSequenceTranslator translator2 = new TranslatorAdapter() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('B');
                return 1;
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});
        assertEquals("AB", translator.translate("AB"));
    }
}