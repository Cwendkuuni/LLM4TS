package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for empty CharSequence.
     */
    @Test
    public void testEmpty() {
        CharSequenceTranslator translator = new TranslateAll();
        String input = "";
        String expected = "";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for simple CharSequence.
     */
    @Test
    public void testSimple() {
        CharSequenceTranslator translator = new TranslateAll();
        CharSequence input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for CharSequence with special characters.
     */
    @Test
    public void testSpecial() {
        CharSequenceTranslator translator = new TranslateAll();
        CharSequence input = "abçdef\u00E9gh\u00F1ijkl";
        String expected = "ab%C3%BScdef%E9gh%F1ijkl";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for CharSequence with surrogate pairs.
     */
    @Test
    public void testSurrogatePairs() {
        CharSequenceTranslator translator = new TranslateAll();
        CharSequence input = "👋🏻";
        String expected = "U+1F44B U+1F3FB";
        String actual = translator.translate(input, new StringWriter()).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for CharSequence with errors.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testErrorNullWriter() {
        CharSequenceTranslator translator = new TranslateAll();
        CharSequence input = "Hello, World!";
        Writer out = null;
        translator.translate(input, out);
    }

    /**
     * Test for merge functionality.
     */
    @Test
    public void testMerge() {
        CharSequenceTranslator translator1 = new TranslateAll();
        CharSequenceTranslator translator2 = new TranslateUpperCase();
        CharSequenceTranslator translator = translator1.with(translator2);
        CharSequence input = "Hello, World!";
        String expected = "HELLO, WORLD!";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for merge functionality with empty translator.
     */
    @Test
    public void testMergeEmpty() {
        CharSequenceTranslator translator1 = new TranslateAll();
        CharSequenceTranslator translator2 = null;
        CharSequenceTranslator translator = translator1.with(translator2);
        CharSequence input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test for merge functionality with missing translator.
     */
    @Test
    public void testMergeMissing() {
        CharSequenceTranslator translator1 = new TranslateAll();
        CharSequenceTranslator[] translators = new CharSequenceTranslator[0];
        CharSequenceTranslator translator = translator1.with((CharSequenceTranslator[]) null);
        CharSequence input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input).toString();
        assertEquals(expected, actual);
    }
}

class TranslateAll extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index < 0 || index >= input.length()) {
            return 0;
        }
        char c = Character.isSurrogate(input.charAt(index)) ? Character.highSurrogate(input.charAt(index)) : input.charAt(index);
        out.write(CharSequenceUtils.subscribe(c));
        return Character.isSurrogate(c) ? 2 : 1;
    }
}

class TranslateUpperCase extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index < 0 || index >= input.length()) {
            return 0;
        }
        char c = Character.isSurrogate(input.charAt(index)) ? Character.highSurrogate(input.charAt(index)) : input.charAt(index);
        if (Character.isLetter(c)) {
            out.write(Character.toUpperCase(c));
        } else {
            out.write(c);
        }
        return Character.isSurrogate(c) ? 2 : 1;
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