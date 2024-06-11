import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new DummyCharSequenceTranslator();
    }

    @After
    public void tearDown() {
        translator = null;
    }

    @Test
    public void testTranslate_normalInput_shouldTranslateAndReturnCountOfCodepointsConsumed() throws IOException {
        // Arrange
        CharSequence input = "Hello World";
        Writer writer = new StringWriter();

        // Act
        int consumed = translator.translate(input, 0, writer);

        // Assert
        Assert.assertEquals(11, consumed);
        Assert.assertEquals("Translated!", writer.toString());
    }

    @Test
    public void testTranslate_inputWithNull_shouldNotTranslateAnything() throws IOException {
        // Arrange
        CharSequence input = null;
        Writer writer = new StringWriter();

        // Act
        int consumed = translator.translate(input, 0, writer);

        // Assert
        Assert.assertEquals(0, consumed);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_inputWithIndexOutOfBounds_shouldThrowIOException() throws IOException {
        // Arrange
        CharSequence input = "Hello World";
        Writer writer = new StringWriter();

        // Act and Assert
        try {
            int consumed = translator.translate(input, 100, writer);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Index out of bounds", e.getMessage());
        }
    }

    @Test
    public void testTranslate_nullWriter_shouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        CharSequence input = "Hello World";
        Writer writer = null;

        // Act and Assert
        try {
            int consumed = translator.translate(input, 0, writer);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The Writer must not be null", e.getMessage());
        }
    }

    @Test
    public void testTranslate_withSurrogatePair_shouldTranslateCorrectly() throws IOException {
        // Arrange
        CharSequence input = "Hi \uD83D\uDE00";
        Writer writer = new StringWriter();

        // Act
        int consumed = translator.translate(input, 0, writer);

        // Assert
        Assert.assertEquals(3, consumed);
        Assert.assertEquals("Translated!", writer.toString());
    }

    @Test
    public void testTranslate_helperMethod_withNullInput_shouldReturnNull() {
        // Arrange
        CharSequence input = null;

        // Act
        String result = translator.translate(input);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testTranslate_helperMethod_withNonEmptyInput_shouldTranslateAndReturnStringOutput() {
        // Arrange
        CharSequence input = "Hello World";

        // Act
        String result = translator.translate(input);

        // Assert
        Assert.assertEquals("Translated!", result);
    }

    @Test
    public void testTranslate_helperMethod_withEmptyInput_shouldReturnEmptyString() {
        // Arrange
        CharSequence input = "";

        // Act
        String result = translator.translate(input);

        // Assert
        Assert.assertEquals("", result);
    }

    @Test
    public void testTranslate_withMultipleTranslators_shouldMergeAndTranslateCorrectly() throws IOException {
        // Arrange
        CharSequence input = "Hello World";
        Writer writer = new StringWriter();
        CharSequenceTranslator translator1 = new DummyCharSequenceTranslator();
        CharSequenceTranslator translator2 = new AnotherDummyCharSequenceTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        // Act
        int consumed = mergedTranslator.translate(input, 0, writer);

        // Assert
        Assert.assertEquals(11, consumed);
        Assert.assertEquals("Merged and Translated!", writer.toString());
    }

    private class DummyCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write("Translated!");
                return 1;
            }
            return 0;
        }
    }

    private class AnotherDummyCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'e') {
                out.write("Merged and Translated!");
                return 1;
            }
            return 0;
        }
    }
}