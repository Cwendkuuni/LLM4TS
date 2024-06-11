
import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateCharSequenceIntWriter() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        StringWriter writer = new StringWriter();
        String input = "Hello, World!";
        int index = 0;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(13, consumed);
            assertEquals("Hello, World!", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslateCharSequenceIntWriter_withNullInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        StringWriter writer = new StringWriter();
        CharSequence input = null;
        int index = 0;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(0, consumed);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Edge case: input string is empty
    @Test
    public void testTranslateCharSequenceIntWriter_withEmptyInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        StringWriter writer = new StringWriter();
        CharSequence input = "";
        int index = 0;
        try {
            int consumed = translator.translate(input, index, writer);
            assertEquals(0, consumed);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    // Error scenario: Writer is null
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateCharSequenceIntWriter_withNullWriter() throws Exception {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        CharSequence input = "Hello, World!";
        int index = 0;
        translator.translate(input, index, null);
    }
    
    @Test
    public void testTranslateCharSequence() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testTranslateCharSequence_withNullInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        CharSequence input = null;
        String expectedOutput = null;
        
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
    
    // Edge case: input string is empty
    @Test
    public void testTranslateCharSequence_withEmptyInput() {
        CharSequenceTranslator translator = new ConcreteTranslator();
        
        CharSequence input = "";
        String expectedOutput = "";
        
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new ConcreteTranslator();
        CharSequenceTranslator translator2 = new AnotherTranslator();
        
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        
        // Test translation using the merged translator
        StringWriter writer = new StringWriter();
        String input = "Hello, World!";
        int index = 0;
        try {
            int consumed = mergedTranslator.translate(input, index, writer);
            assertEquals(13, consumed);
            assertEquals("Merged Translation", writer.toString());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testHex() {
        int codepoint = 65;
        String expectedOutput = "41";
        
        String output = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, output);
    }
    
    // Edge case: codepoint is 0
    @Test
    public void testHex_withCodepointZero() {
        int codepoint = 0;
        String expectedOutput = "0";
        
        String output = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, output);
    }
    
    // Edge case: codepoint is Integer.MAX_VALUE
    @Test
    public void testHex_withCodepointMaxValue() {
        int codepoint = Integer.MAX_VALUE;
        String expectedOutput = "7FFFFFFF";
        
        String output = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, output);
    }
    
    // Edge case: codepoint is Integer.MIN_VALUE
    @Test
    public void testHex_withCodepointMinValue() {
        int codepoint = Integer.MIN_VALUE;
        String expectedOutput = "-80000000";
        
        String output = CharSequenceTranslator.hex(codepoint);
        assertEquals(expectedOutput, output);
    }
}
