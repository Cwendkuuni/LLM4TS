package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello, World!";
        String expected = "48656c6c6f2c20576f726c6421";
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);
        assertEquals(expected, writer.toString());
        assertEquals(input.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello, World!";
        String expected = "48656c6c6f2c20576f726c6421";

        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello, World!";

        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null input
     */
    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        String output = translator.translate(null);
        assertEquals(null, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input
     */
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(Character.toString(new char[0]), 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with input containing surrogate pairs
     */
    @Test
    public void testTranslateWithSurrogatePairs() throws IOException {
        String input = "👋 Hello, World!";
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(input, 0, writer);
        String expected = "d83d dc00 48656c6c6f 2c20 576f726c 6421";
        assertEquals(expected, writer.toString());
        assertEquals(input.length() * 2 + 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with input containing invalid Unicode
     * codepoints
     */
    @Test
    public void testTranslateWithInvalidUnicode() throws IOException {
        String input = "Hello, ☹ World!";
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        assertThrows(IllegalArgumentException.class, () -> translator.translate(input, 0, writer));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with input containing surrogate pairs
     */
    @Test
    public void testTranslateWithSurrogatePairsOutput() throws IOException {
        String input = "👋 Hello, World!";
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        String output = translator.translate(input);
        String expected = "d83d dc00 48656c6c6f 2c20 576f726c 6421";
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with input containing invalid Unicode
     * codepoints
     */
    @Test
     public void testTranslateWithInvalidUnicodeOutput() {
         String input = "Hello, ☹ World!";
         CharSequenceTranslator translator = new MyCharSequenceTranslator();

         String output = translator.translate(input);
         assertEquals(input, output);
     }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty output
     */
    @Test
    public void testTranslateWithEmptyOutput() throws IOException {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        StringWriter writer = new StringWriter();

        int consumed = translator.translate(Character.toString(new char[0]), 0, writer);
        assertEquals("", writer.toString());
        assertEquals(0, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty output
     */
    @Test
    public void testTranslateWithEmptyOutputString() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        String output = translator.translate(Character.toString(new char[0]));
        assertEquals(Character.toString(new char[0]), output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple translators
     */
    @Test
    public void testTranslateWithMultipleTranslators() throws IOException {
        CharSequenceTranslator originalTranslator = new MyCharSequenceTranslator();
        CharSequenceTranslator newTranslator = new NewCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = originalTranslator.with(newTranslator);
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();

        int consumed = mergedTranslator.translate(input, 0, writer);
        String expected = "48656c6c6f2c20576f726c6421new"; // assuming NewCharSequenceTranslator adds "new" to the translation
        assertEquals(expected, writer.toString());
        assertEquals(input.length() * 2, consumed);
    }

    /**
     * Test method for {@link CharSequenceTranslator#hex(int)}
     */
    @Test
    public void testHex() {
        for (int i = 0; i <= 0xFF; i++) {
            String expected = Integer.toHexString(i).toUpperCase(Locale.ENGLISH);
            String actual = CharSequenceTranslator.hex(i);
            assertEquals(expected, actual);
        }
    }

}

class MyCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        // Implementation of the translate method for the test cases
    }
}

class NewCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        // Implementation of the translate method for the test cases with the "new" addition
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            totalConsumed += translator.translate(input, index, out);
            index += totalConsumed;
        }
        return totalConsumed;
    }
}