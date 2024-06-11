package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo";

        String result = translator.translate(input);
        assertEquals("foo", result);

        Writer out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals("foo", out.toString());
        assertEquals(input.length(), consumed);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        consumed = translator.translate(input, 0, baos.getWriter());
        assertEquals(input.length(), consumed);
        assertEquals(input.toString().getBytes(), baos.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null, 0, new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void flush() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void close() throws IOException {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator escaper = new EscapeTranslator();

        CharSequenceTranslator aggregator = identity.with(escaper);

        CharSequence input = "foo<bar>";
        String result = aggregator.translate(input);
        assertEquals("foo&lt;bar&gt;", result);

        Writer out = new StringWriter();
        int consumed = aggregator.translate(input, 0, out);
        assertEquals("foo&lt;bar&gt;", out.toString());
        assertEquals(input.length(), consumed);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (index < input.length() - 1 && Character.isHighSurrogate(c)) {
            int next = Character.codePointAt(input, index + 1);
            if (!Character.isLowSurrogate(next)) {
                throw new IllegalArgumentException("Invalid surrogate pair: " + c + next);
            }
            c = Character.toCodePoint(c, next);
        }
        out.write(c);
        return Character.charCount(c);
    }
}

class EscapeTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        switch (c) {
            case '<':
                out.write("&lt;");
                break;
            case '>':
                out.write("&gt;");
                break;
            default:
                out.write(c);
                break;
        }
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            if (consumed > 0) {
                return consumed;
            }
            index += consumed;
        }
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}