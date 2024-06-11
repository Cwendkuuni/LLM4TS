import org.junit.Test;
import org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new ExampleTranslator();
        
        // Test null input
        String result1 = translator.translate(null);
        assertNull(result1);
        
        // Test empty input
        String result2 = translator.translate("");
        assertEquals("", result2);
        
        // Test non-empty input
        String result3 = translator.translate("Hello, World!");
        assertEquals("Hola, Mundo!", result3);
    }

    @Test
    public void testWith() throws Exception {
        CharSequenceTranslator translator1 = new ExampleTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        
        CharSequenceTranslator translator3 = translator1.with(translator2);
        
        // Test null input
        String result = translator3.translate(null);
        assertNull(result);
    }

    private class ExampleTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == 'H') {
                out.write('H');
                return 1;
            } else if (input.charAt(index) == 'e') {
                out.write('e');
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write('l');
                return 1;
            } else if (input.charAt(index) == 'o') {
                out.write('o');
                return 1;
            } else if (input.charAt(index) == ',') {
                out.write(',');
                return 1;
            } else if (input.charAt(index) == 'W') {
                out.write('W');
                return 1;
            } else if (input.charAt(index) == 'r') {
                out.write('r');
                return 1;
            } else if (input.charAt(index) == 'l') {
                out.write('l');
                return 1;
            } else if (input.charAt(index) == 'd') {
                out.write('d');
                return 1;
            } else {
                return 0;
            }
        }
        
    }
    
    private class AnotherTranslator extends CharSequenceTranslator {
        
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.charAt(index) == '!') {
                out.write('!');
                return 1;
            } else if (input.charAt(index) == ' ') {
                out.write(',');
                return 1;
            } else {
                return 0;
            }
        }
        
    }
    
}