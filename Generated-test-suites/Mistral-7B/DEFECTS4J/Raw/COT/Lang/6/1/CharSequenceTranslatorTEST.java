package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.InvalidCharSequenceInputException;
import org.junit.Test;

/**
 * JUnit test file for CharSequenceTranslator class
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method with valid input and Writer
     *
     * @throws IOException
     */
    @Test
    public void testTranslate() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String input = "Hello World!";
        int expectedConsumed = input.length();
        int actualConsumed = translator.translate(new StringSource(input), 0, writer);
        String actualOutput = writer.toString();
        assertEquals(input, actualOutput);
        assertEquals(expectedConsumed, actualConsumed);
    }

    /**
     * Test for translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        translator.translate(null, 0, writer);
    }

    /**
     * Test for translate method with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        translator.translate(new StringSource("Test"), 0, null);
    }

    /**
     * Test for translate method with empty input
     */
    @Test
    public void testTranslateEmptyInput() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String input = "";
        int expectedConsumed = 0;
        int actualConsumed = translator.translate(new StringSource(input), 0, writer);
        assertEquals("", writer.toString());
        assertEquals(expectedConsumed, actualConsumed);
    }

    /**
     * Test for translate method with InvalidCharSequenceInputException
     */
    @Test(expected = InvalidCharSequenceInputException.class)
    public void testTranslateInvalidInputException() throws IOException {
        CharSequenceTranslator translator = new DefaultCharSequenceTranslator();
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String input = "INVALID CHAR SEQUENCE";
        translator.translate(new StringSource(input), 0, writer);
    }

    /**
     * Test for translate method with custom translators using AggregateTranslator
     *
     * @throws IOException
     */
    @Test
    public void testTranslateAggregateTranslator() throws IOException {
        CharSequenceTranslator translator1 = new MyCustomTranslator1();
        CharSequenceTranslator translator2 = new MyCustomTranslator2();
        CharSequenceTranslator aggregateTranslator = new AggregateTranslator(translator1, translator2);
        Writer writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String input = "Hello World!";
        int expectedConsumed = input.length();
        int actualConsumed = aggregateTranslator.translate(new StringSource(input), 0, writer);
        String actualOutput = writer.toString();
        assertEquals(input, actualOutput);
        assertEquals(expectedConsumed, actualConsumed);
    }

    /**
     * Test for hex method
     */
    @Test
    public void testHex() {
        int codepoint = 0x0061; // 'a'
        String expectedHex = "41";
        String actualHex = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedHex, actualHex);
    }

}

class StringSource implements CharSequence {
    private final String cs;

    public StringSource(String cs) {
        this.cs = cs;
    }

    @Override
    public int length() {
        return cs.length();
    }

    @Override
    public char charAt(int index) {
        return cs.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringSource(cs.subSequence(start, end));
    }
}

class MyCustomTranslator1 implements CharSequenceTranslator {
    @Override
    public int translate(CharSequence cs, int index, Writer writer) throws IOException {
        int consumed = 0;
        if (cs.charAt(index) == 'H') {
            writer.write("Hello");
            consumed += 5;
        } else {
            consumed++;
            writer.write(cs.charAt(index));
        }
        return consumed;
    }
}

class MyCustomTranslator2 implements CharSequenceTranslator {
    @Override
    public int translate(CharSequence cs, int index, Writer writer) throws IOException {
        int consumed = 0;
        if (cs.charAt(index) == 'W') {
            writer.write("World!");
            consumed += 6;
        } else {
            consumed++;
            writer.write(cs.charAt(index));
        }
        return consumed;
    }
}