import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    // Test the translate method for the Happy Path
    @Test
    public void testTranslate_happyPath() throws Exception {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Hello World";
        String expectedOutput = "H3ll0 W0rld";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Test the translate method with a null input
    @Test
    public void testTranslate_nullInput() throws Exception {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        String expectedOutput = null;
        String actualOutput = translator.translate(input);
        assertNull(actualOutput);
    }

    // Test the translate method with an empty input
    @Test
    public void testTranslate_emptyInput() throws Exception {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Test the translate method with an input containing special characters
    @Test
    public void testTranslate_inputWithSpecialCharacters() throws Exception {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Special characters: !@#$%^&*()";
        String expectedOutput = "Sp3c!al charact3rs: !@#$%^&*()";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Test the translate method with an input that throws an IOException
    @Test(expected = IOException.class)
    public void testTranslate_inputWithIOException() throws Exception {
        CharSequenceTranslator translator = new TestTranslatorWithIOException();
        String input = "Hello World";
        translator.translate(input);
    }

    // Test the translate method for a custom translator
    @Test
    public void testTranslate_customTranslator() throws Exception {
        CharSequenceTranslator translator = new CustomTranslator();
        String input = "Hello World";
        String expectedOutput = "H4ll0 W0rld";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'o') {
                out.write("0");
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write("3");
                return 1;
            } else {
                out.write(input.charAt(index));
                return 0;
            }
        }
    }

    private static class TestTranslatorWithIOException extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException("IOException");
        }
    }

    private static class CustomTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'o') {
                out.write("4");
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write("3");
                return 1;
            } else {
                out.write(input.charAt(index));
                return 0;
            }
        }
    }
}