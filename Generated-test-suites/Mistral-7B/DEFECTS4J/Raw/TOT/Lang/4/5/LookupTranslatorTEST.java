package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

    @Test
    public void testTranslateWithEmptyLookupTable() {
        char[][] lookup = new char[0][2];
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "This is a test";
        Writer out = new StringWriter();

        int translated = translator.translate(input, 0, out);
        assertEquals("This is a test", out.toString());
        assertEquals(0, translated);
    }

    @Test
    public void testTranslateWithValidLookupTable() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "abcdabcd";
        Writer out = new StringWriter();

        int translated = translator.translate(input, 0, out);
        assertEquals("cdcdcdcd", out.toString());
        assertEquals(7, translated);
    }

    @Test
    public void testTranslateWithPartialMatch() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "abcdabcdabcd";
        Writer out = new StringWriter();

        int translated = translator.translate(input, 0, out);
        assertEquals("cdcdcdcdcd", out.toString());
        assertEquals(11, translated);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "";
        Writer out = new StringWriter();

        int translated = translator.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullLookupTable() {
        char[][] lookup = null;
        LookupTranslator translator = new LookupTranslator(lookup);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithNegativeIndex() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "abcdabcd";
        Writer out = new StringWriter();

        int translated = translator.translate(input, -1, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithIndexBeyondInputLength() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "abcdabcd";
        Writer out = new StringWriter();

        int translated = translator.translate(input, 10, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = null;
        Writer out = new StringWriter();

        int translated = translator.translate(input, 0, out);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithNullWriter() {
        char[][] lookup = new char[][]{"ab", "cd"};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "abcdabcd";
        Writer out = null;

        int translated = translator.translate(input, 0, out);
    }
}