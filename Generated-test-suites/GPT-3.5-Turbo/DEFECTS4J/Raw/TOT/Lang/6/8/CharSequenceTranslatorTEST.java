import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import java.io.IOException;
import java.io.StringWriter;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateWithNullInput() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();

        // Act
        String result = translator.translate(null);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testTranslateWithValidInput() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello World";

        // Act
        String result = translator.translate(input);

        // Assert
        Assert.assertEquals(input, result);
    }

    @Test
    public void testTranslateWithInvalidReader() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello";
        StringWriter writer = null;

        // Act and Assert
        try {
            translator.translate(input, writer);
            Assert.fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // Exception was expected
            Assert.assertNull(writer);
        }
    }

    @Test
    public void testTranslateWithValidReader() {
        // Arrange
        CharSequenceTranslator translator = new DummyTranslator();
        String input = "Hello";
        StringWriter writer = new StringWriter();

        // Act and Assert
        try {
            translator.translate(input, writer);
            Assert.assertEquals(input, writer.toString());
        } catch (IOException e) {
            Assert.fail("IOException was thrown unexpectedly.");
        }
    }

    @Test
    public void testWithTranslators() {
        // Arrange
        CharSequenceTranslator translator1 = new DummyTranslator();
        CharSequenceTranslator translator2 = new DummyTranslator();

        // Act
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);

        // Assert
        Assert.assertNotNull(combinedTranslator);
    }

    @Test
    public void testHexConversion() {
        // Arrange
        int codepoint = 65; // ASCII value of 'A'

        // Act
        String hex = CharSequenceTranslator.hex(codepoint);

        // Assert
        Assert.assertEquals("41", hex);
    }

    private static class DummyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (out == null) {
                throw new IllegalArgumentException("The Writer must not be null");
            }

            if (input == null) {
                return 0;
            }

            if (index >= input.length()) {
                return 0;
            }

            out.write(input.charAt(index));
            return 1;
        }
    }

}