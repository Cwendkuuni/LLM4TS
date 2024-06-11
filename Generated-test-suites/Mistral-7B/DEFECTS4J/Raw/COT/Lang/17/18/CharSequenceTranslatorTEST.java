package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;

/**
 * JUnit test file for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method in abstract CharSequenceTranslator class.
     * @throws IOException
     */
    @Test
    public void testAbstractTranslateMethod() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequence input = "test";
        StringWriter out = new StringWriter();
        int actualConsumed = translator.translate(input, 0, out);
        int expectedConsumed = 3; // test consumes 3 codepoints
        assertEquals(expectedConsumed, actualConsumed);
        String result = out.toString();
        String expectedResult = "test";
        assertEquals(expectedResult, result);
    }

    /**
     * Test for translate method in CharSequenceTranslator class with null input.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        translator.translate(null, new StringWriter());
    }

    /**
     * Test for translate method in CharSequenceTranslator class with null writer.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequence input = "test";
        translator.translate(input, null);
    }

    /**
     * Test for translate method in CharSequenceTranslator class with empty input.
     * @throws IOException
     */
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter out = new StringWriter();
        int actualConsumed = translator.translate(Character.EMPTY, 0, out);
        int expectedConsumed = 0;
        assertEquals(expectedConsumed, actualConsumed);
        String result = out.toString();
        assertNull(result);
    }

    /**
     * Test for translate method in CharSequenceTranslator class with empty writer.
     * @throws IOException
     */
    @Test
    public void testTranslateWithEmptyWriter() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequence input = Character.toString((char) 0);
        int actualConsumed = translator.translate(input, new StringWriter());
        int expectedConsumed = 1; // input is a single unicode character
        assertEquals(expectedConsumed, actualConsumed);
        String result = translator.translate(input, new StringWriter()).toString();
        assertEquals(Character.toString((char) 0), result);
    }

    /**
     * Test for translate method in CharSequenceTranslator class with Surrogate Pair.
     * @throws IOException
     */
    @Test
    public void testTranslateWithSurrogatePair() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter out = new StringWriter();
        CharSequence input = Character.toString('\uD800') + Character.toString('\uDC00');
        int actualConsumed = translator.translate(input, 0, out);
        int expectedConsumed = 2; // test consumes 2 codepoints
        assertEquals(expectedConsumed, actualConsumed);
        String result = out.toString();
        String expectedResult = Character.toString('\uD800') + Character.toString('\uDC00');
        assertEquals(expectedResult, result);
    }

    /**
     * Test for translate method in CharSequenceTranslator class with AggregateTranslator.
     * @throws IOException
     */
    @Test
    public void testTranslateWithAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new SimpleTranslator();
        CharSequenceTranslator translator2 = new SimpleTranslator();
        CharSequenceTranslator aggregatedTranslator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});
        CharSequence input = "test123test";
        StringWriter out = new StringWriter();
        int actualConsumed = aggregatedTranslator.translate(input, 0, out);
        int expectedConsumed = 13; // test consumes 13 codepoints
        assertEquals(expectedConsumed, actualConsumed);
        String result = out.toString();
        String expectedResult = "test123test";
        assertEquals(expectedResult, result);
    }
}