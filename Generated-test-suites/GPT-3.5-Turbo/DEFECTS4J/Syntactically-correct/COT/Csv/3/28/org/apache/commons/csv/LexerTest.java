import static org.junit.Assert.*;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(null, null);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        Lexer lexer = new Lexer(null, null);
        try {
            assertEquals(10, lexer.readEscape());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(null, null);
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new Lexer(null, null);
        try {
            assertFalse(lexer.readEndOfLine(10));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isWhitespace(10));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isStartOfLine(10));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isEndOfFile(10));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isDelimiter(10));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isEscape(10));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isQuoteChar(10));
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isCommentStart(10));
    }

}