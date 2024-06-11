package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        String expected = "foo bar baz";
        String result = identity.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        StringWriter writer = new StringWriter();
        identity.translate(input, writer);
        String result = writer.toString();
        assertEquals("foo bar baz", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        Writer out = null;
        identity.translate(input, out);
    }

    @Test
    public void testAggregateTranslator() throws IOException {
        CharSequenceTranslator lowerCase = new LowerCaseTranslator();
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { lowerCase, identity, upperCase });
        CharSequence input = "Foo BAR baz foO BAr";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String result = writer.toString();
        String expected = "foo bar baz FOO BAR";
        assertEquals(expected, result);
    }

    @Test
    public void testHex() {
        assertEquals("46", CharSequenceTranslator.hex(0x46));
        assertEquals("4142", CharSequenceTranslator.hex(0x4142));
        assertEquals("0", CharSequenceTranslator.hex(0));
        assertEquals("00", CharSequenceTranslator.hex(0x0));
    }

    @Test
    public void testIdentityTranslator() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz";
        String expected = "foo bar baz";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLowerCaseTranslator() throws IOException {
        CharSequenceTranslator translator = new LowerCaseTranslator();
        CharSequence input = "FOO BAR BAZ";
        String expected = "foo bar baz";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testUpperCaseTranslator() throws IOException {
        CharSequenceTranslator translator = new UpperCaseTranslator();
        CharSequence input = "foo bar baz";
        String expected = "FOO BAR BAZ";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = "";
        StringWriter writer = new StringWriter();
        int consumed = identity.translate(input, writer);
        assertEquals(0, consumed);
        assertNull(writer.toString());
    }

    @Test
    public void testTranslateWithSingleCharacterInput() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = Character.toString('a');
        StringWriter writer = new StringWriter();
        int consumed = identity.translate(input, writer);
        assertEquals(1, consumed);
        assertEquals("a", writer.toString());
    }

    @Test
    public void testTranslateWithSurrogatePairInput() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = Character.toString('\uD800') + Character.toString('\uDC00');
        StringWriter writer = new StringWriter();
        int consumed = identity.translate(input, writer);
        assertEquals(2, consumed);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslateWithSurrogatePairInputAndWriter() throws IOException {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequence input = Character.toString('\uD800') + Character.toString('\uDC00');
        StringWriter writer = new StringWriter();
        identity.translate(input, writer);
        String result = writer.toString();
        assertEquals("\uD800\uDC00", result);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write(Character.codePointAt(input, index));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class LowerCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (Character.isUpperCase(Character.codePointAt(input, index))) {
            char c = Character.toLowerCase(Character.codePointAt(input, index));
            out.write(c);
            return 1;
        }
        out.write(Character.codePointAt(input, index));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (Character.isLowerCase(Character.codePointAt(input, index))) {
            char c = Character.toUpperCase(Character.codePointAt(input, index));
            out.write(c);
            return 1;
        }
        out.write(Character.codePointAt(input, index));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
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
        int consumed = Character.charCount(Character.codePointAt(input, index));
        out.write(Character.codePointAt(input, index));
        return consumed;
    }
}