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
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(0x41));
        assertEquals("22", CharSequenceTranslator.hex(0x22));
        assertEquals("00", CharSequenceTranslator.hex(0x00));
    }

    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testTranslateSimpleInput() {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        assertEquals("foo", translator.translate("foo"));
    }

    @Test
    public void testTranslateSurrogatePair() {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        assertEquals("\uD800\uDC00", translator.translate("💩"));
    }

    @Test
    public void testTranslateMultipleCodePoints() {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        assertEquals("foo\u007Fbar", translator.translate("foo\u007Fbar"));
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new NonTranslatingTranslator();
        Writer writer = new StringWriter();
        translator.translate("foo\u007Fbar", writer);
        assertEquals("foo\u007Fbar", writer.toString());
    }

    @Test
    public void testTranslateWith() throws IOException {
        CharSequenceTranslator translator1 = new HexTranslator();
        CharSequenceTranslator translator2 = new NonTranslatingTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        Writer writer = new StringWriter();
        String input = "\u0068\u0065\u006C\u006C\u006F\u0020\u0062\u0061\u0072\u0020\u0061\u0072\u0072\u0020\u0062\u0065\u0066\u006F\u0072\u0065\u0020\u0063\u0068\u0065\u006D\u0065\u0020\u0074\u0068\u0065\u0020\u0064\u0069\u0065\u0020\u006E\u0065\u0074\u0020\u0073\u0063\u0072\u0065\u0020\u0075\u0073\u0072\u0063\u0065\u002E";
        compositeTranslator.translate(input, writer);

        String expected = "68656C6C6F2062617220617272206265666F7265206368656D652074686520646965206E6574207363725365616D652E";
        assertEquals(expected, writer.toString());
    }

}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index < 0 || index >= input.length()) {
            throw new IllegalArgumentException("Invalid index");
        }

        char c = Character.codePointAt(input, index);
        if (Character.isJavaIdentifierStart(c)) {
            out.write(c);
            return Character.charCount(c);
        } else if (Character.isDigit(c)) {
            out.write(hex(Character.codePointAt(input, index)).charAt(0));
            index++;
            if (index < input.length()) {
                char next = Character.codePointAt(input, index);
                if (Character.isDigit(next)) {
                    out.write(hex(Character.codePointAt(input, index)).charAt(1));
                    index++;
                }
            }
            return Character.charCount(c) * 2;
        } else {
            throw new IllegalArgumentException("Invalid character: " + c);
        }
    }
}

class NonTranslatingTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null || index < 0 || index >= input.length()) {
            throw new IllegalArgumentException("Invalid index");
        }

        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            consumed += translator.translate(input, index, out);
            index += consumed;
            if (index >= input.length()) {
                break;
            }
        }
        return consumed;
    }
}