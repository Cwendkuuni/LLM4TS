package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslation() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";
        String expectedOutput = "Hello, 世界!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);

        CharSequenceTranslator hexTranslator = new HexTranslator();
        input = "Hello, 世界!";
        expectedOutput = "48656c6c6f2C e4b882be!";
        output = hexTranslator.translate(input);
        assertEquals(expectedOutput, output);

        CharSequenceTranslator upperHexTranslator = new UpperHexTranslator();
        input = "Hello, 世界!";
        expectedOutput = "48656C6C6F2C E4B882BE!";
        output = upperHexTranslator.translate(input);
        assertEquals(expectedOutput, output);

        CharSequenceTranslator identityWithHexTranslator = new IdentityTranslator().with(new HexTranslator());
        input = "Hello, 世界!";
        expectedOutput = "48656c6c6f2C e4b882be!";
        output = identityWithHexTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslationWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        String expectedOutput = "Hello, 世界!";
        assertEquals(expectedOutput, output);

        CharSequenceTranslator hexTranslator = new HexTranslator();
        input = "Hello, 世界!";
        writer = new StringWriter();
        hexTranslator.translate(input, writer);
        output = writer.toString();
        expectedOutput = "48656c6c6f2C e4b882be!";
        assertEquals(expectedOutput, output);

        CharSequenceTranslator upperHexTranslator = new UpperHexTranslator();
        input = "Hello, 世界!";
        writer = new StringWriter();
        upperHexTranslator.translate(input, writer);
        output = writer.toString();
        expectedOutput = "48656C6C6F2C E4B882BE!";
        assertEquals(expectedOutput, output);

        CharSequenceTranslator identityWithHexTranslator = new IdentityTranslator().with(new HexTranslator());
        input = "Hello, 世界!";
        writer = new StringWriter();
        identityWithHexTranslator.translate(input, writer);
        output = writer.toString();
        expectedOutput = "48656c6c6f2C e4b882be!";
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, 世界!";
        Writer out = null;
        translator.translate(input, out);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char[] c = Character.toChars(Character.codePointAt(input, index));
        out.write(c);
        return c.length;
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int codePoint = Character.codePointAt(input, index);
        String hex = CharSequenceTranslator.hex(codePoint);
        out.write(hex);
        return hex.length();
    }
}

class UpperHexTranslator extends HexTranslator {
    @Override
    public String hex(int codepoint) {
        return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
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