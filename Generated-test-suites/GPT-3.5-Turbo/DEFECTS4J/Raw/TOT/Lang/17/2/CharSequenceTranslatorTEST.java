import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CharSequenceTranslatorTest {
    
    private CharSequenceTranslator translator;
    
    @Before
    public void setup() {
        translator = new ConcreteTranslator();
    }
    
    @Test
    public void testTranslate_WithNonNullInput_ShouldTranslateText() {
        String input = "Hello World";
        String expectedOutput = "Hola Mundo";
        
        String output = translator.translate(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testTranslate_WithNullInput_ShouldReturnNull() {
        String input = null;
        
        String output = translator.translate(input);
        
        assertNull(output);
    }
    
    @Test
    public void testTranslate_WithEmptyInput_ShouldReturnEmptyString() {
        String input = "";
        
        String output = translator.translate(input);
        
        assertEquals("", output);
    }
    
    @Test
    public void testTranslate_WithInvalidInput_ShouldThrowRuntimeException() {
        String input = "Hello World";
        
        try {
            translator.translate(input);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            assertEquals("IOException while writing to StringWriter", e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_WithInputContainingSurrogatePairs_ShouldTranslateText() {
        String input = "Hello \uD83D\uDE03";
        String expectedOutput = "Hola \uD83D\uDE03";
        
        String output = translator.translate(input);
        
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testTranslate_WithLargeInput_ShouldTranslateText() {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            input.append("Hello World");
        }
        
        String output = translator.translate(input.toString());
        
        assertEquals("Hola Mundo".repeat(1000), output);
    }
    
    private static class ConcreteTranslator extends CharSequenceTranslator {
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write("H");
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write("o");
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write("l");
                return 1;
            } else if (input.charAt(index) == 'o') {
                out.write("a");
                return 1;
            } else if (input.charAt(index) == 'W') {
                out.write("H");
                return 1;
            } else if (input.charAt(index) == 'r') {
                out.write("u");
                return 1;
            } else if (input.charAt(index) == 'd') {
                out.write("m");
                return 1;
            }
            return 0;
        }
    }
}