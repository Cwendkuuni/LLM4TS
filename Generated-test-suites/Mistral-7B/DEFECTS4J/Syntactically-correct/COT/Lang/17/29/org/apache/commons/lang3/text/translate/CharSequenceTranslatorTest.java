package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for abstract method translate(CharSequence, int, Writer)
     */
    @Test
    public void testAbstractTranslateMethod() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        int result = translator.translate(input, 0, writer);
        assertEquals("test", writer.toString());
        assertEquals(3, result);
    }

    /**
     * Test for translate(CharSequence) method
     */
    @Test
    public void testTranslateMethod() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";

        String result = translator.translate(input);
        assertEquals("test", result);
    }

    /**
     * Test for translate(CharSequence, Writer) method
     */
    @Test
    public void testTranslateMethodWithWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        assertEquals("test", writer.toString());
    }

    /**
     * Test for translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() {
        DefaultTranslator translator = new DefaultTranslator();
        translator.translate(null, new StringWriter());
    }

    /**
     * Test for translate method with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        translator.translate(input, null);
    }

    /**
     * Test for method translate(CharSequence, int, Writer) with illegal argument
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithIllegalArgument() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "test";
        StringWriter writer = new StringWriter();

        translator.translate(input, input.length(), writer);
    }

    /**
     * Test for method translate(CharSequence, Writer) with empty input
     */
    @Test
    public void testTranslateWithEmptyInput() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = Character.TO_CHAR_ARRAY_UNSAFE[0];
        StringWriter writer = new StringWriter();

        translator.translate(input, writer);
        assertNull(writer.toString());
    }

    /**
     * Test for method with(CharSequenceTranslator...)
     */
    @Test
    public void testWithMethod() {
        DefaultTranslator translator1 = new DefaultTranslator();
        DefaultTranslator translator2 = new DefaultTranslator();

        CharSequenceTranslator translator3 = translator1.with(translator2);
        CharSequence input = "test1test2";
        StringWriter writer = new StringWriter();

        translator3.translate(input, writer);
        assertEquals("test1test2", writer.toString());
    }

    /**
     * Test for method hex(int)
     */
    @Test
    public void testHexMethod() {
        assertEquals("41544543", CharSequenceTranslator.hex(0x306D6573));
        assertEquals("0A", CharSequenceTranslator.hex(0x0A));
    }

}