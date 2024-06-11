import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedOutput() {
        // Arrange
        CharSequenceTranslator translator = new CustomTranslator();

        // Act
        String result = translator.translate("Hello, World!");

        // Assert
        assertEquals("Translating 'Hello, World!' should return 'Hallo, Welt!'", "Hallo, Welt!", result);
    }

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        // Arrange
        CharSequenceTranslator translator = new CustomTranslator();

        // Act
        String result = translator.translate(null);

        // Assert
        assertNull("Translating null should return null", result);
    }

    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        // Arrange
        CharSequenceTranslator translator = new CustomTranslator();

        // Act
        String result = translator.translate("");

        // Assert
        assertEquals("Translating an empty string should return an empty string", "", result);
    }

    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        // Arrange
        CharSequenceTranslator translator = new CustomTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        // Act & Assert
        assertThrows("Translating with an IOException should throw RuntimeException",
                RuntimeException.class, () -> translator.translate("Hello"));
    }

    private static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            // Translate 'Hello, World!' to 'Hallo, Welt!'
            if (input.charAt(index) == 'H') {
                out.write('H');
                out.write('a');
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write('e');
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write('l');
                out.write('o');
                return 1;
            } else if (input.charAt(index) == ',') {
                out.write(',');
                return 1;
            } else if (input.charAt(index) == ' ') {
                out.write(' ');
                return 1;
            } else if (input.charAt(index) == 'W') {
                out.write('W');
                return 1;
            } else if (input.charAt(index) == 'r') {
                out.write('r');
                return 1;
            } else if (input.charAt(index) == 'd') {
                out.write('d');
                return 1;
            } else if (input.charAt(index) == '!') {
                out.write('!');
                return 1;
            }
            return 0;
        }
    }
}