package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

/**
 * JUnit test file for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator(); // replace with actual translator class

        CharSequence input = "Hello World";
        String expectedOutput = "48656c6c6f20576f726c64";
        String actualOutput = translator.translate(input).toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        translator.translate(null, null);
    }

    @Test
    public void testTranslateWithEmptyCharSequence() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        CharSequence input = "";
        String actualOutput = translator.translate(input).toString();

        Assert.assertEquals("", actualOutput);
    }

    @Test
    public void testTranslateWithSingleCharacter() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        CharSequence input = "A";
        String expectedOutput = "41";
        String actualOutput = translator.translate(input).toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithSurrogatePair() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        CharSequence input = "\uD800\uDC00";
        String expectedOutput = "\uD800\uDC00"; // surrogate pair should be passed through unchanged
        String actualOutput = translator.translate(input).toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testTranslateWithIOException() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        CharSequence input = "Hello World";
        translator.translate(input, null);
    }

    @Test
    public void testTranslateWithMultipleCharacters() throws Exception {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();

        CharSequence input = "Hello World";
        String expectedOutput = "48656c6c6f20576f726c64";
        String actualOutput = translator.translate(input).toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslateWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new MyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new MyCharSequenceTranslator();

        CharSequenceTranslator aggregatedTranslator = translator1.with(translator2);

        CharSequence input = "Hello World";
        String expectedOutput = "48656c6c6f20576f726c64"; // no change in output as both translators are identical
        String actualOutput = aggregatedTranslator.translate(input).toString();

        Assert.assertEquals(expectedOutput, actualOutput);
    }
}

class MyCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (input == null) {
            return 0;
        }

        char currentChar = Character.codePointAt(input, index);

        String result = CharSequenceTranslator.hex(Character.codePointAt(input, index));

        out.write(result);
        return result.length();
    }
}