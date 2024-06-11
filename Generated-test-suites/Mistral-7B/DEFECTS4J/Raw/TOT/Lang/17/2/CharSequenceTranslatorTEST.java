package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test basic functionality of {@link CharSequenceTranslator}.
     */
    @Test
    public void testBasicFunctionality() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test handling of null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null, 0, new StringWriter());
    }

    /**
     * Test handling of negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndex() {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, -1, new StringWriter());
    }

    /**
     * Test handling of null writer output.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriterOutput() {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(input, 0, null);
    }

    /**
     * Test basic functionality of {@link AggregateTranslator}.
     */
    @Test
    public void testAggregateFunctionality() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, uppercaseTranslator});
        String input = "Hello, World!";
        String expected = "HELLO, World!";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test handling of null input in {@link AggregateTranslator}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAggregateNullInput() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, uppercaseTranslator});
        translator.translate(null, 0, new StringWriter());
    }

    /**
     * Test handling of negative index in {@link AggregateTranslator}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAggregateNegativeIndex() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, uppercaseTranslator});
        String input = "Hello, World!";
        translator.translate(input, -1, new StringWriter());
    }

    /**
     * Test handling of null writer output in {@link AggregateTranslator}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAggregateNullWriterOutput() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[]{identityTranslator, uppercaseTranslator});
        String input = "Hello, World!";
        translator.translate(input, 0, null);
    }

    /**
     * Test custom translator functionality.
     */
    @Test
    public void testCustomTranslatorFunctionality() {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new CustomTranslator();
        String expected = "H*E*L*L*O, *W*O*R*L*D*!";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test custom translator handling of null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCustomTranslatorNullInput() {
        CharSequenceTranslator translator = new CustomTranslator();
        translator.translate(null, 0, new StringWriter());
    }

    /**
     * Test custom translator handling of negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCustomTranslatorNegativeIndex() {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new CustomTranslator();
        translator.translate(input, -1, new StringWriter());
    }

    /**
     * Test custom translator handling of null writer output.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCustomTranslatorNullWriterOutput() {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new CustomTranslator();
        translator.translate(input, 0, null);
    }

    /**
     * Custom translator for testing purposes.
     */
    private static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || out == null) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            switch (c) {
                case 'H':
                    out.write("H*");
                    break;
                case 'e':
                    out.write("E*");
                    break;
                case 'l':
                    out.write("L*");
                    break;
                case 'l':
                    out.write("L*");
                    break;
                case 'o':
                    out.write("O*");
                    break;
                case ' ':
                    out.write(" ");
                    break;
                case 'W':
                    out.write("W*");
                    break;
                case 'r':
                    out.write("R*");
                    break;
                case 'l':
                    out.write("L*");
                    break;
                case 'd':
                    out.write("D*");
                    break;
                case '!':
                    out.write("!*");
                    break;
                default:
                    out.write(Character.forDigit(Character.codePointAt(input, index) >>> 4, 16));
                    out.write(Character.forDigit(Character.codePointAt(input, index) & 0xf, 16));
            }
            return 1;
        }
    }
}