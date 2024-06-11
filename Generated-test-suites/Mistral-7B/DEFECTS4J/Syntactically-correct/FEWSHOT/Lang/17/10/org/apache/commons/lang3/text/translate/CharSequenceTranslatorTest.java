package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.DefaultTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * JUnit 4 test cases for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with default translator.
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello, 世界!";
        String expected = "Hello,\\uD840\\uDC00\\uD840\\uDC0D!";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test translate method with custom translator.
     */
    @Test
    public void testCustomTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index < input.length() && Character.isDigit(Character.codePointAt(input, index))) {
                    int codePoint = Character.codePointAt(input, index);
                    String hex = CharSequenceTranslator.hex(codePoint);
                    out.write(hex);
                    return 2; // consume 2 code points for each digit
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };
        String input = "123abc";
        String expected = "313233\\u0061\\u0062\\u0063";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test translate method with multiple translators.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator defaultTranslator = new DefaultTranslator();
        CharSequenceTranslator hexTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index < input.length() && Character.isDigit(Character.codePointAt(input, index))) {
                    int codePoint = Character.codePointAt(input, index);
                    String hex = CharSequenceTranslator.hex(codePoint);
                    out.write(hex);
                    return 2; // consume 2 code points for each digit
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };
        CharSequenceTranslator customTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index < input.length() && Character.isUpperCase(Character.codePointAt(input, index))) {
                    char c = Character.codePointAt(input, index);
                    out.write(StringUtils.reverse(Character.name(c)).charAt(0));
                    return Character.charCount(c);
                } else {
                    out.write(Character.codePointAt(input, index));
                    return Character.charCount(Character.codePointAt(input, index));
                }
            }
        };
        CharSequenceTranslator translator = defaultTranslator.with(hexTranslator, customTranslator);
        String input = "1A1B2C";
        String expected = "C2B1A1\\u0041\\u0042\\u0043";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    /**
     * Test null input.
     */
    @Test
    public void testNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String result = translator.translate((CharSequence) null);
        assertNull(result);
    }
}