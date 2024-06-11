package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.junit.Test;

/**
 * Test cases for the abstract class {@link CharSequenceTranslator}.
 *
 * @author John Reaves
 */
public class CharSequenceTranslatorTest {

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslateCharSequence() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux quux";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = translator.translate(input, 0, out);
        assertEquals(input.length(), consumed);
        assertEquals(input.toString(), out.get(0, (int) out.size()).toString());
    }

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateString() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux quux";

        String output = translator.translate(input);
        assertEquals(input.toString(), output);
    }

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with surrogate pairs.
     */
    @Test
    public void testTranslateSurrogatePairs() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "𠮷𠮷";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = translator.translate(input, 0, out);
        assertEquals(2, consumed);
        assertEquals("𠮷𠮷", out.get(0, (int) out.size()).toString());
    }

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method with a {@link IdentityTranslator}.
     */
    @Test
    public void testTranslateIdentity() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux quux";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = translator.translate(input, 0, out);
        assertEquals(input.length(), consumed);
        assertEquals(input.toString(), out.get(0, (int) out.size()).toString());
    }

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a {@link HexTranslator}.
     */
    @Test
    public void testTranslateHex() throws IOException {
        CharSequenceTranslator translator = new HexTranslator();
        CharSequence input = "foo bar baz qux quux";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = translator.translate(input, 0, out);
        assertEquals(input.length() * 2, consumed);
        assertEquals(CharSequenceUtils.repeat(hex(0066), 13), out.get(0, (int) out.size()).toString());
    }

    /**
     * Test the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a {@link ReverseTranslator}.
     */
    @Test
    public void testTranslateReverse() throws IOException {
        CharSequenceTranslator translator = new ReverseTranslator();
        CharSequence input = "foo bar baz qux quux";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = translator.translate(input, 0, out);
        assertEquals(input.length(), consumed);
        assertEquals(new StringBuilder(input.toString()).reverse().toString(), out.get(0, (int) out.size()).toString());
    }

    /**
     * Test the {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testComposeTranslators() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();
        CharSequenceTranslator reverse = new ReverseTranslator();

        CharSequenceTranslator composite = identity.with(hex, reverse);

        CharSequence input = "foo bar baz qux quux";
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());

        int consumed = composite.translate(input, 0, out);
        assertEquals(input.length() * 2, consumed);
        assertEquals(new StringBuilder(CharSequenceUtils.repeat(hex(0066), 6)).reverse().toString(), out.get(0, (int) out.size()).toString());
    }

}

/**
 * An Identity translator that just returns the original input.
 */
class IdentityTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = super.translate(input, index, out);
        if (consumed == 0) {
            char[] c = Character.toChars(Character.codePointAt(input, index));
            out.write(c);
            consumed += c.length;
        }
        return consumed;
    }

}

/**
 * A {@link CharSequenceTranslator} that converts characters to their hexadecimal
 * representation.
 */
class HexTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        char c = Character.codePointAt(input, index);

        out.write(hex(0001 | (Character.isHighSurrogate(c) ? 0x10 : 0x0)).getBytes());
        consumed++;

        if (Character.isHighSurrogate(c)) {
            char c2 = Character.codePointAt(input, index + 1);
            out.write(hex(0x0400 | Character.codePointAt(input, index + 1)).getBytes());
            consumed += 2;
            index++;
        }

        return consumed;
    }

}

/**
 * A {@link CharSequenceTranslator} that reverses the order of the characters.
 */
class ReverseTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;

        while (index >= 0 && index < input.length()) {
            char c = Character.codePointAt(input, index);
            out.write(c);
            consumed++;
            index--;
        }

        return consumed;
    }

}

/**
 * A {@link CharSequenceTranslator} that merges multiple translators.
 */
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
        }
        return consumed;
    }

}