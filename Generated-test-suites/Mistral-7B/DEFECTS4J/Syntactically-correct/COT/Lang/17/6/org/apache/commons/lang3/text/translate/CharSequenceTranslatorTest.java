package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.NoOpTranslator;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator NO_OP_TRANSLATOR = new NoOpTranslator();
    private static final CharSequenceTranslator AGGREGATE_TRANSLATOR = new AggregateTranslator(new CharSequenceTranslator[] {NO_OP_TRANSLATOR, NO_OP_TRANSLATOR});

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslate() throws Exception {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);
        StringWriter writer = new StringWriter();

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(input, 0, writer);
        assertEquals(0, consumed);
        assertEquals(testString, writer.toString());

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(input, 0, writer);
        assertEquals(testString.length(), consumed);
        assertEquals(testString, writer.toString());

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @org.junit.Test
    public void testTranslateWithStringWriter() {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);

        // Test with no-op translator
        String result = NO_OP_TRANSLATOR.translate(input);
        assertEquals(testString, result);

        // Test with aggregate translator
        result = AGGREGATE_TRANSLATOR.translate(input);
        assertEquals(testString, result);

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslateWithWriter() throws Exception {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);
        Writer output = new StringWriter();

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(input, 0, output);
        assertEquals(0, consumed);
        assertEquals(testString, output.toString());

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(input, 0, output);
        assertEquals(testString.length(), consumed);
        assertEquals(testString, output.toString());

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}
     * with an empty CharSequence
     * @throws Exception
     */
    @org.junit.Test
    public void testTranslateWithEmptyCharSequence() throws Exception {
        StringWriter writer = new StringWriter();

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(CharSequence.of(""), 0, writer);
        assertEquals(0, consumed);
        assertEquals( "", writer.toString() );

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(CharSequence.of(""), 0, writer);
        assertEquals(0, consumed);
        assertEquals( "", writer.toString() );

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with an empty CharSequence
     */
    @org.junit.Test
    public void testTranslateWithEmptyCharSequenceAndStringWriter() {
        Writer output = new StringWriter();

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(CharSequence.of(""), output);
        assertEquals(0, consumed);
        assertEquals( "", output.toString() );

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(CharSequence.of(""), output);
        assertEquals(0, consumed);
        assertEquals( "", output.toString() );

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with an IOException
     * @throws Exception
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testTranslateWithIOException() throws Exception {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);
        Writer output = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(input, 0, output);
        fail("IOException should have been thrown");

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(input, 0, output);
        fail("IOException should have been thrown");

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     * with an IOException
     */
    @org.junit.Test(expected = RuntimeException.class)
    public void testTranslateWithStringWriterAndIOException() {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };

        // Test with no-op translator
        String result = NO_OP_TRANSLATOR.translate(CharSequence.of(""));
        fail("IOException should have been thrown");

        // Test with aggregate translator
        result = AGGREGATE_TRANSLATOR.translate(CharSequence.of(""));
        fail("IOException should have been thrown");

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     * with a null Writer
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);

        // Test with no-op translator
        int consumed = NO_OP_TRANSLATOR.translate(input, 0, null);
        fail("IllegalArgumentException should have been thrown");

        // Test with aggregate translator
        consumed = AGGREGATE_TRANSLATOR.translate(input, 0, null);
        fail("IllegalArgumentException should have been thrown");

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     * with a null Writer
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriterAndStringWriter() {
        String testString = "Hello World!";
        CharSequence input = CharSequence.of(testString);

        // Test with no-op translator
        String result = NO_OP_TRANSLATOR.translate(input);
        fail("IllegalArgumentException should have been thrown");

        // Test with aggregate translator
        result = AGGREGATE_TRANSLATOR.translate(input);
        fail("IllegalArgumentException should have been thrown");

        // Test with custom translator (not provided in the original code)
        // Add your custom test case here
    }
}