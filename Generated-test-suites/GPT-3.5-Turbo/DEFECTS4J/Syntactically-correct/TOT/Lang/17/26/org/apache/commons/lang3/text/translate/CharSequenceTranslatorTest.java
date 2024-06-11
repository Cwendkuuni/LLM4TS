import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate_withNullInput_shouldReturnNull() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String translated = translator.translate(null);
        assertNull(translated);
    }
    
    @Test
    public void testTranslate_withEmptyInput_shouldReturnEmptyString() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String translated = translator.translate("");
        assertEquals("", translated);
    }
    
    @Test
    public void testTranslate_withValidInput_shouldReturnTranslatedString() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "Hello, World!";
        String translated = translator.translate(input);
        assertEquals("Translated Hello, World!", translated);
    }
    
    @Test
    public void testTranslate_withIOException_shouldThrowRuntimeException() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "Hello, World!";
        try {
            translator.translate(input);
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Success
        }
    }
    
    @Test
    public void testWith_withTranslators_shouldCombineTranslators() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        CharSequenceTranslator anotherTranslator = new AnotherConcreteTranslator();
        CharSequenceTranslator combinedTranslator = translator.with(anotherTranslator);
        
        String input = "Hello, World!";
        String translated = combinedTranslator.translate(input);
        
        assertEquals("Translated Hello, World! Another Translator", translated);
    }
    
    @Test
    public void testHex_withValidCodepoint_shouldReturnCorrectHexString() {
        int codepoint = 65;
        String hexString = CharSequenceTranslator.hex(codepoint);
        
        assertEquals("41", hexString);
    }
    
    @Test
    public void testHex_withInvalidCodepoint_shouldReturnEmptyString() {
        int codepoint = -1;
        String hexString = CharSequenceTranslator.hex(codepoint);
        
        assertEquals("", hexString);
    }
    
}