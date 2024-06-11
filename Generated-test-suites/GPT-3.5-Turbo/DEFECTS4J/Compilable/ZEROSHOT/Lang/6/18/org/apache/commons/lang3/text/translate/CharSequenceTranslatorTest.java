package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "Hello World!";
        String expectedOutput = "Translated: Hello World!";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = null;
        String expectedOutput = null;
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        CharSequenceTranslator translator = new MockCharSequenceTranslator();
        String input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MockCharSequenceTranslator();
        CharSequenceTranslator translator2 = new AnotherMockCharSequenceTranslator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String input = "Hello World!";
        String expectedOutput = "Merged: Hello World!";
        String actualOutput = mergedTranslator.translate(input);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";
        String actualOutput = CharSequenceTranslator.hex(codepoint);
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    private class MockCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Translated: ");
            return 0;
        }

    }

    private class AnotherMockCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write("Merged: ");
            return 0;
        }

    }
}