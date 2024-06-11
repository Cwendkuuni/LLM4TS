package org.junit.tests;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateCharSequenceWriter() throws Exception {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        Writer out = new StringWriter();

        int result = translator.translate(input, 0, out);
        String expectedOutput = "Expected output for the given input";

        assertEquals("Checking the output size", expectedOutput.length(), out.toString().length());
        assertEquals("Checking the output content", expectedOutput, out.toString());
        assertEquals("Checking the number of codepoints consumed", result, input.length());
    }

    @Test
    public void testTranslateEmptyCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        Writer out = new StringWriter();

        int result = translator.translate(Character.DELETED, 0, out);
        String expectedOutput = "";

        assertEquals("Checking the output size", expectedOutput.length(), out.toString().length());
        assertEquals("Checking the output content", expectedOutput, out.toString());
        assertEquals("Checking the number of codepoints consumed", 0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        Writer out = null;

        translator.translate(null, 0, out);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNegativeIndexCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "Hello, World!";
        Writer out = new StringWriter();

        translator.translate(input, -1, out);
    }

    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "Hello, World!";
        Writer out = null;

        translator.translate(input, 0, out);
    }

    @Test(expected = IOException.class)
    public void testTranslateWriterIOException() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "Hello, World!";
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };

        translator.translate(input, 0, out);
    }

    @Test
    public void testTranslateLargeCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
        Writer out = new StringWriter();

        int result = translator.translate(input, 0, out);
        String expectedOutput = "Expected output for the given input";

        assertEquals("Checking the output size", expectedOutput.length(), out.toString().length());
        assertEquals("Checking the output content", expectedOutput, out.toString());
        assertEquals("Checking the number of codepoints consumed", result, input.length());
    }

    @Test
    public void testTranslateSurrogatePairCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "\uD800\uDC00";
        Writer out = new StringWriter();

        int result = translator.translate(input, 0, out);
        String expectedOutput = "\uD834\uDD1E";

        assertEquals("Checking the output size", expectedOutput.length(), out.toString().length());
        assertEquals("Checking the output content", expectedOutput, out.toString());
        assertEquals("Checking the number of codepoints consumed", 2, result);
    }

    @Test
    public void testTranslateSpecialCharactersCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new YourTranslatorClassNameHere();
        String input = "Hello!\nWorld!";
        Writer out = new StringWriter();

        int result = translator.translate(input, 0, out);
        String expectedOutput = "Expected output for the given input";

        assertEquals("Checking the output size", expectedOutput.length(), out.toString().length());
        assertEquals("Checking the output content", expectedOutput, out.toString());
        assertEquals("Checking the number of codepoints consumed", input.length(), result);
    }
}