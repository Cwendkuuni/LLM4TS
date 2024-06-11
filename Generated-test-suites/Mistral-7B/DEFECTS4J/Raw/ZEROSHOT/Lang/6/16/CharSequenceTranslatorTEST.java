package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.CodePointTranslator;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     */
    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new CodePointTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello, %23105C%231F304%231F5B5%231F5FC%231F680%231F681%231F680%21";
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateString() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello, %23105C%231F304%231F5B5%231F5FC%231F680%231F681%231F680%21";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, 0, writer);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String input = "Hello, 世界!";
        translator.translate(input, null);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with null input.
     */
    @Test
    public void testTranslateNullInputString() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String actual = translator.translate(null);
        assertNull(actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("", 0, writer);
        String actual = writer.toString();
        assertEquals("", actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with empty input.
     */
    @Test
    public void testTranslateEmptyInputString() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String actual = translator.translate("");
        assertEquals("", actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with no codepoints to translate.
     */
    @Test
    public void testTranslateNoCodepoints() throws IOException {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        String input = "H";
        translator.translate(input, 0, writer);
        String actual = writer.toString();
        assertEquals("H", actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with no codepoints to translate.
     */
    @Test
    public void testTranslateNoCodepointsString() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String actual = translator.translate("H");
        assertEquals("H", actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with an empty array of translators.
     */
    @Test
    public void testTranslateWithEmptyTranslatorArray() {
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[0]);
        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        assertEquals("Hello, 世界!", actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a null array of translators.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullTranslatorArray() {
        CharSequenceTranslator translator = new AggregateTranslator(null);
        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with a null array of translators.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateStringWithNullTranslatorArray() {
        CharSequenceTranslator translator = new AggregateTranslator(null);
        String input = "Hello, 世界!";
        String actual = translator.translate(input);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple translators.
     */
    @Test
    public void testTranslateWithMultipleTranslators() throws IOException {
        CharSequenceTranslator translator1 = new CodePointTranslator();
        CharSequenceTranslator translator2 = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { translator1, translator2 });
        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String actual = writer.toString();
        String expected = "HELLO, %23105C%231F304%231F5B5%231F5FC%231F680%231F681%231F680%21";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with multiple translators.
     */
    @Test
    public void testTranslateStringWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new CodePointTranslator();
        CharSequenceTranslator translator2 = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { translator1, translator2 });
        String input = "Hello, 世界!";
        String actual = translator.translate(input);
        String expected = "HELLO, %23105C%231F304%231F5B5%231F5FC%231F680%231F681%231F680%21";
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a single translator that throws an IOException.
     */
    @Test
    public void testTranslateWithIOExceptionTranslator() throws IOException {
        CharSequenceTranslator translator = new IOExceptionThrowingTranslator();
        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        assertThrows(RuntimeException.class, () -> translator.translate(input, writer));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with a single translator that throws an IOException.
     */
    @Test
    public void testTranslateStringWithIOExceptionTranslator() {
        CharSequenceTranslator translator = new IOExceptionThrowingTranslator();
        String input = "Hello, 世界!";
        String actual = translator.translate(input);
        assertNull(actual);
    }
}

class CodePointTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (Character.isSurrogate(c)) {
            int next = index + Character.charCount(c);
            char d = Character.codePointAt(input, next);
            if (!Character.isSurrogate(d)) {
                throw new RuntimeException("Invalid UTF-16 sequence.");
            }
            int consumed = Character.charCount(c) + Character.charCount(d);
            out.write(Character.toChars(Character.toIntBe(Character.codePointAt(input, index))));
            out.write(Character.toChars(Character.toIntBe(Character.codePointAt(input, index + 1))));
            return consumed;
        } else {
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(Character.toUpperCase(c)));
        return Character.charCount(c);
    }
}

class IOExceptionThrowingTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        throw new IOException();
    }
}

class AggregateTranslator extends CharSequenceTranslator {

    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            index += consumed;
            if (consumed == input.length()) {
                break;
            }
        }
        return consumed;
    }
}