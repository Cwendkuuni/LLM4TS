import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate1() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        String result = translator.translate("Hello");

        // Assert
        assertEquals("Translated text is incorrect", "Hello", result);
    }

    @Test
    public void testTranslate2() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new TestTranslator();

        // Act
        String result = translator.translate("Hello World!");

        // Assert
        assertEquals("Translated text is incorrect", "Hello World!", result);
    }

    private static class TestTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.charAt(index));
            return 1;
        }
    }

}