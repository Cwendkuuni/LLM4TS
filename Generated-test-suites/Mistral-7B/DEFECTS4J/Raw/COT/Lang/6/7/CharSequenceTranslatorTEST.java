package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Test String";
        String expectedOutput = "Test String";
        Writer out = new StringWriter();

        int consumed = translator.translate(input, 0, out);
        assertEquals(expectedOutput.length(), out.toString().length());
        assertEquals(expectedOutput, out.toString());

        // test with input index greater than length
        consumed = translator.translate(input, input.length(), out);
        assertEquals(0, consumed);
        assertEquals("", out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslateCharSequence() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Test String";
        String expectedOutput = "Test String";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with null Writer
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Test String";
        Writer out = null;

        translator.translate(input, 0, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator[])}.
     * Test behavior when merging with null translators array
     * @throws Exception
     */
    @org.junit.Test
    public void testWithNullTranslators() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator.with((CharSequenceTranslator[]) null);
        assertNotNull(mergedTranslator);
        assertEquals(translator.getClass(), mergedTranslator.getClass());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * Test behavior when merging with empty translators array
     * @throws Exception
     */
    @org.junit.Test
    public void testWithEmptyTranslators() throws Exception {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(new CharSequenceTranslator[0]);
        String input = "Test String";
        String expectedOutput = "Test String";
        Writer out = new StringWriter();

        int consumed = mergedTranslator.translate(input, 0, out);
        assertEquals(expectedOutput.length(), out.toString().length());
        assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * Test behavior when merging with one translator
     * @throws Exception
     */
    @org.junit.Test
    public void testWithOneTranslator() throws Exception {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(new CharSequenceTranslator[] {translator2});
        String input = "Test String";
        String expectedOutput = "Test StringTest String";
        Writer out = new StringWriter();

        int consumed = mergedTranslator.translate(input, 0, out);
        assertEquals(expectedOutput.length(), out.toString().length());
        assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * Test behavior when merging with more than one translator
     * @throws Exception
     */
    @org.junit.Test
    public void testWithMoreTranslators() throws Exception {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator3 = new MyCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(new CharSequenceTranslator[] {translator2, translator3});
        String input = "Test String";
        String expectedOutput = "Test StringTest StringTest String";
        Writer out = new StringWriter();

        int consumed = mergedTranslator.translate(input, 0, out);
        assertEquals(expectedOutput.length(), out.toString().length());
        assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#hex(int)}
     */
    @org.junit.Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("42", CharSequenceTranslator.hex(66));
        assertEquals("43", CharSequenceTranslator.hex(67));
    }

    /**
     * Test class for MyCharSequenceTranslator
     */
    public static class MyCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }

}