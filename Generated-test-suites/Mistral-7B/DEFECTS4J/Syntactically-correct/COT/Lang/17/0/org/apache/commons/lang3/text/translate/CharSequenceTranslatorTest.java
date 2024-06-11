package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     */
    @org.junit.Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @org.junit.Test
    public void testTranslateWithoutWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null writer
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input
     */
    @org.junit.Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer writer = new StringWriter();
        translator.translate(CharSequences.EMPTY, writer);
        String output = writer.toString();
        assertEquals("", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with long input
     */
    @org.junit.Test
    public void testTranslateWithLongInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals(input, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with surrogate pairs
     */
    @org.junit.Test
    public void testTranslateWithSurrogatePairs() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "𠮷";
        Writer writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals(input, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null input
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testTranslateWithNullInputWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = null;
        String output = translator.translate(input);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty input
     */
    @org.junit.Test
    public void testTranslateWithEmptyInputWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = CharSequences.EMPTY;
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input
     */
    @org.junit.Test
    public void testTranslateWithEmptyInputWithWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer writer = new StringWriter();
        translator.translate(CharSequences.EMPTY, writer);
        String output = writer.toString();
        assertEquals("", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with long input
     */
    @org.junit.Test
    public void testTranslateWithLongInputWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        String output = translator.translate(input);
        assertEquals(input, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null writer
     * and long input
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testTranslateWithLongInputAndNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with an empty
     * array of translators
     */
    @org.junit.Test
    public void testTranslateWithEmptyTranslators() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequenceTranslator[] translators = {};
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer, translators);
        String output = writer.toString();
        assertEquals("Hello, World!", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with a single
     * translator
     */
    @org.junit.Test
    public void testTranslateWithSingleTranslator() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {identityTranslator});
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals("Hello, World!", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with multiple
     * translators
     */
    @org.junit.Test
    public void testTranslateWithMultipleTranslators() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {identityTranslator, uppercaseTranslator});
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();
        translator.translate(input, writer);
        String output = writer.toString();
        assertEquals("HELLO, WORLD!", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with an empty
     * input and multiple translators
     */
    @org.junit.Test
    public void testTranslateWithEmptyInputAndMultipleTranslators() throws Exception {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UpperCaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {identityTranslator, uppercaseTranslator});
        StringWriter writer = new StringWriter();
        translator.translate(CharSequences.EMPTY, writer);
        String output = writer.toString();
        assertEquals("", output);
    }
}