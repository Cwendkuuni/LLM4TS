package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test the basic functionality of translate method
     */
    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new SimpleTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        String testString = "Test String";
        int length = translator.translate(testString, 0, out);
        String actual = out.getBuffer().toString();
        String expected = "Test String";
        assertEquals(expected.length(), length);
        assertEquals(expected, actual);
    }

    /**
     * Test the null input scenario for translate method
     */
    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new SimpleTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        int length = translator.translate(null, 0, out);
        assertNull(out.getBuffer().toString());
        assertNull(length);
    }

    /**
     * Test the null writer scenario for translate method
     */
    @Test
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new SimpleTranslator();
        int length = translator.translate("Test String", 0, null);
        assertThrows(IllegalArgumentException.class, () -> translator.translate("Test String", 0, null));
        assertNull(length);
    }

    /**
     * Test the combine functionality of with method
     */
    @Test
    public void testCombineFunctionality() {
        CharSequenceTranslator translator1 = new SimpleTranslator();
        CharSequenceTranslator translator2 = new SimpleTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        String testString = "Test String 1, Test String 2";
        int length = combinedTranslator.translate(testString, 0, out);
        String actual = out.getBuffer().toString();
        String expected = "Test String 1, Test String 2";
        assertEquals(expected.length(), length);
        assertEquals(expected, actual);
    }

    /**
     * Test the hex method functionality
     */
    @Test
    public void testHexMethod() {
        String codepoint = "61";
        String expected = "41"; // 'A' is the ASCII value of the character '6' in hex
        String actual = CharSequenceTranslator.hex(Character.toIntValue(codepoint.charAt(0)));
        assertEquals(expected, actual);
    }

    /**
     * Test the edge case for translate method with empty CharSequence
     */
    @Test
    public void testTranslateEmptyCharSequence() {
        CharSequenceTranslator translator = new SimpleTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        int length = translator.translate(Character.toString(new char[0]), 0, out);
        assertNull(out.getBuffer().toString());
        assertEquals(0, length);
    }

    /**
     * Test the exception handling scenario for translate method with an invalid index
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateInvalidIndex() {
        CharSequenceTranslator translator = new SimpleTranslator();
        Writer out = new OutputStreamWriter(new ByteArrayOutputStream());
        String testString = "Test String";
        int index = testString.length() + 1;
        translator.translate(testString, index, out);
    }
}