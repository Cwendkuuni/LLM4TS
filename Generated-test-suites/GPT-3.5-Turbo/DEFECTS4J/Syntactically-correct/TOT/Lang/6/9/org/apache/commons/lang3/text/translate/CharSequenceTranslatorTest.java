import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_shouldTranslateInputToWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        String expected = "Hello, world!";
        StringWriter writer = new StringWriter();
        StringBuffer input = new StringBuffer(expected);

        // Act
        translator.translate(input, writer);

        // Assert
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testTranslate_shouldReturnTranslatedString() {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return 1;
            }
        };
        String expected = "Hello, world!";
        StringBuffer input = new StringBuffer(expected);

        // Act
        String actual = translator.translate(input);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withNullWriter_shouldThrowIllegalArgumentException() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        StringBuffer input = new StringBuffer("Hello, world!");

        // Act
        translator.translate(input, null);
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Act
        String actual = translator.translate(null);

        // Assert
        Assert.assertNull(actual);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Act
        String actual = translator.translate("");

        // Assert
        Assert.assertEquals("", actual);
    }

    @Test
    public void testTranslate_withCodePointConsumption_shouldReturnTranslatedString() {
        // Arrange
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.charAt(index) == 'a') {
                    out.write('A');
                    return 1;
                } else if (input.charAt(index) == 'b') {
                    out.write('B');
                    return 2;
                } else {
                    out.write(input.charAt(index));
                    return 1;
                }
            }
        };
        String expected = "ABca";
        StringWriter writer = new StringWriter();
        StringBuffer input = new StringBuffer(expected);

        // Act
        translator.translate(input, writer);

        // Assert
        Assert.assertEquals(expected, writer.toString());
    }

    @Test
    public void testWith_shouldReturnMergedTranslator() {
        // Arrange
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };

        // Act
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        // Assert
        Assert.assertTrue(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex_shouldReturnUpperCaseHexadecimalString() {
        // Arrange
        int codepoint = 65;
        String expected = "41";

        // Act
        String actual = CharSequenceTranslator.hex(codepoint);

        // Assert
        Assert.assertEquals(expected, actual);
    }
}