package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new TestTranslator();
    }

    @Test
    public void testTranslate_ValidInput_TranslationSuccessful() throws IOException {
        // Arrange
        String input = "Hello, world!";
        String expectedOutput = "Hello! world";

        // Act
        String actualOutput = translator.translate(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslate_NullInput_ReturnsNull() throws IOException {
        // Arrange
        String input = null;

        // Act
        String actualOutput = translator.translate(input);

        // Assert
        assertNull(actualOutput);
    }

    @Test
    public void testTranslate_EmptyInput_ReturnsEmptyString() throws IOException {
        // Arrange
        String input = "";

        // Act
        String actualOutput = translator.translate(input);

        // Assert
        assertEquals("", actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter_ThrowsIllegalArgumentException() throws IOException {
        // Arrange
        String input = "Hello, world!";
        Writer writer = null;

        // Act
        translator.translate(input, writer);

        // Assert
        // Exception expected
    }

    @Test
    public void testTranslate_IOExceptionThrownByWriter_RuntimeExceptionThrown() {
        // Arrange
        String input = "Hello, world!";
        Writer writer = new TestWriter();

        // Act & Assert
        assertThrows(RuntimeException.class, () -> translator.translate(input, writer));
    }

    @Test
    public void testTranslate_IndexOutOfBounds_ReturnsZero() throws IOException {
        // Arrange
        String input = "Hello, world!";
        int index = 100;
        StringWriter writer = new StringWriter();

        // Act
        int consumed = translator.translate(input, index, writer);

        // Assert
        assertEquals(0, consumed);
    }

    @Test
    public void testWith_MergingTranslators_TranslationSuccessful() throws IOException {
        // Arrange
        CharSequenceTranslator translator2 = new TestTranslator2();
        CharSequenceTranslator mergedTranslator = translator.with(translator2);
        String input = "Hello, world!";
        String expectedOutput = "Hello!!!!!!!!!! world";

        // Act
        String actualOutput = mergedTranslator.translate(input);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                char c = input.charAt(index);
                if (c == ',') {
                    out.write("!");
                    return 1;
                }
            }
            return 0;
        }
    }

    private static class TestTranslator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index < input.length()) {
                char c = input.charAt(index);
                if (c == '!') {
                    out.write("!!");
                    return 1;
                }
            }
            return 0;
        }
    }

    private static class TestWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }
        @Override
        public void flush() throws IOException {
        }
        @Override
        public void close() throws IOException {
        }
    }
}