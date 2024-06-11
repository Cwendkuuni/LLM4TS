
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class LexerTest {
  
    @Test
    public void testGetLineNumber() {
        // Create a Lexer object and check that getLineNumber() returns 0
        Lexer lexer = new Lexer(null, null);
        assertEquals(0, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape_validEscapeCharacter() throws IOException {
        // Create a Lexer object and an ExtendedBufferedReader object
        Lexer lexer = new Lexer(null, new ExtendedBufferedReader(null));
        
        // Set the next character in the input stream to 'n'
        lexer.in.setNextChar('n');
        
        // Call readEscape() and check that it returns the expected value
        int result = lexer.readEscape();
        assertEquals('\n', result);
    }
    
    @Test(expected = IOException.class)
    public void testReadEscape_invalidEscapeCharacter() throws IOException {
        // Create a Lexer object and an ExtendedBufferedReader object
        Lexer lexer = new Lexer(null, new ExtendedBufferedReader(null));
        
        // Set the next character in the input stream to 'x'
        lexer.in.setNextChar('x');
        
        // Call readEscape() and expect an IOException to be thrown
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces_noTrailingSpaces() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Create a StringBuilder object with no trailing spaces
        StringBuilder buffer = new StringBuilder("hello");
        
        // Call trimTrailingSpaces() and check that the buffer remains unchanged
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello", buffer.toString());
    }
    
    @Test
    public void testTrimTrailingSpaces_withTrailingSpaces() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Create a StringBuilder object with trailing spaces
        StringBuilder buffer = new StringBuilder("hello     ");
        
        // Call trimTrailingSpaces() and check that the trailing spaces are removed
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine_singleLineTerminator() throws IOException {
        // Create a Lexer object and an ExtendedBufferedReader object
        Lexer lexer = new Lexer(null, new ExtendedBufferedReader(null));
        
        // Set the next character in the input stream to '\n'
        lexer.in.setNextChar('\n');
        
        // Call readEndOfLine() and check that it returns true
        boolean result = lexer.readEndOfLine('\n');
        assertTrue(result);
    }
    
    @Test
    public void testReadEndOfLine_carriageReturnLineFeed() throws IOException {
        // Create a Lexer object and an ExtendedBufferedReader object
        Lexer lexer = new Lexer(null, new ExtendedBufferedReader(null));
        
        // Set the next characters in the input stream to '\r\n'
        lexer.in.setNextChar('\r');
        lexer.in.setNextChar('\n');
        
        // Call readEndOfLine() and check that it returns true
        boolean result = lexer.readEndOfLine('\r');
        assertTrue(result);
    }
    
    @Test
    public void testReadEndOfLine_noLineTerminator() throws IOException {
        // Create a Lexer object and an ExtendedBufferedReader object
        Lexer lexer = new Lexer(null, new ExtendedBufferedReader(null));
        
        // Set the next character in the input stream to 'a'
        lexer.in.setNextChar('a');
        
        // Call readEndOfLine() and check that it returns false
        boolean result = lexer.readEndOfLine('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsWhitespace_spaceCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isWhitespace() with a space character and check that it returns true
        boolean result = lexer.isWhitespace(' ');
        assertTrue(result);
    }
    
    @Test
    public void testIsWhitespace_nonSpaceCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isWhitespace() with a non-space character and check that it returns false
        boolean result = lexer.isWhitespace('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsStartOfLine_startOfLineCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isStartOfLine() with a start of line character and check that it returns true
        boolean result = lexer.isStartOfLine('\n');
        assertTrue(result);
    }
    
    @Test
    public void testIsStartOfLine_nonStartOfLineCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isStartOfLine() with a non-start of line character and check that it returns false
        boolean result = lexer.isStartOfLine('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsEndOfFile_endOfFileCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isEndOfFile() with an end of file character and check that it returns true
        boolean result = lexer.isEndOfFile(-1);
        assertTrue(result);
    }
    
    @Test
    public void testIsEndOfFile_nonEndOfFileCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isEndOfFile() with a non-end of file character and check that it returns false
        boolean result = lexer.isEndOfFile(0);
        assertFalse(result);
    }
    
    @Test
    public void testIsDelimiter_delimiterCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isDelimiter() with a delimiter character and check that it returns true
        boolean result = lexer.isDelimiter(',');
        assertTrue(result);
    }
    
    @Test
    public void testIsDelimiter_nonDelimiterCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isDelimiter() with a non-delimiter character and check that it returns false
        boolean result = lexer.isDelimiter('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsEscape_escapeCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isEscape() with an escape character and check that it returns true
        boolean result = lexer.isEscape('\\');
        assertTrue(result);
    }
    
    @Test
    public void testIsEscape_nonEscapeCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isEscape() with a non-escape character and check that it returns false
        boolean result = lexer.isEscape('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsQuoteChar_quoteCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isQuoteChar() with a quote character and check that it returns true
        boolean result = lexer.isQuoteChar('"');
        assertTrue(result);
    }
    
    @Test
    public void testIsQuoteChar_nonQuoteCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isQuoteChar() with a non-quote character and check that it returns false
        boolean result = lexer.isQuoteChar('a');
        assertFalse(result);
    }
    
    @Test
    public void testIsCommentStart_commentStartCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isCommentStart() with a comment start character and check that it returns true
        boolean result = lexer.isCommentStart('#');
        assertTrue(result);
    }
    
    @Test
    public void testIsCommentStart_nonCommentStartCharacter() {
        // Create a Lexer object
        Lexer lexer = new Lexer(null, null);
        
        // Call isCommentStart() with a non-comment start character and check that it returns false
        boolean result = lexer.isCommentStart('a');
        assertFalse(result);
    }
  
}
