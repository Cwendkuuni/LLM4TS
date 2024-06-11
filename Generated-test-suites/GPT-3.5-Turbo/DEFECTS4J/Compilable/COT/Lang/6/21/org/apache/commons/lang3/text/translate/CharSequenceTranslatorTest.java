package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new TranslatorImpl();
        String input = "abc";
        String expected = "abc";
        String result = translator.translate(input);
        Assert.assertEquals("Translation failed", expected, result);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new TranslatorImpl();
        String input = null;
        String expected = null;
        String result = translator.translate(input);
        Assert.assertEquals("Translation failed with null input", expected, result);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new TranslatorImpl();
        String input = "";
        String expected = "";
        String result = translator.translate(input);
        Assert.assertEquals("Translation failed with empty input", expected, result);
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new TranslatorWithIOException();
        String input = "abc";
        String expected = "abc";
        try {
            String result = translator.translate(input);
            Assert.fail("IOException not thrown");
        } catch (RuntimeException e) {
            Assert.assertTrue("IOException not thrown", e.getCause() instanceof IOException);
        }
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new TranslatorImpl();
        String input = "\uD835\uDDFC";
        String expected = "\uD835\uDDFC";
        String result = translator.translate(input);
        Assert.assertEquals("Translation failed with surrogate pair", expected, result);
    }

    @Test
    public void testTranslateWithTranslatorMerged() {
        CharSequenceTranslator translator1 = new TranslatorImpl();
        CharSequenceTranslator translator2 = new TranslatorImpl();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        String input = "abc";
        String expected = "abc";
        String result = mergedTranslator.translate(input);
        Assert.assertEquals("Translation failed with merged translators", expected, result);
    }

    @Test
    public void testHex() {
        int codepoint = 97;
        String expected = "61";
        String result = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals("Hex conversion failed", expected, result);
    }

    private static class TranslatorImpl extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.subSequence(index, index + 1).toString());
            return 1;
        }
    }

    private static class TranslatorWithIOException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("IOException occurred");
        }
    }

}