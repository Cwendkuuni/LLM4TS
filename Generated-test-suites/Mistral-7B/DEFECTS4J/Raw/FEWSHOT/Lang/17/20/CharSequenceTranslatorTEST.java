package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("42", CharSequenceTranslator.hex(0x42));
        assertEquals("26", CharSequenceTranslator.hex(0x26));
        assertEquals("00", CharSequenceTranslator.hex(0x00));
    }

    @Test
    public void testTranslateEmpty() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(Character.toString(new char[0])));
    }

    @Test
    public void testTranslateSingleCharacter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate(Character.toString('A')));
    }

    @Test
    public void testTranslateMultipleCharacters() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "ABC";
        String expected = "ABC";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "ABC";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream);
        translator.translate(input, outputWriter);
        String output = outputStream.toString();
        assertEquals(input, output);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate((CharSequence) null));
    }

    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "ABC";
        assertThrows(IllegalArgumentException.class, () -> translator.translate(input, (Writer) null));
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new ToUpperTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();

        CharSequenceTranslator aggregatedTranslator = translator1.with(translator2);

        String input = "abc";
        String expected = "ABC";
        assertEquals(expected, aggregatedTranslator.translate(input));
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (input == null) {
            return 0;
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("Negative index: " + index);
        }
        char c = Character.codePointAt(input, index);
        int consumed = Character.charCount(c);
        out.write(Character.toChars(c));
        return consumed;
    }
}

class ToUpperTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (input == null) {
            return 0;
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("Negative index: " + index);
        }
        char c = Character.codePointAt(input, index);
        int consumed = Character.charCount(c);
        out.write(Character.toChars(Character.toUpperCase(c)));
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
            index++;
        }
        return consumed;
    }
}