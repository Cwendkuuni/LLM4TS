package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.NoOpCharSequenceTranslator;
import org.junit.Test;

/**
 * JUnit 4 test cases for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for abstract translate method with Writer
     * @throws IOException
     */
    @Test
    public void testAbstractTranslateMethodWithWriter() throws IOException {
        // Create a mock translator
        CharSequenceTranslator mockTranslator = new MockCharSequenceTranslator();
        // Test translate method with Writer
        CharSequence input = CharUtils.toCharSequence("TestInput");
        Writer out = new StringWriter();
        int expectedConsumed = 6;
        int actualConsumed = mockTranslator.translate(input, 0, out);
        assertEquals("Expected 6 consumed, but got " + actualConsumed, expectedConsumed, actualConsumed);
        assertEquals("Expected 'TestInput' to be translated to 'TestInput', but got " + out.toString(),
                "TestInput", out.toString());
    }

    /**
     * Test for translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethodWithNullInput() {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        charSequenceTranslator.translate(null);
    }

    /**
     * Test for translate method with null Writer
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateMethodWithNullWriter() throws IOException {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        charSequenceTranslator.translate(CharUtils.toCharSequence("TestInput"), null);
    }

    /**
     * Test for translate method with empty input
     */
    @Test
    public void testTranslateMethodWithEmptyInput() {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        String expectedOutput = "";
        String actualOutput = charSequenceTranslator.translate(CharUtils.toCharSequence(""));
        assertEquals("Expected empty input to be translated to '' but got " + actualOutput, expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with a single character input
     */
    @Test
    public void testTranslateMethodWithSingleCharacterInput() {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        char c = 'A';
        String expectedOutput = String.valueOf(c);
        String actualOutput = charSequenceTranslator.translate(CharUtils.toCharSequence(String.valueOf(c)));
        assertEquals("Expected single character input to be translated to " + expectedOutput + " but got " + actualOutput,
                expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with multiple character input
     */
    @Test
    public void testTranslateMethodWithMultipleCharacterInput() {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        CharSequence input = CharUtils.toCharSequence("TestInput");
        String expectedOutput = "TestInput";
        String actualOutput = charSequenceTranslator.translate(input);
        assertEquals("Expected 'TestInput' to be translated to 'TestInput' but got " + actualOutput, expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with surrogate pair
     */
    @Test
    public void testTranslateMethodWithSurrogatePair() {
        CharSequenceTranslator charSequenceTranslator = new NoOpCharSequenceTranslator();
        CharSequence input = CharUtils.toCharSequence("\uD800\uDC00");
        String expectedOutput = "\uD800\uDC00";
        String actualOutput = charSequenceTranslator.translate(input).toString();
        assertEquals("Expected '\uD800\uDC00' to be translated to '\uD800\uDC00' but got " + actualOutput, expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with multiple translators using AggregateTranslator
     * @throws IOException
     */
    @Test
    public void testTranslateMethodWithMultipleTranslatorsUsingAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('1');
                return 1;
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write('2');
                return 1;
            }
        };
        CharSequenceTranslator aggregateTranslator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});

        CharSequence input = CharUtils.toCharSequence("TestInput");
        Writer out = new StringWriter();
        int expectedConsumed = 6;
        int actualConsumed = aggregateTranslator.translate(input, 0, out).length();
        assertEquals("Expected 6 consumed, but got " + actualConsumed, expectedConsumed, actualConsumed);
        assertEquals("Expected 'TestInput' to be translated to '11121112' but got " + out.toString(),
                "11121112", out.toString());
    }

    /**
     * Test for hex method
     */
    @Test
    public void testHexMethod() {
        int codepoint = 0x1234;
        String expectedOutput = "1234";
        String actualOutput = hex(codepoint);
        assertEquals("Expected hex method to return '1234' but got " + actualOutput, expectedOutput, actualOutput);
    }

    /**
     * Test for with method
     */
    @Test
    public void testWithMethod() {
        CharSequenceTranslator translator1 = new NoOpCharSequenceTranslator();
        CharSequenceTranslator translator2 = new NoOpCharSequenceTranslator();
        CharSequenceTranslator expectedOutput = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});
        CharSequenceTranslator actualOutput = new NoOpCharSequenceTranslator().with(translator1, translator2);
        assertEquals("Expected 'translator1' and 'translator2' to be merged using with method to create 'expectedOutput' but got " + actualOutput,
                expectedOutput, actualOutput);
    }
}

class MockCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int consumed = super.translate(input, index, out);
        if (consumed == 0) {
            out.write(Character.toChars(Character.codePointAt(input, index))[0]);
        }
        return consumed;
    }
}

class NoOpCharSequenceTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        return 0;
    }
}