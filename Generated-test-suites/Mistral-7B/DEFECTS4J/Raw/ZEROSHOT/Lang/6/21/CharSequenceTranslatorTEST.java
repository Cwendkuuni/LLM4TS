package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.CodePointTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * with default CharSequenceTranslator.
     */
    @Test
    public void testTranslateWithDefault() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        String originalText = "Hello, 世界!";
        String expectedText = "Hello,\\uD74C\\uD604\\uD600!";
        String resultText = translator.translate(originalText);
        assertEquals(expectedText, resultText);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     * with default CharSequenceTranslator.
     */
    @Test
    public void testTranslateWithDefaultStringWriter() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        String originalText = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(originalText, writer);
        String resultText = writer.toString();
        String expectedText = "Hello,\\uD74C\\uD604\\uD600!";
        assertEquals(expectedText, resultText);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * with custom CharSequenceTranslator.
     */
    @Test
    public void testTranslateCustom() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator().with(new CodePointTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("custom_");
                return 5;
            }
        });
        String originalText = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(originalText, writer);
        String resultText = writer.toString();
        String expectedText = "Hello, custom_custom_custom_custom_custom_custom_世界!";
        assertEquals(expectedText, resultText);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     * with custom CharSequenceTranslator.
     */
    @Test
    public void testTranslateCustomStringWriter() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator().with(new CodePointTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("custom_");
                return 5;
            }
        });
        String originalText = "Hello, 世界!";
        StringWriter writer = new StringWriter();
        translator.translate(originalText, writer);
        String resultText = writer.toString();
        String expectedText = "Hello, custom_custom_custom_custom_custom_custom_世界!";
        assertEquals(expectedText, resultText);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullInput() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with null writer.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        CharSequence input = "Hello, 世界!";
        translator.translate(input, null);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with null result.
     */
    @Test
    public void testTranslateWithNullResult() {
        CharSequenceTranslator translator = new CodePointTranslator();
        translator.translate(null);
        assertNull(translator.translate(null));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input.
     */
    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(Character.toString(''), writer);
        String resultText = writer.toString();
        assertEquals("", resultText);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with empty input.
     */
    @Test
    public void testTranslateWithEmptyInputStringWriter() throws Exception {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(Character.toString(''), writer);
        String resultText = writer.toString();
        assertEquals("", resultText);
    }

}

class CodePointTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int codepoint = Character.codePointAt(input, index);
        if (Character.isHighSurrogate(codepoint)) {
            int nextIndex = index + Character.charCount(codepoint);
            int nextCodepoint = Character.codePointAt(input, nextIndex);
            if (Character.isLowSurrogate(nextCodepoint)) {
                codepoint = ((codepoint << 10) | nextCodepoint) - 0x10000;
                nextIndex += Character.charCount(nextCodepoint);
            }
        }
        out.write(hex(codepoint).toCharArray());
        return Character.charCount(codepoint);
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
        throw new IOException("Could not translate all characters.");
    }
}