package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceTranslator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for empty CharSequence.
     */
    @Test
    public void testEmpty() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String result = translator.translate(StringUtils.EMPTY);
        assertEquals(StringUtils.EMPTY, result);
    }

    /**
     * Test for one character.
     */
    @Test
    public void testOneCharacter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String result = translator.translate("A");
        assertEquals("41", result);
    }

    /**
     * Test for multiple characters.
     */
    @Test
    public void testMultipleCharacters() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String result = translator.translate("ABC");
        assertEquals("414243", result);
    }

    /**
     * Test for character with special characters.
     */
    @Test
    public void testSpecialCharacters() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String result = translator.translate("ABCDEF\\\\/\\\"\\'");
        assertEquals("4142434445465C2F2227", result);
    }

    /**
     * Test for empty array.
     */
    @Test
    public void testEmptyArray() {
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[0]);
        String result = translator.translate(StringUtils.EMPTY);
        assertEquals(StringUtils.EMPTY, result);
    }

    /**
     * Test for one translator.
     */
    @Test
    public void testOneTranslator() {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequenceTranslator mergedTranslator = new SimpleTranslator().with(translator);
        String result = mergedTranslator.translate("ABCDEF\\\\/\\\"\\'");
        assertEquals("4142434445465C2F2227", result);
    }

    /**
     * Test for multiple translators.
     */
    @Test
    public void testMultipleTranslators() {
        CharSequenceTranslator translator1 = new SimpleTranslator();
        CharSequenceTranslator translator2 = new SimpleTranslator();
        CharSequenceTranslator mergedTranslator = new SimpleTranslator().with(translator1, translator2);
        String result = mergedTranslator.translate("ABCDEF\\\\/\\\"\\'");
        assertEquals("4142434445465C2F2227", result);
    }

    /**
     * Test for IOException when writing to StringWriter.
     */
    @Test(expected = RuntimeException.class)
    public void testIOException() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String input = "ABCDEF\\\\/\\\"\\'";
        Writer writer = new Writer() {
            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {

            }

            @Override
            public void close() throws IOException {

            }
        };
        translator.translate(input, writer);
    }

    /**
     * Test for null input.
     */
    @Test
    public void testNullInput() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String result = translator.translate(null);
        assertEquals(null, result);
    }

    /**
     * Test for null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        translator.translate(StringUtils.EMPTY, null);
    }
}