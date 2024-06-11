package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Test null input
        assertNull(CharSequenceTranslatorTest.TestTranslator.translate(null));

        // Test empty input
        assertEquals("", CharSequenceTranslatorTest.TestTranslator.translate(""));

        // Test translation of characters
        assertEquals("ABC", CharSequenceTranslatorTest.TestTranslator.translate("ABC"));

        // Test translation of special characters
        assertEquals("&&&&&", CharSequenceTranslatorTest.TestTranslator.translate("&&&&&"));

        // Test translation of spaces
        assertEquals("   ", CharSequenceTranslatorTest.TestTranslator.translate("   "));

        // Test translation of numbers
        assertEquals("123", CharSequenceTranslatorTest.TestTranslator.translate("123"));

        // Test translation of symbols
        assertEquals("!!!", CharSequenceTranslatorTest.TestTranslator.translate("!!!"));
    }

    @Test(expected = RuntimeException.class)
    public void testTranslateRuntimeException() {
        CharSequenceTranslatorTest.RuntimeExceptionThrowingTranslator.translate("ABC");
        fail("Expected RuntimeException to be thrown");
    }

    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        Writer writer = new StringWriter();
        assertEquals(3, CharSequenceTranslatorTest.TestTranslator.translate("ABC", 0, writer));
        assertEquals("ABC", writer.toString());

        writer = new StringWriter();
        assertEquals(5, CharSequenceTranslatorTest.TestTranslator.translate("&&&&&", 0, writer));
        assertEquals("&&&&&", writer.toString());

        writer = new StringWriter();
        assertEquals(3, CharSequenceTranslatorTest.TestTranslator.translate("   ", 0, writer));
        assertEquals("   ", writer.toString());

        writer = new StringWriter();
        assertEquals(3, CharSequenceTranslatorTest.TestTranslator.translate("123", 0, writer));
        assertEquals("123", writer.toString());

        writer = new StringWriter();
        assertEquals(3, CharSequenceTranslatorTest.TestTranslator.translate("!!!", 0, writer));
        assertEquals("!!!", writer.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslateCharSequenceWriterIOException() throws IOException {
        CharSequenceTranslatorTest.IOExceptionThrowingTranslator.translate("ABC", 0, new StringWriter());
        fail("Expected IOException to be thrown");
    }

    @Test
    public void testWith() {
        CharSequenceTranslator[] translators = {
                CharSequenceTranslatorTest.TestTranslator,
                CharSequenceTranslatorTest.TestTranslator
        };
        CharSequenceTranslator translator = CharSequenceTranslatorTest.TestTranslator.with(translators);
        assertEquals("ABC", translator.translate("ABC"));
    }

    @Test
    public void testHex() {
        assertEquals("41", CharSequenceTranslator.hex(65));
        assertEquals("61", CharSequenceTranslator.hex(97));
        assertEquals("2A", CharSequenceTranslator.hex(42));
        assertEquals("5F", CharSequenceTranslator.hex(95));
        assertEquals("C0", CharSequenceTranslator.hex(192));
        assertEquals("FF", CharSequenceTranslator.hex(255));
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

    private static class RuntimeExceptionThrowingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new RuntimeException();
        }
    }

    private static class IOExceptionThrowingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }
}