package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();

        // Act
        String result = translator.translate("test");

        // Assert
        assertEquals("Translation result is incorrect.", "test", result);
    }

    @Test
    public void testTranslateWithNullInput() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();

        // Act
        String result = translator.translate(null);

        // Assert
        assertEquals("Translation result is incorrect.", null, result);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();

        // Act
        String result = translator.translate("");

        // Assert
        assertEquals("Translation result is incorrect.", "", result);
    }

    @Test
    public void testTranslateWithIOException() {
        // Arrange
        CharSequenceTranslator translator = new FailingTranslator();

        // Act and Assert
        try {
            translator.translate("test");
        } catch (IOException e) {
            assertEquals("IOException was not thrown.", "Test IOException", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNullWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();

        // Act and Assert
        translator.translate("test", null);
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

    private static class FailingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("Test IOException");
        }
    }
}