package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        assertEquals("", translator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testTranslateIdentity() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo";
        String expected = "foo";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithWriterNull() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo";
        try {
            translator.translate(input, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo";
        StringWriter output = new StringWriter();
        translator.translate(input, output);
        assertEquals("foo", output.toString());
    }

    @Test
    public void testTranslateWithWriterIOException() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        CharSequence input = "foo";
        StringWriter output = new StringWriter();
        try {
            translator.translate(input, output);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslateWithAggregate() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(uppercase);
        String input = "foo";
        String expected = "FOO";
        assertEquals(expected, translator.translate(input));
    }

    @Test
    public void testTranslateWithAggregateNullInput() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(uppercase);
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithAggregateEmptyInput() {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator uppercase = new UpperCaseTranslator();
        CharSequenceTranslator translator = identity.with(uppercase);
        assertEquals("", translator.translate(""));
    }

    @Test
    public void testHex() {
        String expected = "46";
        int codepoint = 'F';
        assertEquals(expected, CharSequenceTranslator.hex(codepoint));
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(c));
        return Character.charCount(c);
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(Character.toUpperCase(c)));
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
        CharSequenceTranslator translator = translators[0];
        int consumed = translator.translate(input, index, out);
        for (int i = 1; i < translators.length; i++) {
            CharSequenceTranslator next = translators[i];
            consumed += next.translate(input, index + consumed, out);
        }
        return consumed;
    }
}