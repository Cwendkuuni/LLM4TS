package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import org.apache.commons.lang3.text.translate.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input";
        String expected = "Test Output";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        String expected = null;
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new TestTranslatorWithIOException();
        String input = "Test Input";
        String expected = "Test Output";
        try {
            StringWriter writer = new StringWriter(input.length() * 2);
            translator.translate(input, writer);
            String actual = writer.toString();
            assertEquals(expected, actual);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Test Input";
        Writer out = null;
        translator.translate(input, out);
    }

    @Test
    public void testTranslateWithNullInputAndWriter() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        Writer out = new StringWriter();
        try {
            translator.translate(input, out);
            String actual = out.toString();
            assertEquals("", actual);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCharSequenceTranslatorWithOneTranslator() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator[] translators = {translator1};
        CharSequenceTranslator mergedTranslator = translator1.with(translators);
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testCharSequenceTranslatorWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator[] translators = {translator1, translator2};
        CharSequenceTranslator mergedTranslator = translator1.with(translators);
        assertNotNull(mergedTranslator);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String actual = CharSequenceTranslator.hex(codepoint);
        assertEquals(expected, actual);
    }

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Test Output");
            return input.length() - index;
        }
    }

    private class TestTranslatorWithIOException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Test IOException");
        }
    }
}