package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequence input = "Test Input";
        String result = translator.translate(input);
        Assert.assertEquals("Test Output", result);
    }

    @Test
    public void testTranslateWithNullInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequence input = null;
        String result = translator.translate(input);
        Assert.assertNull(result);
    }

    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        CharSequenceTranslator translator = new TestTranslator();
        CharSequence input = "";
        String result = translator.translate(input);
        Assert.assertEquals("", result);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            String translation = "Test Output";
            out.write(translation);
            return 0;
        }
    }
}