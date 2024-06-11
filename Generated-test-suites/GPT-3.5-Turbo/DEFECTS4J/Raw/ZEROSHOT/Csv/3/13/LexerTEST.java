import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {
    
    @Test
    public void testGetLineNumber() {
        // Test case for getLineNumber() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the getLineNumber() method and check the result
        assertEquals(0, lexer.getLineNumber());
    }
    
    @Test
    public void testMapNullToDisabled() {
        // Test case for mapNullToDisabled() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the mapNullToDisabled() method with null as input and check the result
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        
        // Call the mapNullToDisabled() method with 'a' as input and check the result
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }
    
    @Test
    public void testReadEscape() {
        // Test case for readEscape() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the readEscape() method and check the result
        try {
            assertEquals(10, lexer.readEscape());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        // Test case for trimTrailingSpaces() method
        
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Create a StringBuilder
        StringBuilder buffer = new StringBuilder("Hello World     ");
        
        // Call the trimTrailingSpaces() method
        lexer.trimTrailingSpaces(buffer);
        
        // Check the result
        assertEquals("Hello World", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        // Test case for readEndOfLine() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the readEndOfLine() method and check the result
        try {
            assertFalse(lexer.readEndOfLine(10));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testIsWhitespace() {
        // Test case for isWhitespace() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isWhitespace() method with 'a' as input and check the result
        assertFalse(lexer.isWhitespace('a'));
        
        // Call the isWhitespace() method with ' ' as input and check the result
        assertTrue(lexer.isWhitespace(' '));
    }
    
    @Test
    public void testIsStartOfLine() {
        // Test case for isStartOfLine() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isStartOfLine() method with 'a' as input and check the result
        assertFalse(lexer.isStartOfLine('a'));
        
        // Call the isStartOfLine() method with '\n' as input and check the result
        assertTrue(lexer.isStartOfLine('\n'));
    }
    
    @Test
    public void testIsEndOfFile() {
        // Test case for isEndOfFile() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isEndOfFile() method with -1 as input and check the result
        assertTrue(lexer.isEndOfFile(-1));
        
        // Call the isEndOfFile() method with 10 as input and check the result
        assertFalse(lexer.isEndOfFile(10));
    }
    
    @Test
    public void testIsDelimiter() {
        // Test case for isDelimiter() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isDelimiter() method with ',' as input and check the result
        assertTrue(lexer.isDelimiter(','));
        
        // Call the isDelimiter() method with 'a' as input and check the result
        assertFalse(lexer.isDelimiter('a'));
    }
    
    @Test
    public void testIsEscape() {
        // Test case for isEscape() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isEscape() method with '\\' as input and check the result
        assertTrue(lexer.isEscape('\\'));
        
        // Call the isEscape() method with 'a' as input and check the result
        assertFalse(lexer.isEscape('a'));
    }
    
    @Test
    public void testIsQuoteChar() {
        // Test case for isQuoteChar() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isQuoteChar() method with '\"' as input and check the result
        assertTrue(lexer.isQuoteChar('\"'));
        
        // Call the isQuoteChar() method with 'a' as input and check the result
        assertFalse(lexer.isQuoteChar('a'));
    }
    
    @Test
    public void testIsCommentStart() {
        // Test case for isCommentStart() method
        // Create a Lexer object
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        
        // Call the isCommentStart() method with '#' as input and check the result
        assertTrue(lexer.isCommentStart('#'));
        
        // Call the isCommentStart() method with 'a' as input and check the result
        assertFalse(lexer.isCommentStart('a'));
    }
}