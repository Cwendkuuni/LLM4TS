import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_method1_typicalCase() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testTranslate_method1_nullInput() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = null;
        assertNull(translator.translate(input));
    }
    
    @Test
    public void testTranslate_method1_emptyInput() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "";
        assertEquals("", translator.translate(input));
    }
    
    @Test
    public void testTranslate_method1_specialCharacters() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "!@#$%^&*()";
        String expectedOutput = "!@#$%^&*()";
        assertEquals(expectedOutput, translator.translate(input));
    }
    
    @Test
    public void testTranslate_method2_typicalCase() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello, World!";
        assertEquals(13, translator.translate(input, 0, new StringWriter()));
    }
    
    @Test
    public void testTranslate_method2_nullInput() throws IOException {
        CharSequenceTranslator translator = new MyTranslator();
        String input = null;
        StringWriter writer = new StringWriter();
        translator.translate(input, 0, writer);
        String actualOutput = writer.toString();
        assertEquals("", actualOutput);
    }
    
    @Test
    public void testTranslate_method2_emptyInput() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "";
        assertEquals(0, translator.translate(input, 0, new StringWriter()));
    }
    
    @Test
    public void testTranslate_method2_specialCharacters() throws IOException {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "!@#$%^&*()";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        String actualOutput = writer.toString();
        assertEquals(0, consumed);
        assertEquals("!@#$%^&*()", actualOutput);
    }
    
    @Test
    public void testTranslate_method2_surrogatePair() throws IOException {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "\uD83D\uDE00";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        String actualOutput = writer.toString();
        assertEquals(2, consumed);
        assertEquals("\\uD83D\\uDE00", actualOutput);
    }
    
    @Test
    public void testTranslate_method2_invalidInput() {
        CharSequenceTranslator translator = new MyTranslator();
        String input = "\\uD83D\\uDE00";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, 0, writer);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }
    
    @Test
    public void testWith_typicalCase() {
        CharSequenceTranslator translator1 = new MyTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        CharSequenceTranslator translator3 = new YetAnotherTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);
        assertNotNull(mergedTranslator);
    }
    
    @Test
    public void testWith_noTranslators() {
        CharSequenceTranslator translator = new MyTranslator();
        CharSequenceTranslator mergedTranslator = translator.with();
        assertEquals(translator, mergedTranslator);
    }
    
    @Test
    public void testHex_typicalCase() {
        int codepoint = 65;
        String expectedOutput = "41";
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHex_zeroCodepoint() {
        int codepoint = 0;
        String expectedOutput = "0";
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }
    
    @Test
    public void testHex_negativeCodepoint() {
        int codepoint = -65;
        String expectedOutput = "FFFFFFFFFFFFFFC1";
        assertEquals(expectedOutput, CharSequenceTranslator.hex(codepoint));
    }
    
    private class MyTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException();
            }
            if (index < 0 || index >= input.length()) {
                return 0;
            }
            
            out.write(input.charAt(index));
            return 1;
        }
    }
    
    private class AnotherTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException();
            }
            if (index < 0 || index >= input.length()) {
                return 0;
            }
            
            out.write(Character.toUpperCase(input.charAt(index)));
            return 1;
        }
    }
    
    private class YetAnotherTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || out == null) {
                throw new IllegalArgumentException();
            }
            if (index < 0 || index >= input.length()) {
                return 0;
            }
            
            char ch = input.charAt(index);
            int codepoint = Character.codePointAt(input, index);
            out.write(Character.toChars(codepoint));
            return Character.charCount(codepoint);
        }
    }
}