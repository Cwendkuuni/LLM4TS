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
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.EmptyTranslator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator emptyTranslator;
    private CharSequenceTranslator defaultTranslator;
    private CharSequenceTranslator aggregateTranslator;

    @Before
    public void setUp() {
        emptyTranslator = new EmptyTranslator();
        defaultTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toChars(Character.codePointAt(input, index)));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        aggregateTranslator = emptyTranslator.with(defaultTranslator);
    }

    @Test
    public void testTranslate_NullInput() {
        assertThrows(NullPointerException.class, () -> defaultTranslator.translate(null));
    }

    @Test
    public void testTranslate_EmptyInput() {
        assertEquals("", defaultTranslator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testTranslate_Basic() {
        String input = "Hello World!";
        String expectedOutput = "48656c6c6f20576f726c6421";
        String output = defaultTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateToWriter_NullWriter() {
        assertThrows(NullPointerException.class, () -> defaultTranslator.translate(CharSequence.class.cast(""), null));
    }

    @Test
    public void testTranslateToWriter_EmptyWriter() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        defaultTranslator.translate(CharSequence.class.cast(""), writer);
        assertEquals("", writer.get(0, (int) os.size()));
    }

    @Test
    public void testTranslateToWriter_Basic() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        String input = "Hello World!";
        int consumed = defaultTranslator.translate(input, 0, writer);
        assertEquals("Hello World!", input.subSequence(0, consumed));
        assertEquals("48656c6c6f20576f726c6421", writer.get(0, (int) os.size()));
    }

    @Test
    public void testAggregateTranslator_EmptyInput() {
        assertEquals("", aggregateTranslator.translate(CharSequence.class.cast("")));
    }

    @Test
    public void testAggregateTranslator_Basic() {
        String input = "Hello World!";
        String expectedOutput = "48656c6c6f20576f726c6421";
        String output = aggregateTranslator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IOException.class)
    public void testAggregateTranslator_IOException() throws IOException {
        Writer out = new OutputStreamWriter(System.out) {
            @Override
            public void write(int c, int length) throws IOException {
                throw new IOException();
            }
        };
        aggregateTranslator.translate(CharSequence.class.cast(""), out);
    }
}