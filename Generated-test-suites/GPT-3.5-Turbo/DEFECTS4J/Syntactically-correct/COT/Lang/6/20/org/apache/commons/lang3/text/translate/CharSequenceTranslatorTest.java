import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_StringWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        StringWriter writer = new StringWriter();
        translator.translate("input", writer);

        // Assert
        Assert.assertEquals("output", writer.toString());
    }

    @Test
    public void testTranslate_String() {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        String output = translator.translate("input");

        // Assert
        Assert.assertEquals("output", output);
    }

    @Test
    public void testTranslate_NullInput() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);

        // Assert
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_NullString() {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        String output = translator.translate(null);

        // Assert
        Assert.assertNull(output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NullWriter() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        translator.translate("input", null);
    }

    @Test
    public void testWith_OneTranslator() {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();
        CharSequenceTranslator[] translators = {translator};

        // Act
        CharSequenceTranslator mergedTranslator = translator.with(translators);

        // Assert
        Assert.assertNotNull(mergedTranslator);
        Assert.assertEquals(translator, mergedTranslator);
    }

    @Test
    public void testHex() {
        // Arrange
        int codepoint = 65;

        // Act
        String hex = CharSequenceTranslator.hex(codepoint);

        // Assert
        Assert.assertEquals("41", hex);
    }

    // Custom Translator for testing
    public static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'i' && input.charAt(index + 1) == 'n') {
                out.write("output");
                return 2;
            }
            return 0;
        }
    }
}