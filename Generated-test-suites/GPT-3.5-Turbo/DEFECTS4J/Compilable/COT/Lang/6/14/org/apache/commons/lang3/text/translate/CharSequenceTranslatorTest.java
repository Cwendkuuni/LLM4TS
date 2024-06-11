package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        translator.translate(null, writer);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        translator.translate("", writer);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithNonEmptyInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        translator.translate("abc", writer);
        Assert.assertEquals("ABC", writer.toString());
    }

    @Test
    public void testTranslateWithUnicodeInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        translator.translate("\u00df", writer);
        Assert.assertEquals("SS", writer.toString());
    }

    @Test
    public void testTranslateWithIOException() throws IOException {
        CharSequenceTranslator translator = new TestTranslatorThrowsIOException();
        Writer writer = new StringWriter();
        try {
            translator.translate("abc", writer);
            Assert.fail("Expected IOException was not thrown");
        } catch (IOException e) {
            Assert.assertEquals("Test IOException", e.getMessage());
        }
    }

    @Test
    public void testTranslateWithNullWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        try {
            translator.translate("abc", null);
            Assert.fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Writer must not be null", e.getMessage());
        }
    }

    @Test
    public void testTranslateWithIndexAndWriter() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        int consumed = translator.translate("abc", 1, writer);
        Assert.assertEquals(2, consumed);
        Assert.assertEquals("BC", writer.toString());
    }

    @Test
    public void testTranslateWithIndexAndWriterOutOfBounds() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        int consumed = translator.translate("abc", 10, writer);
        Assert.assertEquals(0, consumed);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithIndexOutOfBounds() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        Writer writer = new StringWriter();
        int consumed = translator.translate("abc", 10, writer);
        Assert.assertEquals(0, consumed);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateWithStringOutput() {
        CharSequenceTranslator translator = new TestTranslator();
        String output = translator.translate("abc");
        Assert.assertEquals("ABC", output);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new TestTranslator();
        CharSequenceTranslator translator2 = new TestTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        // Assert that mergedTranslator contains both translator1 and translator2
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = input.charAt(index);
            if (Character.isLowerCase(c)) {
                out.write(Character.toUpperCase(c));
                return 1;
            } else {
                out.write(c);
                return 1;
            }
        }
    }

    private static class TestTranslatorThrowsIOException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Test IOException");
        }
    }
}