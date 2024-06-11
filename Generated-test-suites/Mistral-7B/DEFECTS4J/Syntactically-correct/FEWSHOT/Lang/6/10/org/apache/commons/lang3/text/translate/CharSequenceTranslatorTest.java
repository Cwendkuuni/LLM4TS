package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateIdentity() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("hello world", translator.translate("hello world"));
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new HexTranslator();
        assertEquals("48656c6c6f20776f726c64", translator.translate("hello world"));
        assertEquals("48", translator.translate("H"));
    }

    @Test
    public void testTranslateSurrogatePair() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\uD800\uDC00";
        String expected = "\uD800\uDC00";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateMultipleSurrogatePairs() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "\uD800\uDC00\uD800\uDCCD";
        String expected = "\uD800\uDC00\uD800\uDCCD";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateAggregate() {
        CharSequenceTranslator idTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregatedTranslator = idTranslator.with(hexTranslator);
        String input = "hello world";
        String expected = "48656c6c6f20776f726c64";
        String actual = aggregatedTranslator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        CharSequence input = "hello world";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        String expected = "48656c6c6f20776f726c64";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        translator.translate("hello world", null);
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
        int codepoint = Character.codePointAt(input, index);
        out.write(hex(codepoint).toCharArray());
        return Character.charCount(codepoint);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            totalConsumed += consumed;
            index += consumed;
        }
        return totalConsumed;
    }
}