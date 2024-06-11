
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withValidInput_shouldTranslateText() {
        // Arrange
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Act
        String result = translator.translate("Hello World!");

        // Assert
        assertEquals("Translated text", "Hello World!", result);
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        // Arrange
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Act
        String result = translator.translate(null);

        // Assert
        assertNull("Null input should return null", result);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        // Arrange
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Act
        String result = translator.translate("");

        // Assert
        assertEquals("Empty input should return empty string", "", result);
    }

    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        // Arrange
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Act & Assert
        assertThrows("IOException should throw RuntimeException", RuntimeException.class, () ->
                translator.translate(new MockCharSequenceWithIOException()));
    }

    @Test
    public void testTranslate_withTranslatorThrowingException_shouldCatchAndContinue() {
        // Arrange
        CharSequenceTranslator translator = new FaultyCharSequenceTranslator();

        // Act
        String result = translator.translate("Hello World!");

        // Assert
        assertEquals("Faulty translator should catch and continue", "Hello World!", result);
    }

    @Test
    public void testTranslate_withSurrogatePairInput_shouldTranslateText() {
        // Arrange
        CharSequenceTranslator translator = new MockCharSequenceTranslator();

        // Act
        String result = translator.translate("Hello \uD83D\uDC4B\uD83D\uDE0E");

        // Assert
        assertEquals("Translated text with surrogate pair", "Hello \uD83D\uDC4B\uD83D\uDE0E", result);
    }

    private static class MockCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.subSequence(index, input.length()).toString());
            return input.length() - index;
        }
    }

    private static class MockCharSequenceWithIOException implements CharSequence {

        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            throw new IOException("IOException");
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }

    private static class FaultyCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("IOException");
        }
    }
}
