import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {
    
    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(null, null);
        assertEquals(0, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() {
        Lexer lexer = new Lexer(null, null);
        
        // Test escape characters
        try {
            assertEquals(13, lexer.readEscape());
            assertEquals(10, lexer.readEscape());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test invalid escape character at end of stream
        try {
            lexer.readEscape();
            fail("Expected IOException to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof java.io.IOException);
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(null, null);
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new Lexer(null, null);
        
        // Test \n
        try {
            assertTrue(lexer.readEndOfLine(10));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test \r\n
        try {
            assertTrue(lexer.readEndOfLine(13));
            assertFalse(lexer.readEndOfLine(10));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test \r
        try {
            assertTrue(lexer.readEndOfLine(13));
            assertFalse(lexer.readEndOfLine(10));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isWhitespace(44));
        assertFalse(lexer.isWhitespace(65));
        assertTrue(lexer.isWhitespace(9));
        assertTrue(lexer.isWhitespace(32));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isStartOfLine(10));
        assertTrue(lexer.isStartOfLine(13));
        assertTrue(lexer.isStartOfLine(-1));
        assertFalse(lexer.isStartOfLine(32));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isEndOfFile(10));
        assertFalse(lexer.isEndOfFile(13));
        assertTrue(lexer.isEndOfFile(-1));
        assertFalse(lexer.isEndOfFile(32));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isDelimiter(10));
        assertFalse(lexer.isDelimiter(13));
        assertFalse(lexer.isDelimiter(44));
        assertTrue(lexer.isDelimiter(59));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isEscape(10));
        assertFalse(lexer.isEscape(13));
        assertFalse(lexer.isEscape(44));
        assertTrue(lexer.isEscape(92));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isQuoteChar(10));
        assertFalse(lexer.isQuoteChar(13));
        assertFalse(lexer.isQuoteChar(44));
        assertTrue(lexer.isQuoteChar(34));
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isCommentStart(10));
        assertFalse(lexer.isCommentStart(13));
        assertFalse(lexer.isCommentStart(44));
        assertTrue(lexer.isCommentStart(35));
    }
}