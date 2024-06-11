import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        // Typical use case
        CharSequenceTranslator translator = new MyTranslator();
        String input = "Hello World!";
        String expected = "He110 W0r1d!";
        assertEquals(expected, translator.translate(input));
        
        // Edge case: empty input
        String emptyInput = "";
        assertEquals("", translator.translate(emptyInput));
        
        // Error scenario: IOException when writing to writer
        String errorInput = "Test";
        CharSequenceTranslator errorTranslator = new ErrorTranslator();
        assertThrows(IOException.class, () -> {
            errorTranslator.translate(errorInput);
        });
    }
    
    @Test
    public void testTranslateWithIndexAndWriter() {
        // Typical use case
        CharSequenceTranslator translator = new MyTranslator();
        StringWriter writer = new StringWriter();
        String input = "Hello World!";
        int index = 0;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(input.length(), consumed);
            String expected = "He110 W0r1d!";
            assertEquals(expected, writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this case");
        }
        
        // Edge case: empty input
        StringWriter emptyWriter = new StringWriter();
        String emptyInput = "";
        try {
            int consumed = translator.translate(emptyInput, 0, emptyWriter);
            assertEquals(0, consumed);
            assertEquals("", emptyWriter.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this case");
        }
        
        // Error scenario: IOException when writing to writer
        StringWriter errorWriter = new StringWriter();
        String errorInput = "Test";
        int errorIndex = 0;
        CharSequenceTranslator errorTranslator = new ErrorTranslator();
        assertThrows(IOException.class, () -> {
            errorTranslator.translate(errorInput, errorIndex, errorWriter);
        });
    }
    
    @Test
    public void testWith() {
        // Typical use case
        CharSequenceTranslator mainTranslator = new MyTranslator();
        CharSequenceTranslator extraTranslator = new ExtraTranslator();
        CharSequenceTranslator mergedTranslator = mainTranslator.with(extraTranslator);
        String input = "Alice's Adventures in Wonderland";
        String expected = "A11ce's Adv3ntur35 in Wond3rland";
        assertEquals(expected, mergedTranslator.translate(input));
        
        // Edge case: no extra translators
        CharSequenceTranslator singleTranslator = new MyTranslator();
        CharSequenceTranslator result = singleTranslator.with();
        assertEquals(input, result.translate(input));
    }
    
    @Test
    public void testHex() {
        // Typical use case
        int codepoint = 97;
        String expectedHex = "61";
        assertEquals(expectedHex, CharSequenceTranslator.hex(codepoint));
        
        // Edge case: special character
        int specialCodepoint = 8364; // Euro sign
        String expectedSpecialHex = "20AC";
        assertEquals(expectedSpecialHex, CharSequenceTranslator.hex(specialCodepoint));
    }
}