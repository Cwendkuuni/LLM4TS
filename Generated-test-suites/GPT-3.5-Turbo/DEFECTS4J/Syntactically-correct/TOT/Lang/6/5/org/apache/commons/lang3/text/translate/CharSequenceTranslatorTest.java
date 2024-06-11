import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withValidInput_shouldTranslateSuccessfully() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "Hello, World!";
        String expectedOutput = "Hola, Mundo!";
        String translatedOutput = translator.translate(input);
        
        assertEquals(expectedOutput, translatedOutput);
    }
    
    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = null;
        String expectedOutput = null;
        String translatedOutput = translator.translate(input);
        
        assertEquals(expectedOutput, translatedOutput);
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "";
        String expectedOutput = "";
        String translatedOutput = translator.translate(input);
        
        assertEquals(expectedOutput, translatedOutput);
    }
    
    @Test
    public void testTranslate_withSpecialCharacters_shouldTranslateSuccessfully() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "!@#$%^&*()_+=-";
        String expectedOutput = "1234567890";
        String translatedOutput = translator.translate(input);
        
        assertEquals(expectedOutput, translatedOutput);
    }
    
    @Test
    public void testTranslate_withUnicodeInput_shouldTranslateSuccessfully() {
        CharSequenceTranslator translator = new TestTranslator();
        String input = "こんにちは";
        String expectedOutput = "Hello";
        String translatedOutput = translator.translate(input);
        
        assertEquals(expectedOutput, translatedOutput);
    }

    private static class TestTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write("H");
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write("e");
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write("l");
                return 1;
            } else if (input.charAt(index) == 'o') {
                out.write("o");
                return 1;
            } else if (input.charAt(index) == ',') {
                out.write(",");
                return 1;
            } else if (input.charAt(index) == 'W') {
                out.write("W");
                return 1;
            } else if (input.charAt(index) == 'r') {
                out.write("r");
                return 1;
            } else if (input.charAt(index) == 'd') {
                out.write("d");
                return 1;
            } else if (input.charAt(index) == '!') {
                out.write("!");
                return 1;
            } else if (input.charAt(index) == ' ') {
                out.write(" ");
                return 1;
            } else {
                return 0;
            }
        }
    }

}