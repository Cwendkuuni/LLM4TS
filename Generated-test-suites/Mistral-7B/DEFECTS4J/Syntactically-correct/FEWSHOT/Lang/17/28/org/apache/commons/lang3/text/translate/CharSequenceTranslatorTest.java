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
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo bar baz qux";
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    @Test
    public void testTranslateWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo bar baz qux";
        String expectedOutput = "foo bar baz qux";

        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux";
        translator.translate(input, null);
    }

    @Test
    public void testTranslateSurrogatePair1() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        String input = "\uD800\uDC00";
        String output = translator.translate(input);
        assertEquals("\\uD800\\uDC00", output);

        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        output = writer.toString();
        assertEquals("\\uD800\\uDC00", output);
    }

    @Test
    public void testTranslateSurrogatePair2() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\uD800\uDC00";
        String output = translator.translate(input);
        assertNull(output);

        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        output = writer.toString();
        assertNull(output);
    }

    @Test
    public void testTranslateAggregate() throws IOException {
        CharSequenceTranslator translator1 = new HexTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator aggregateTranslator = translator1.with(translator2);
        String input = "foo bar baz qux \uD800\uDC00";
        String output = aggregateTranslator.translate(input);
        assertEquals("foo bar baz qux \\uD800\\uDC00", output);

        StringWriter writer = new StringWriter();
        aggregateTranslator.translate(input, writer);
        output = writer.toString();
        assertEquals("foo bar baz qux \\uD800\\uDC00", output);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }

        char c = Character.codePointAt(input, index);
        if (Character.isHighSurrogate(c)) {
            int next = Character.codePointAt(input, index + 1);
            int hex = ((Character.charToInt(c) << 10) | Character.charToInt(next)) >> 4;
            out.write('%');
            out.write('X');
            out.write(Integer.toHexString(hex).charAt(0));
            out.write(Integer.toHexString(hex).charAt(1));
            index += Character.charCount(c) + Character.charCount(next);
            return Character.charCount(c) + Character.charCount(next);
        }

        out.write(c);
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int pos = index;
        int consumed = 0;

        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, pos, out);
            if (consumed > 0) {
                pos += consumed;
            }
        }

        if (consumed == 0) {
            char c = Character.codePointAt(input, pos);
            out.write(c);
            consumed = Character.charCount(c);
            pos++;
        }

        return consumed;
    }
}