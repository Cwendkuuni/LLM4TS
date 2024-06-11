package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator abstract class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for default translator.
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new Translator();
        String input = "Hello, 世界!";
        String expectedOutput = "Hello, \\u4E16\\u754C!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for custom translator with identity and hex translators.
     */
    @Test
    public void testCustomTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, hexTranslator});
        String input = "Hello, 世界!";
        String expectedOutput = "Hello, 世界!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);

        String inputWithCodepoints = "\u0048\u0065\u006c\u006c\u006f\u002C\u4E16\u754C\u0021";
        String expectedOutputWithCodepoints = "\u0048\u0065\u006c\u006c\u006f\u002C\u4E16\u754C\u0021";
        String actualOutputWithCodepoints = translator.translate(inputWithCodepoints);
        assertEquals(expectedOutputWithCodepoints, actualOutputWithCodepoints);

        String inputWithSurrogatePair = "\uD87E\uDD1E";
        String expectedOutputWithSurrogatePair = "\uD87E\uDD1E";
        String actualOutputWithSurrogatePair = translator.translate(inputWithSurrogatePair);
        assertEquals(expectedOutputWithSurrogatePair, actualOutputWithSurrogatePair);

        String inputWithInvalidCodepoint = "\uFFFF";
        try {
            actualOutputWithInvalidCodepoint = translator.translate(inputWithInvalidCodepoint);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            // Expected
        }

        String inputWithNull = null;
        try {
            actualOutputWithNull = translator.translate(inputWithNull);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    /**
     * Test for null translator.
     */
    @Test(expected = NullPointerException.class)
    public void testNullTranslator() {
        CharSequenceTranslator translator = null;
        String input = "Hello, 世界!";
        translator.translate(input);
    }
}

class Translator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);

        switch (c) {
            case '\u0025':
                out.write("\\u0025");
                break;
            default:
                out.write(c);
                break;
        }
        return Character.charCount(c);
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        out.write(c);
        return Character.charCount(c);
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.codePointAt(input, index);
        String hex = CharSequenceTranslator.hex((int) c);
        out.write(hex);
        return Character.charCount(c);
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = 0;
        for (CharSequenceTranslator translator : translators) {
            try {
                consumed += translator.translate(input, index, out);
                break;
            } catch (IOException e) {
                // Catch and ignore, next translator will be tried
            }
        }
        if (consumed == 0) {
            char c = Character.codePointAt(input, index);
            out.write(c);
            consumed += Character.charCount(c);
        }
        return consumed;
    }
}