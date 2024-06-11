package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator abstract class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method of CharSequenceTranslator abstract class with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "";
        String expected = "";
        String result = translator.translate(original);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for translate method of CharSequenceTranslator abstract class with simple input.
     */
    @Test
    public void testTranslateSimpleInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "Hello, World!";
        String expected = "Hello, World!";
        String result = translator.translate(original);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for translate method of CharSequenceTranslator abstract class with surrogate pairs.
     */
    @Test
    public void testTranslateSurrogatePairs() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "👋";
        String expected = "👋";
        String result = translator.translate(original);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for translate method of CharSequenceTranslator abstract class with Writer.
     */
    @Test
    public void testTranslateWithWriter() throws IOException {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "Hello, World!";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(CharSequenceUtils.subSequence(original, 0, original.length()), 0, writer);
        String expected = "Hello, World!";
        String result = writer.toString();
        Assert.assertEquals(expected, result);
        Assert.assertEquals(original.length(), consumed);
    }

    /**
     * Test for translate method of CharSequenceTranslator abstract class with error scenario - null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = null;
        translator.translate(input);
    }

    /**
     * Test for translate method of CharSequenceTranslator abstract class with error scenario - null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        Writer writer = null;
        translator.translate(input, writer);
    }

    /**
     * Test for translate method of AggregateTranslator class.
     */
    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new ReverseTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String original = "Hello, World!";
        String expected = "!dlroW ,olleH";
        String result = combinedTranslator.translate(original);
        Assert.assertEquals(expected, result);
    }

    /**
     * Identity translator that does not translate any codepoints.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char codePoint = Character.codePointAt(input, index);
            char[] chars = Character.toChars(codePoint);
            out.write(chars);
            return chars.length;
        }
    }

    /**
     * Reverse translator that reverse the order of codepoints.
     */
    private static class ReverseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            char codePoint = Character.codePointAt(input, index);
            String reversedCodePoint = hex(Character.codePointAt(Character.toChars(codePoint)[0])).substring(1) +
                    hex(Character.codePointAt(Character.toChars(codePoint)[1])).substring(1);
            out.write(reversedCodePoint.toCharArray());
            return 2;
        }
    }
}