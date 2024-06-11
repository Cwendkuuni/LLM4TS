package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("25", CharSequenceTranslator.hex(0x25));
        assertEquals("61", CharSequenceTranslator.hex(0x61));
        assertEquals("40", CharSequenceTranslator.hex(0x40));
        assertEquals("00", CharSequenceTranslator.hex(0x0));
    }

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String testInput = "Test String with codepoints: ÆØÅÀÁÆÅ";

        String result = translator.translate(testInput);
        assertEquals(testInput, result);

        StringWriter writer = new StringWriter();
        translator.translate(testInput, writer);
        result = writer.toString();
        assertEquals(testInput, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null, new StringWriter());
    }

    @Test
    public void testAggregateTranslator() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator translator = identity.with(hex);

        String testInput = "Test String with codepoints: ÆØÅÀÁÆÅ";
        StringWriter writer = new StringWriter();

        translator.translate(testInput, writer);
        String result = writer.toString();

        String expected = "Test String with codepoints: 4D80C586C3B8C284C3A6C3B8C289C3A6";
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
        String hex = CharSequenceTranslator.hex((int) c);
        out.write(hex);
        return hex.length();
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int result = 0;
        for (CharSequenceTranslator translator : translators) {
            result += translator.translate(input, index, out);
            if (result >= input.length()) {
                break;
            }
            index++;
        }
        return result;
    }
}