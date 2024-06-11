package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * JUnit 4 test cases for {@link LookupTranslator} class
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslatorConstruction() {
        // Test empty constructor
        LookupTranslator emptyLookupTranslator = new LookupTranslator();

        // Test constructor with null lookup
        LookupTranslator nullLookupTranslator = new LookupTranslator(null);

        // Test constructor with empty lookup
        CharSequence[][] emptyLookup = new CharSequence[0][];
        LookupTranslator emptyLookupTranslatorWithLookup = new LookupTranslator(emptyLookup);

        // Test constructor with valid lookup
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);

        // Check that the constructor sets the shortest and longest length properly
        assertEquals(0, emptyLookupTranslator.shortest);
        assertEquals(0, emptyLookupTranslator.longest);
        assertEquals(5, validLookupTranslator.shortest);
        assertEquals(6, validLookupTranslator.longest);
    }

    @Test
    public void testEmptyTranslation() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast(""), 0, writer);
        assertEquals(0, result);
        assertNull(outputStream.toString());
    }

    @Test
    public void testTranslationWithShortestMatch() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast("hello"), 0, writer);
        assertEquals(5, result);
        assertEquals("hi", outputStream.toString());
    }

    @Test
    public void testTranslationWithLongestMatch() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast("helloooo"), 0, writer);
        assertEquals(6, result);
        assertEquals("greetings", outputStream.toString());
    }

    @Test
    public void testTranslationWithNoMatch() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookuptTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast("testtest"), 0, writer);
        assertEquals(0, result);
        assertNull(outputStream.toString());
    }

    @Test
    public void testTranslationWithPartialMatch() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast("testtesthello"), 0, writer);
        assertEquals(5, result);
        assertEquals("yes", outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnTranslation() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        validLookupTranslator.translate(CharSequence.class.cast("hello"), 0, writer);
    }

    @Test
    public void testTranslationWithNullInput() throws IOException {
        CharSequence[][] validLookup = {{"hello", "hi"}, {"world", "greetings"}, {"test", "yes"}};
        LookupTranslator validLookupTranslator = new LookupTranslator(validLookup);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(null, 0, writer);
        assertEquals(0, result);
        assertNull(outputStream.toString());
    }

    @Test
    public void testTranslationWithNullLookup() throws IOException {
        LookupTranslator validLookupTranslator = new LookupTranslator(null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // NOP
            }

            @Override
            public void close() throws IOException {
                // NOP
            }
        };

        int result = validLookupTranslator.translate(CharSequence.class.cast("hello"), 0, writer);
        assertEquals(0, result);
        assertNull(outputStream.toString());
    }
}