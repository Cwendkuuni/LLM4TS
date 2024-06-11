package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String original = "Hello, World!";
        String expected = "Hello, World!";

        try {
            String translated = translator.translate(CharSequenceUtils.subSequence(original, 0, original.length()), new StringWriter());
            Assert.assertEquals(expected, translated);
        } catch (IOException e) {
            // Should not throw any exception
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "";
        String expected = "";

        try {
            String translated = translator.translate(input);
            Assert.assertEquals(expected, translated);
        } catch (IOException e) {
            // Should not throw any exception
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method with various inputs.
     */
    @Test
    public void testTranslateVariousInputs() {
        CharSequenceTranslator translator = new IdentityTranslator();

        String input1 = "Hello, World!";
        String expected1 = "Hello, World!";

        try {
            String translated1 = translator.translate(input1);
            Assert.assertEquals(expected1, translated1);
        } catch (IOException e) {
            // Should not throw any exception
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }

        String input2 = "Hello, Øld ¢øld!";
        String expected2 = "Hello, Øld ¢øld!";

        try {
            String translated2 = translator.translate(input2);
            Assert.assertEquals(expected2, translated2);
        } catch (IOException e) {
            // Should not throw any exception
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }

        String input3 = "Hello, \uD800\uDC00 World!";
        String expected3 = "Hello, \uD800\uDC00 World!";

        try {
            String translated3 = translator.translate(input3);
            Assert.assertEquals(expected3, translated3);
        } catch (IOException e) {
            // Should not throw any exception
            Assert.fail("IOException should not be thrown: " + e.getMessage());
        }
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(StringUtils.EMPTY, null);
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...)}.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new UppercaseHexTranslator();

        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        String input = "Hello, World!";
        String expected = "HELLO, WORLD!";

        String translated = compositeTranslator.translate(input);
        Assert.assertEquals(expected, translated);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(Character.toChars(c)[0]);
        return Character.isSurrogate(c) ? 2 : 1;
    }
}

class UppercaseHexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        if (Character.isDigit(c) || Character.isLetter(c)) {
            String hex = Integer.toHexString(Character.codePointAt(input, index)).toUpperCase(Locale.ENGLISH);
            out.write(hex.toCharArray(), 0, hex.length());
            return 1;
        } else {
            out.write(Character.toChars(c)[0]);
            return 1;
        }
    }
}

class AggregateTranslator extends CharSequenceTranslator {

    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int length = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            length += consumed;
            index += consumed;
            if (length >= input.length()) {
                break;
            }
        }
        return length;
    }
}