package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" }, { "baz", "qux" } };

        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("bar", translator.translate(new String("foobar"), 0, new StringWriter()));
        assertEquals("qux", translator.translate(new String("bazqux"), 0, new StringWriter()));

        assertEquals("", translator.translate(new String(""), 0, new StringWriter()));
        assertEquals("", translator.translate(new String("foo"), 0, new StringWriter()));
        assertEquals("", translator.translate(new String("foobar"), 3, new StringWriter()));
        assertEquals("", translator.translate(new String("foobar"), 7, new StringWriter()));

        String input = "foobarqux";
        String expected = "barqux";
        Writer writer = new StringWriter();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(0, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            assertEquals(expected.substring(0, translatedLength), translated);
            expected = expected.substring(translatedLength);
        }
        assertEquals("", expected);
    }

    @Test
    public void testEmptyLookupTranslator() {
        LookupTranslator translator = new LookupTranslator(null);
        assertEquals("", translator.translate(new String(""), 0, new StringWriter()));
        assertEquals("", translator.translate(new String("foo"), 0, new StringWriter()));
    }

    @Test
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "foo", "bar" }, { "baz", "qux" } });
        assertNull(translator.translate(null, 0, new StringWriter()));
    }

    @Test
    public void testNullLookupMap() {
        LookupTranslator translator = new LookupTranslator(null);
        assertNull(translator.translate(new String("foobarqux"), 0, new StringWriter()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertEquals("", translator.translate(new String(""), 0, new StringWriter()));
        assertEquals("", translator.translate(new String("foo"), 0, new StringWriter()));
    }

    @Test
    public void testMismatchedInputAndLookupSize() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" } };

        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobarqux";
        Writer writer = new StringWriter();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(0, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (i + translatedLength < input.length()) {
                throw new AssertionError("Unexpected translation length: expected " + (i + 1) + ", got " + (i + translatedLength));
            }
        }
    }

    @Test
    public void testMismatchedInputAndLookupCharacter() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" } };

        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobaz";
        Writer writer = new StringWriter();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(0, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (!subSeq.toString().equals(translated)) {
                throw new AssertionError("Unexpected translation: expected " + subSeq + ", got " + translated);
            }
        }
    }

    @Test
    public void testMismatchedInputAndLookupSubsequence() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", "bar" }, { "baz", "qux" } };

        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobazqux";
        Writer writer = new StringWriter();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(0, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (i < 2 || translatedLength != 1 || !subSeq.toString().equals(translated)) {
                throw new AssertionError("Unexpected translation: expected " + subSeq + ", got " + translated);
            }
        }

        length += 3;
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(i, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (translatedLength != 2 || ! Arrays.equals(new String[] { "qu", "x" }, new String[] { translated, translated.substring(1) })) {
                throw new AssertionError("Unexpected translation: expected " + subSeq + ", got " + translated);
            }
        }
    }

    @Test
    public void testMismatchedInputAndLookupSubsequenceWithNullValue() {
        CharSequence[][] lookup = new CharSequence[][] { { "foo", null }, { "baz", "qux" } };

        LookupTranslator translator = new LookupTranslator(lookup);
        String input = "foobazqux";
        Writer writer = new StringWriter();
        int length = input.length();
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(0, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (i < 2 || translatedLength != 1 || !subSeq.toString().equals(translated)) {
                throw new AssertionError("Unexpected translation: expected " + subSeq + ", got " + translated);
            }
        }

        length += 3;
        for (int i = 0; i < length; i++) {
            CharSequence subSeq = new String(input.subSequence(i, i + 1));
            int translatedLength = translator.translate(subSeq, 0, writer);
            writer.flush();
            String translated = writer.getBuffer().toString();
            writer.getBuffer().setLength(0);
            if (translatedLength != 2 || translated == null || translated.length() != 1) {
                throw new AssertionError("Unexpected translation: expected " + subSeq + ", got " + translated);
            }
        }
    }
}