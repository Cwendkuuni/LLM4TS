package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test for the CharSequenceTranslator.";
        String expected = "This is a test for the CharSequenceTranslator.";

        String result = translator.translate(input);
        assertEquals(expected, result);

        Writer out = new StringWriter();
        int consumed = translator.translate(input.toCharArray(), 0, out);
        assertEquals(input.length(), consumed);
        assertEquals(expected, out.toString());
    }

    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test for the CharSequenceTranslator.";

        Writer out = new StringWriter();
        translator.translate(input.toCharArray(), 0, out);

        String result = out.toString();
        assertEquals("This is a test for the CharSequenceTranslator.", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test for the CharSequenceTranslator.";

        translator.translate(input.toCharArray(), 0, null);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();

        CharSequenceTranslator translator = identityTranslator.with(hexTranslator);
        String input = "This is a test for the CharSequenceTranslator.";

        Writer out = new StringWriter();
        translator.translate(input.toCharArray(), 0, out);

        String result = out.toString();
        String hexResult = "5468697320696e737468656E636F64652E";
        assertEquals(hexResult, result.substring(0, result.length() / 2).replaceAll("(..)", "$1 ").replaceAll(" ", "").toUpperCase(Locale.ENGLISH));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        if (input == null) {
            return consumed;
        }

        char c = Character.codePointAt(input, index);
        out.write(c);
        consumed += Character.charCount(c);
        index += consumed;

        return consumed;
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;

        if (input == null) {
            return consumed;
        }

        char c = Character.codePointAt(input, index);
        if (Character.isDigit(c) || Character.isLetter(c) || c == '_') {
            int codepoint = Character.codePointAt(input, index);
            out.write(hex(codepoint));
            consumed += 2;
            index++;
        } else {
            consumed = Character.charCount(c);
            index += consumed;
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
            if (consumed > 0) {
                break;
            }
            index += Character.charCount(Character.codePointAt(input, index));
        }

        return consumed;
    }
}