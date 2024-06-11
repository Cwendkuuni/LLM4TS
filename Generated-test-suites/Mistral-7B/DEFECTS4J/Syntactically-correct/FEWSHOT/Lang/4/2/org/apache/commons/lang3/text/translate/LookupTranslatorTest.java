package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorCreation() {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(4, translator.shortest);
        assertEquals(4, translator.longest);
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals(0, translator.shortest);
        assertEquals(0, translator.longest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "aaabbbccd";

        int length = input.length();
        for (int i = 0; i < length; i++) {
            int result = translator.translate(input, i, writer);
            if (result > 0) {
                assertTrue("Expected translation at index " + i + " but got " + result, result <= input.length() - i);
            }
        }

        assertEquals("ABCD", writer.toString());
    }

    @Test
    public void testTranslateShortest() throws IOException {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "aaa";

        translator.translate(input, 0, writer);
        translator.translate(input, 1, writer);
        translator.translate(input, 2, writer);

        assertEquals("AAA", writer.toString());
    }

    @Test
    public void testTranslateLongest() throws IOException {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "dddd";

        translator.translate(input, 0, writer);
        translator.translate(input, 1, writer);
        translator.translate(input, 2, writer);
        translator.translate(input, 3, writer);

        assertEquals("DDDD", writer.toString());
    }

    @Test
    public void testTranslatePartialMatch() throws IOException {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "abcd";

        translator.translate(input, 0, writer);
        translator.translate(input, 1, writer);
        translator.translate(input, 2, writer);
        translator.translate(input, 3, writer);

        assertEquals("ABCD", writer.toString());
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(null);
        StringWriter writer = new StringWriter();
        String input = "";

        translator.translate(input, 0, writer);

        assertNull("Expected null output for empty input", writer.toString());
    }

    @Test
    public void testTranslateNegativeIndex() {
        LookupTranslator translator = new LookupTranslator(null);

        try {
            translator.translate(null, -1, new StringWriter());
            fail("Expected IllegalArgumentException for negative index");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            translator.translate("", -1, new StringWriter());
            fail("Expected IllegalArgumentException for negative index");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            translator.translate("abcd", -1, new StringWriter());
            fail("Expected IOException for index out of bounds");
        } catch (IOException e) {
            // expected
        }

        try {
            translator.translate("abcd", -2, new StringWriter());
            fail("Expected IOException for index out of bounds");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslateIndexOutOfBounds() throws IOException {
        CharSequence[][] lookup = { { "aa", "A" }, { "bb", "B" }, { "cc", "C" }, { "dd", "D" } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        String input = "abcd";

        try {
            translator.translate(input, input.length(), writer);
            fail("Expected IOException for index out of bounds");
        } catch (IOException e) {
            // expected
        }

        try {
            translator.translate(input, input.length() + 1, writer);
            fail("Expected IOException for index out of bounds");
        } catch (IOException e) {
            // expected
        }

        try {
            translator.translate(input, -1, writer);
            fail("Expected IllegalArgumentException for negative index");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTranslateNullInput() {
        LookupTranslator translator = new LookupTranslator(null);

        try {
            translator.translate(null, 0, new StringWriter());
            fail("Expected IllegalArgumentException for null input");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            translator.translate(null, 0, new StringWriter());
            fail("Expected IOException for null input");
        } catch (IOException e) {
            // expected
        }
    }
}