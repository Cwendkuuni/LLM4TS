package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        String expected = "Hello%20World%21";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateMethodWithWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        String result = writer.toString();
        String expected = "Hello%20World%21";
        assertEquals(expected, result);
        assertEquals(input.length() * 2, consumed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethodWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "Hello World!";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testTranslateMethodWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = "";
        String result = translator.translate(input);
        assertEquals("", result);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateMethodWithNullInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = null;
        translator.translate(input);
    }

    @Test
    public void testTranslateMethodWithSingleCharacterInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = Character.toString('A');
        String result = translator.translate(input);
        assertEquals("A", result);
    }

    @Test
    public void testTranslateMethodWithSpecialCharacterInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = Character.toString((char) 0x26); // ™ symbol
        String result = translator.translate(input);
        assertEquals("%E2%82%AB", result);
    }

    @Test
    public void testTranslateMethodWithMultiByteCharacterInput() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslator();
        CharSequence input = Character.toString((char) 0x3041); //  symbol
        String result = translator.translate(input);
        assertEquals("%E3%83%90%E3%83%B3", result);
    }

    @Test
    public void testAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new DefaultTranslator();
        CharSequenceTranslator translator2 = new DefaultTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        CharSequence input = "Hello World!";
        String result = mergedTranslator.translate(input);
        String expected = "Hello%20World%21";
        assertEquals(expected, result);
    }

    @Test
    public void testHexMethod() {
        int codepoint = 0x61; // a
        String expected = "61";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    @Test
    public void testHexMethodWithNegativeCodepoint() {
        int codepoint = 0xFFFD;
        String expected = "FFFD";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

    @Test
    public void testHexMethodWithZeroCodepoint() {
        int codepoint = 0;
        String expected = "00";
        String result = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, result);
    }

}