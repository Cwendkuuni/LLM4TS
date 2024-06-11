package org.apache.commons.lang3.text.translate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate() throws IOException {
        String input = "Hello world!";
        String expected = "Translated: Hello world!";
        Writer writer = new TestWriter();
        translator.translate(input, writer);
        assert writer.toString().equals(expected);
    }

    @Test
    public void testTranslate_NullInput() throws IOException {
        String input = null;
        Writer writer = new TestWriter();
        translator.translate(input, writer);
        assert writer.toString() == null;
    }

    @Test
    public void testTranslate_StringOutput() {
        String input = "Hello world!";
        String expected = "Translated: Hello world!";
        String output = translator.translate(input);
        assert output.equals(expected);
    }

    @Test
    public void testTranslate_StringOutput_NullInput() {
        String input = null;
        String output = translator.translate(input);
        assert output == null;
    }

    @Test
    public void testWith() {
        CharSequenceTranslator[] translators = {translator};
        CharSequenceTranslator mergedTranslator = translator.with(translators);
        assert mergedTranslator != null;
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expected = "41";
        String output = CharSequenceTranslator.hex(codepoint);
        assert output.equals(expected);
    }

    private class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Translated: " + input.subSequence(index, input.length()));
            return input.length();
        }
    }

    private class TestWriter extends Writer {
        private StringBuilder stringBuilder;

        public TestWriter() {
            stringBuilder = new StringBuilder();
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            stringBuilder.append(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            // Do nothing
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}
