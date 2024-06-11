package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NoOpCharSequenceTranslator;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new NoOpCharSequenceTranslator();
        String input = "foo bar baz qux";
        String expected = "foo bar baz qux";

        String result = translator.translate(input);
        assertEquals(expected, result);

        Writer out = new StringWriter();
        translator.translate(input.toCharArray(), 0, out);
        expected = out.toString();
        assertEquals(expected, result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateWithStringWriter() throws Exception {
        CharSequenceTranslator translator = new NoOpCharSequenceTranslator();
        String input = "foo bar baz qux";
        StringWriter writer = new StringWriter();

        String result = translator.translate(input);
        assertEquals(result, translator.translate(input, writer).toString());
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new NoOpCharSequenceTranslator();
        String input = "foo\u00E1\u00F1 bar baz qux";
        Writer out = new StringWriter();

        translator.translate(input.toCharArray(), 0, out);
        String result = out.toString();
        assertEquals("foo\u00E1\u00F1 bar baz qux", result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateWithAggregateTranslator() throws Exception {
        CharSequenceTranslator noOpTranslator = new NoOpCharSequenceTranslator();
        CharSequenceTranslator hexTranslator = (c, out) -> {
            if (Character.isDigit(c)) {
                out.write(Character.forDigit(Character.digit(c), 16));
                return 1;
            }
            out.write(c);
            return Character.isSurrogate(c) ? Character.charCount(c) : 1;
        };
        CharSequenceTranslator translator = noOpTranslator.with(hexTranslator);
        String input = "foo\u00E1\u00F1 bar baz qux";
        StringWriter writer = new StringWriter();

        translator.translate(input.toCharArray(), 0, writer);
        String result = writer.toString();
        assertEquals("fooD1F1 bar baz qux", result);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new NoOpCharSequenceTranslator();
        String input = "foo bar baz qux";
        Writer out = null;

        translator.translate(input.toCharArray(), 0, out);
    }
}

class NoOpCharSequenceTranslator implements CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.isSurrogate(c) ? Character.charCount(c) : 1;
    }

    @Override
    public String translate(CharSequence input) {
        if (input == null) {
            return null;
        }
        return new String(input);
    }
}

class AggregateTranslator implements CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            totalConsumed += translator.translate(input, index, out);
            if (totalConsumed >= input.length()) {
                break;
            }
        }
        return totalConsumed;
    }

    @Override
    public String translate(CharSequence input) {
        StringWriter writer = new StringWriter();
        translate(input, writer);
        return writer.toString();
    }

    @Override
    public void translate(CharSequence input, Writer out) throws IOException {
        int index = 0;
        while (index < input.length()) {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
                if (consumed > 0) {
                    index += consumed;
                    break;
                }
            }
            if (index < input.length()) {
                char c = Character.codePointAt(input, index);
                out.write(c);
            }
        }
    }
}