import org.junit.Test;
import static org.junit.Assert.*;

public class CharSequenceTranslatorTest {
    
    // Test translate(CharSequence, int, Writer) method
    
    @Test
    public void testTranslateWithValidInput() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        StringWriter writer = new StringWriter();
        
        // Act
        int consumed = translator.translate(input, 0, writer);
        
        // Assert
        assertEquals(3, consumed);
        assertEquals("abc", writer.toString());
    }
    
    @Test
    public void testTranslateWithNullInput() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = null;
        StringWriter writer = new StringWriter();
        
        // Act
        int consumed = translator.translate(input, 0, writer);
        
        // Assert
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }
    
    // Test translate(CharSequence) method
    
    @Test
    public void testTranslateWithValidInput() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        
        // Act
        String output = translator.translate(input);
        
        // Assert
        assertEquals("abc", output);
    }
    
    @Test
    public void testTranslateWithNullInput() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = null;
        
        // Act
        String output = translator.translate(input);
        
        // Assert
        assertNull(output);
    }
    
    // Other test cases for edge cases and exception handling scenarios
    
    @Test
    public void testTranslateWithEmptyInput() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "";
        StringWriter writer = new StringWriter();
        
        // Act
        int consumed = translator.translate(input, 0, writer);
        
        // Assert
        assertEquals(0, consumed);
        assertEquals("", writer.toString());
    }
    
    @Test(expected = IOException.class)
    public void testTranslateWithWriterException() throws IOException {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "abc";
        Writer writer = new TestWriter();
        
        // Act
        translator.translate(input, 0, writer);
    }
    
    @Test
    public void testTranslateWithSurrogatePair() {
        // Arrange
        CharSequenceTranslator translator = new ConcreteTranslator();
        String input = "😀";
        StringWriter writer = new StringWriter();
        
        // Act
        int consumed = translator.translate(input, 0, writer);
        
        // Assert
        assertEquals(2, consumed);
        assertEquals("😀", writer.toString());
    }
    
    // Concrete translator for testing
    
    private static class ConcreteTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.subSequence(index, input.length()).toString());
            return input.length() - index;
        }
    }
    
    // Test writer for simulating IOException
    
    private static class TestWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }
        @Override
        public void flush() throws IOException {}
        @Override
        public void close() throws IOException {}
    }
}