import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {
    
    private Lexer lexer;
    private ExtendedBufferedReader in;
    
    @Before
    public void setUp() {
        CSVFormat format = new CSVFormat.Builder().withDelimiter(',').withEscape('\\').withQuoteChar('\'').withCommentStart('#').build();
        in = new ExtendedBufferedReader();
        lexer = new Lexer(format, in);
    }
    
    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }
    
    @Test
    public void testMapNullToDisabled() {
        assertEquals('a', lexer.mapNullToDisabled('a'));
        assertEquals('\ufffe', lexer.mapNullToDisabled(null));
    }
    
    @Test
    public void testReadEscape() {
        try {
            int escape = lexer.readEscape();
            assertEquals(LF, escape);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("Hello   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() {
        try {
            boolean endOfLine = lexer.readEndOfLine(LF);
            assertTrue(endOfLine);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertTrue(isWhitespace);
        
        isWhitespace = lexer.isWhitespace('\t');
        assertTrue(isWhitespace);
        
        isWhitespace = lexer.isWhitespace(',');
        assertFalse(isWhitespace);
    }
    
    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(LF);
        assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine(CR);
        assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine(UNDEFINED);
        assertTrue(isStartOfLine);
        
        isStartOfLine = lexer.isStartOfLine('a');
        assertFalse(isStartOfLine);
    }
    
    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
        assertTrue(isEndOfFile);
        
        isEndOfFile = lexer.isEndOfFile('c');
        assertFalse(isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(',');
        assertTrue(isDelimiter);
        
        isDelimiter = lexer.isDelimiter('.');
        assertFalse(isDelimiter);
    }
    
    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape('\\');
        assertTrue(isEscape);
        
        isEscape = lexer.isEscape('/');
        assertFalse(isEscape);
    }
    
    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar('\'');
        assertTrue(isQuoteChar);
        
        isQuoteChar = lexer.isQuoteChar('"');
        assertFalse(isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart('#');
        assertTrue(isCommentStart);
        
        isCommentStart = lexer.isCommentStart('%');
        assertFalse(isCommentStart);
    }
    
}