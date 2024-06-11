package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSetEscaper;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with a null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new CharSetEscaper();
        translator.translate(new String("test"), 0, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a null input.
     */
    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new CharSetEscaper();
        String result = translator.translate(null);
        assertNull(result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with an empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new CharSetEscaper();
        String result = translator.translate(new String(""));
        assertEquals(new String(""), result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a simple input.
     */
    @Test
    public void testTranslateSimpleInput() {
        CharSequenceTranslator translator = new CharSetEscaper();
        String input = "abc";
        String result = translator.translate(new String(input));
        assertEquals(new String("abc"), result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a special character input.
     */
    @Test
    public void testTranslateSpecialCharacterInput() {
        CharSequenceTranslator translator = new CharSetEscaper();
        String input = "abc\u00E1bc";
        String result = translator.translate(new String(input));
        String expected = "abc\\u00E1bc";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a simple input and a StringWriter.
     */
    @Test
    public void testTranslateSimpleInputToStringWriter() throws IOException {
        CharSequenceTranslator translator = new CharSetEscaper();
        String input = "abc";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(new String(input), 0, writer);
        assertEquals(input.length(), consumed);
        String result = writer.toString();
        assertEquals(new String("abc"), result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a special character input and a StringWriter.
     */
    @Test
    public void testTranslateSpecialCharacterInputToStringWriter() throws IOException {
        CharSequenceTranslator translator = new CharSetEscaper();
        String input = "abc\u00E1bc";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(new String(input), 0, writer);
        assertEquals(input.length() * 2, consumed);
        String result = writer.toString();
        String expected = "abc\\u00E1bc";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with a custom translator and a special character input.
     */
    @Test
    public void testTranslateCustomTranslatorSpecialCharacterInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.codePointAt(input, index++);
                out.write(hex(Character.codePointAt(input, index)).toCharArray());
                return Character.charCount(c);
            }
        };
        String input = "abc\u00E1bc";
        String result = translator.translate(new String(input));
        String expected = "abc\\u00E1bc";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a custom translator and a special character input.
     */
    @Test
    public void testTranslateCustomTranslatorSpecialCharacterInputToStringWriter() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.codePointAt(input, index++);
                out.write(hex(Character.codePointAt(input, index)).toCharArray());
                return Character.charCount(c);
            }
        };
        String input = "abc\u00E1bc";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(new String(input), 0, writer);
        assertEquals(input.length() * 2, consumed);
        String result = writer.toString();
        String expected = "abc\\u00E1bc";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with no translators.
     */
    @Test
    public void testWithNoTranslators() {
        CharSequenceTranslator translator = new CharSequenceTranslator();
        CharSequenceTranslator merged = translator.with();
        assertEquals(translator.getClass(), merged.getClass());
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} with one translator.
     */
    @Test
    public void testWithOneTranslator() {
        CharSequenceTranslator translator1 = new CharSetEscaper();
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                out.write('X');
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };
        CharSequenceTranslator merged = translator1.with(translator2);
        assertEquals(CharSequenceTranslator.class, merged.getClass());
        AggregateTranslator expected = (AggregateTranslator) merged;
        assertEquals(translator1.getClass(), expected.getTranslators()[0].getClass());
        assertEquals(translator2.getClass(), expected.getTranslators()[1].getClass());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with merged translators.
     */
    @Test
    public void testTranslateMergedTranslatorsToStringWriter() throws IOException {
        CharSequenceTranslator translator1 = new CharSetEscaper();
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                out.write('X');
                return Character.charCount(Character.codePointAt(input, index++));
            }
        };
        CharSequenceTranslator merged = translator1.with(translator2);
        StringWriter writer = new StringWriter();
        String input = "abc\u00E1bc";
        int consumed = merged.translate(new String(input), 0, writer);
        assertEquals(input.length() * 3, consumed);
        String result = writer.toString();
        String expected = "abc\\u00E1bcXabc\\u00E1bcX";
        assertEquals(expected, result);
    }
}