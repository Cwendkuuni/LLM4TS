package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testEmptyTranslation() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("", translator.translate(StringUtils.EMPTY));
    }

    @Test
    public void testSingleCharacterTranslation() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("a", translator.translate("a"));
    }

    @Test
    public void testMultipleCharacterTranslation() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals("abc", translator.translate("abc"));
    }

    @Test
    public void testTranslationToWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("abc", writer);
        assertEquals("abc", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("abc", null);
    }

    @Test
    public void testTranslationWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new HexTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        StringWriter writer = new StringWriter();
        compositeTranslator.translate("abc", writer);
        assertEquals("006100620063", writer.toString());
    }

    @Test
    public void testTranslationWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertEquals(null, translator.translate(null));
    }

    @Test
    public void testSurrogatePairTranslation() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        CharSequence input = Character.toChars(0xD800) + Character.toChars(0xDC00);
        translator.translate(input, writer);
        assertEquals("\uD800\uDC00", writer.toString());
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char codepoint = Character.codePointAt(input, index);
        out.write(codepoint);
        return Character.charCount(codepoint);
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int codepoint = Character.codePointAt(input, index);
        String hex = CharSequenceTranslator.hex(codepoint);
        out.write(hex);
        return hex.length();
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        CharSequenceTranslator translator = translators[0]; // use first translator as default
        int codepointConsumed;
        while ((codepointConsumed = translator.translate(input, index, out)) < 1) {
            if (index >= input.length()) {
                break;
            }
            index += codepointConsumed;
            if (++translator.length < translators.length) {
                translator = translators[translator.length];
            } else {
                translator = new IdentityTranslator();
            }
        }
        return codepointConsumed;
    }
}