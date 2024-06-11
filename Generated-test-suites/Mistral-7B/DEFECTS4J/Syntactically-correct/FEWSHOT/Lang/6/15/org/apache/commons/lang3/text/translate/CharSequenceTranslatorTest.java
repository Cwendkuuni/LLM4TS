package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateSimple() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("Hello World", translator.translate("Hello World"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("Hello World", writer);
        assertEquals("Hello World", writer.toString());
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String result = translator.translate(Character.toString(new char[0]));
        assertEquals("", result);
    }

    @Test
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate("A"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testTranslateSurrogatePair() {
        CharSequenceTranslator translator = new IdentityTranslator();
        char[] input = Character.toChars(0x10FFFF);
        String result = translator.translate(new String(input));
        assertEquals(new String(input), result);
    }

    @Test
    public void testTranslateWithCustomTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        StringWriter writer = new StringWriter();
        mergedTranslator.translate("Hello World", writer);
        assertEquals("HELLO WORLD", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ToUpperCaseTranslator();
        CharSequenceTranslator translator3 = new ReverseTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        mergedTranslator.translate("Hello World", writer);
        String result = outputStream.toString(StandardCharsets.UTF_8.name());
        assertArrayEquals(new byte[]{"57", "4F", "4C", "4C", "45", "4E", "20", "57", "4F", "4D", "4C", "45", "4E", "4C", "4F", "57", "4F", "4B", "4C", "4C", "45", "4E"}, Arrays.copyOf(result.getBytes(), result.getBytes().length));
    }

}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char[] chars = Character.toChars(Character.codePointAt(input, index));
        out.write(chars);
        return chars.length;
    }
}

class ToUpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.toUpperCase(Character.codePointAt(input, index));
        out.write(Character.toChars(c));
        return Character.charCount(c);
    }
}

class ReverseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(Character.icodePoint(Character.isHighSurrogate(c) ? Character.highSurrogate(Character.codePointAt(input, index + 1)) : c))[0]);
        out.write(Character.toChars(Character.isHighSurrogate(c) ? Character.lowSurrogate(Character.codePointAt(input, index + 1)) : c)[1]);
        return 2;
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
            index += consumed;
            if (translator.translate(input, index, out) == 0) {
                break;
            }
        }
        return consumed;
    }
}