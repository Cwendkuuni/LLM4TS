package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link CharSequenceTranslator} abstract class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the translate method of the abstract class.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = "Hello, 世界!";
        String expectedOutput = "Hello,\\uD841\\uDC00\\uD841\\uDC0D!";

        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for the translate method with error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = "Hello, 世界!";
        Writer out = null;

        translator.translate(input, 0, out);
    }

    /**
     * Test for the translate method with an empty CharSequence.
     */
    @Test
    public void testTranslateWithEmptyCharSequence() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = "";
        String expectedOutput = "";

        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for the with method.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator escapingTranslator = new EscapingTranslator();

        CharSequenceTranslator translator = new Translator().with(identityTranslator, escapingTranslator);

        CharSequence input = "Hello, 世界!";
        String expectedOutput = "Hello, 世界!\uD841\uDC00\uD841\uDC0D!";

        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for the translate method of the AggregateTranslator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator escapingTranslator = new EscapingTranslator();

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, escapingTranslator});

        CharSequence input = "Hello, 世界!";
        String expectedOutput = "Hello, 世界!\uD841\uDC00\uD841\uDC0D!";

        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for the translate method with a large input.
     */
    @Test
    public void testTranslateWithLargeInput() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = CharSequenceUtils.repeat("a", 100000);

        String expectedOutput = CharSequenceUtils.repeat("a", 100000);

        String actualOutput = translator.translate(input).toString();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for the translate method with a malformed input.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithMalformedInput() {
        CharSequenceTranslator translator = new Translator();
        CharSequence input = "Hello, 世界!";

        translator.translate(input, input.length(), new StringWriter());
    }
}

class Translator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }
}

class IdentityTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index < input.length()) {
            out.write(Character.codePointAt(input, index));
            return Character.charCount(Character.codePointAt(input, index));
        }
        return 0;
    }
}

class EscapingTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (Character.isHighSurrogate(Character.codePointAt(input, index))) {
            int next = index + 1;
            if (next < input.length() && Character.isLowSurrogate(Character.codePointAt(input, next))) {
                out.write(hex(Character.codePointAt(input, index)));
                out.write(hex(Character.codePointAt(input, next++)));
                return 2;
            }
        }
        out.write(Character.codePointAt(input, index));
        return Character.charCount(Character.codePointAt(input, index));
    }
}

class AggregateTranslator extends CharSequenceTranslator {
    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            if (consumed > 0) {
                totalConsumed += consumed;
                index += consumed;
            }
            if (index >= input.length()) {
                break;
            }
        }
        return totalConsumed;
    }
}