package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        String expected = "abcdefghijklmnopqrstuvwxyz";

        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        Writer out = new StringWriter();

        translator.translate(input, out);
        String result = out.toString();
        assertEquals("abcdefghijklmnopqrstuvwxyz", result);
    }

    @Test
    public void testTranslateNullInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = new StringWriter();

        translator.translate(null, out);
        String result = out.toString();
        assertNull(result);
    }

    @Test
    public void testTranslateNullOutput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "abcdefghijklmnopqrstuvwxyz";

        translator.translate(input, null);
        throw new AssertionError("Expected IOException");
    }

    @Test
    public void testTranslateWith() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new HexTranslator();

        CharSequence input = "abcdefghijklmnopqrstuvwxyz";
        Writer out = new StringWriter();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        mergedTranslator.translate(input, out);
        String result = out.toString();

        String expected = "abcdef1234567890abcdef";
        assertEquals(expected, result);
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(hex(c).toCharArray());
        return Character.charCount(c);
    }
}

class AggregateTranslator implements CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            if (consumed >= Character.codePointCount(input, 0, input.length())) {
                break;
            }
        }
        return consumed;
    }
}