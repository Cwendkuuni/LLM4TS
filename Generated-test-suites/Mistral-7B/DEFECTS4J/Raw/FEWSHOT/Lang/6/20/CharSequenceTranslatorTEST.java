package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("22", CharSequenceTranslator.hex(34));
        assertEquals("26", CharSequenceTranslator.hex(38));
        assertEquals("40", CharSequenceTranslator.hex(64));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
        assertEquals("23", CharSequenceTranslator.hex(33));
        assertEquals("45", CharSequenceTranslator.hex(69));
    }

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
    public void testTranslateSingleCharacter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("A", translator.translate("ABC"));
    }

    @Test
    public void testTranslateMultipleCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(CharSequence.class.cast("ABC"), 0, writer);
        assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregateTranslator = identityTranslator.with(hexTranslator);

        assertEquals("414243", aggregateTranslator.translate(CharSequence.class.cast("ABC")));
    }

    @Test
    public void testTranslateWithAggregateTranslatorNullInput() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregateTranslator = identityTranslator.with(hexTranslator);

        assertNull(aggregateTranslator.translate(null));
    }

    @Test
    public void testTranslateWithAggregateTranslatorEmptyInput() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator aggregateTranslator = identityTranslator.with(hexTranslator);

        assertEquals("", aggregateTranslator.translate(CharSequence.class.cast("")));
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

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int codepoint = Character.codePointAt(input, index);
        out.write(CharSequenceTranslator.hex(codepoint).toCharArray());
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
            if (consumed >= input.length() - index) {
                break;
            }
        }
        return consumed;
    }
}