import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class CharSequenceTranslatorTest {
    
    private CharSequenceTranslator translator;
    
    @Before
    public void setUp() {
        translator = new ConcreteCharSequenceTranslator();
    }
    
    @Test
    public void testTranslateIndexWriter() {
        // Test typical input and expected output
        StringBuilder input = new StringBuilder("Hello");
        StringWriter writer = new StringWriter();
        try {
            int consumed = translator.translate(input, 0, writer);
            assertEquals(5, consumed);
            assertEquals("Hello", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testTranslateCharSequence() {
        // Test typical input and expected output
        String input = "Hello";
        String output = translator.translate(input);
        assertEquals("Hello", output);
    }
    
    @Test
    public void testTranslateCharSequenceWriter() {
        // Test typical input and expected output
        String input = "Hello";
        StringWriter writer = new StringWriter();
        try {
            translator.translate(input, writer);
            assertEquals("Hello", writer.toString());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testWith() {
        // Test merging translators
        CharSequenceTranslator translator2 = new AnotherConcreteCharSequenceTranslator();
        CharSequenceTranslator merged = translator.with(translator2);
        assertNotNull(merged);
    }
    
    @Test
    public void testHex() {
        // Test converting a character to uppercase hexadecimal string
        String hex = CharSequenceTranslator.hex(65);
        assertEquals("41", hex);
    }
}