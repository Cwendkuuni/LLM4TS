package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "foo bar baz";
        String expected = "foo %20bar %20baz";

        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        String output = writer.toString();
        String expected = "foo %20bar %20baz";

        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        CharSequence input = "foo bar baz";
        Writer out = null;

        translator.translate(input, out);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = "";
        String output = translator.translate(input);
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator translator1 = new DefaultCharSequenceTranslator();
        CharSequenceTranslator translator2 = new HexCharSequenceTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "foo bar baz";
        String expected = "foo %20bar %20baz%7C%22%7D";

        String output = aggregateTranslator.translate(input);
        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithAggregateTranslatorWriter() throws IOException {
        CharSequenceTranslator translator1 = new DefaultCharSequenceTranslator();
        CharSequenceTranslator translator2 = new HexCharSequenceTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();

        aggregateTranslator.translate(input, writer);
        String output = writer.toString();
        String expected = "foo %20bar %20baz%7C%22%7D";

        assertEquals(expected, output);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        String input = null;
        String output = translator.translate(input);
        assertNull(output);
    }
}

class HexCharSequenceTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        int consumed = 1;

        if (Character.isDigit(c)) {
            out.write(c);
            consumed += Character.charCount(c);
            if (++index < input.length()) {
                char next = Character.codePointAt(input, index);
                if (Character.isDigit(next) || Character.isAlphabetic(next) || next == '_') {
                    int codepoint = Character.digit(c, 16) << 4 | Character.digit(next, 16);
                    out.write(hex(codepoint).charAt(0));
                    out.write(hex(codepoint).charAt(1));
                    index++;
                    consumed += 2;
                }
            }
        } else {
            out.write(c);
            consumed += Character.charCount(c);
        }

        return consumed;
    }
}

class DefaultCharSequenceTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        int consumed = 1;

        if (c == ' ') {
            out.write('%');
            out.write('20');
            consumed += 2;
        } else {
            out.write(c);
            consumed += Character.charCount(c);
        }

        return consumed;
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
            if (consumed >= input.length()) {
                break;
            }
            index += consumed;
        }

        return consumed;
    }
}