package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testIdentityTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "foo bar baz qux";
        String expected = "foo bar baz qux";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testAggregateTranslate() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator upperCaseTranslator = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(identityTranslator, upperCaseTranslator);
        String input = "foo bar baz qux";
        String expected = "FOO BAR BAZ QUX";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = "foo bar baz qux";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithSurrogatePairs() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "\uD834\uDF06\uD835\uDD32";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = "\uD834\uDF06\uD835\uDD32";
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithCodePoints() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = CharSequenceUtils.subSequence(Character.toChars(0x0100), 1, 2);
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String expected = Character.toString((char) 0x0100);
        String actual = writer.toString();
        assertEquals(expected, actual);

        CharSequence input2 = CharSequenceUtils.subSequence(Character.toChars(0x10FFFF), 0, 1);
        translator.translate(input2, writer);
        String expected2 = Character.toString((char) 0x10FFFF);
        String actual2 = writer.toString();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        String expected = null;
        try {
            translator.translate(null, writer);
            fail("Expected an IOException");
        } catch (IOException e) {
            // Expected
        }
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "foo bar baz qux";
        String expected = "foo bar baz qux";
        try {
            translator.translate(input, null);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertEquals(expected, translator.translate(input));
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (Character.isSurrogate(c)) {
            int next = Character.codePointAt(input, index + 1);
            if (!Character.isSurrogate(next)) {
                out.write(Character.toChars(Character.toIntBehind(c)));
            }
            out.write(Character.toChars(c));
            index += Character.charCount(c);
            return Character.charCount(c) + Character.charCount(next);
        }
        out.write(Character.toChars(c));
        return Character.charCount(c);
    }
}

class UpperCaseTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (Character.isUpperCase(c)) {
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }
        if (Character.isLowercase(c)) {
            c = Character.toUpperCase(c);
            out.write(Character.toChars(c));
            index++;
            return Character.charCount(c);
        }
        out.write(Character.toChars(c));
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
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