package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Test cases for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method of abstract CharSequenceTranslator class.
     */
    @Test
    public void testAbstractTranslateMethod() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "Test Input sequence";
        Writer out = new StringWriter();
        int expectedConsumed = input.length();

        int actualConsumed = translator.translate(input, 0, out);
        String actualOutput = out.toString();

        assertEquals("Unexpected output from translate method", input.toString(), actualOutput);
        assertEquals("Unexpected number of codepoints consumed", expectedConsumed, actualConsumed);
    }

    /**
     * Test translate method with null input and Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullInput() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = null;
        Writer out = new StringWriter();

        translator.translate(input, out);
    }

    /**
     * Test translate method with null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "Test Input sequence";
        Writer out = null;

        translator.translate(input, out);
    }

    /**
     * Test translate method with IOException.
     */
    @Test(expected = IOException.class)
    public void testTranslateWithIOException() throws IOException {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "Test Input sequence";
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Writer should not call write method");
            }
        };

        translator.translate(input, 0, out);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateWithEmptyInput() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "";
        Writer out = new StringWriter();
        int expectedConsumed = 0;

        int actualConsumed = translator.translate(input, 0, out);
        String actualOutput = out.toString();

        assertEquals("Unexpected output from translate method", "", actualOutput);
        assertEquals("Unexpected number of codepoints consumed", expectedConsumed, actualConsumed);
    }

    /**
     * Test translate method with non-empty input.
     */
    @Test
    public void testTranslateWithNonEmptyInput() {
        DefaultTranslator translator = new DefaultTranslator();
        CharSequence input = "Test Input sequence";
        Writer out = new StringWriter();
        int expectedConsumed = input.length();

        int actualConsumed = translator.translate(input, 0, out);
        String actualOutput = out.toString();

        assertEquals("Unexpected output from translate method", input.toString(), actualOutput);
        assertEquals("Unexpected number of codepoints consumed", expectedConsumed, actualConsumed);
    }

    /**
     * Test translate method with custom translator and input.
     */
    @Test
    public void testTranslateWithCustomTranslator() {
        DefaultTranslator translator1 = new DefaultTranslator();
        AggregateTranslator translator2 = new AggregateTranslator(new CharSequenceTranslator[] {translator1, new EscapeHtmlTranslator()});
        CharSequence input = "Test <input> sequence";
        Writer out = new StringWriter();
        int expectedConsumed = input.length() + 16; // +16 for escaping < and >

        int actualConsumed = translator2.translate(input, 0, out);
        String actualOutput = out.toString();

        String expectedOutput = "Test &lt;input&gt; sequence";
        assertEquals("Unexpected output from translate method", expectedOutput, actualOutput);
        assertEquals("Unexpected number of codepoints consumed", expectedConsumed, actualConsumed);
    }

}