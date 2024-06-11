package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String input = "foo bar baz";
        String expected = "foo bar baz";
        String result = underTest.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToWriter() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String input = "foo bar baz";
        StringWriter writer = new StringWriter();
        underTest.translate(input, writer);
        String result = writer.toString();
        assertEquals("foo bar baz", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String input = "foo bar baz";
        Writer nullWriter = null;
        underTest.translate(input, nullWriter);
    }

    @Test
    public void testTranslateAggregate() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregatedTranslator = identityTranslator.with(hexTranslator);
        String input = "foo bar baz 0x1234 0xFF";
        StringWriter writer = new StringWriter();
        aggregatedTranslator.translate(input, writer);
        String result = writer.toString();
        assertEquals("foo bar baz 3C31323334 46", result);
    }

    @Test
    public void testTranslateSurrogatePair() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String d = "\uD800\uDC00";
        StringWriter writer = new StringWriter();
        int consumed = underTest.translate(new String(d), 0, writer);
        assertEquals(2, consumed);
        String result = writer.toString();
        assertEquals(d, result);
    }

    @Test
    public void testTranslateAggregateSurrogatePair() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregatedTranslator = identityTranslator.with(hexTranslator);
        String d = "\uD800\uDC00";
        StringWriter writer = new StringWriter();
        aggregatedTranslator.translate(new String(d), writer);
        String result = writer.toString();
        assertEquals("\uD800\uDC00", result);
    }

    @Test
    public void testTranslateNullCharSequence() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String result = underTest.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateEmptyCharSequence() throws Exception {
        CharSequenceTranslator underTest = new IdentityTranslator();
        String result = underTest.translate(Character.EMPTY);
        assertEquals("", result);
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
        if (Character.isDigit(c) || Character.isLetter(c) || c == '_') {
            out.write(c);
            return 1;
        }
        int codepoint = Character.codePointAt(input, index);
        String hex = CharSequenceTranslator.hex(codepoint);
        out.write(hex.charAt(0));
        out.write(hex.charAt(1));
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
            int translated = translator.translate(input, index, out);
            consumed += translated;
            index += translated;
            if (consumed >= input.length()) {
                break;
            }
        }
        return consumed;
    }

}