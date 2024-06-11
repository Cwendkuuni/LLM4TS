import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {
    
    @Test
    public void testConstructor() {
        // Test constructor with valid input
        Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);
        assertEquals(format, lexer.format);
        assertEquals(in, lexer.in);
        assertEquals(delimiter, lexer.delimiter);
        assertEquals(escape, lexer.escape);
        assertEquals(quoteChar, lexer.quoteChar);
        assertEquals(commmentStart, lexer.commmentStart);
        assertEquals(ignoreSurroundingSpaces, lexer.ignoreSurroundingSpaces);
        assertEquals(ignoreEmptyLines, lexer.ignoreEmptyLines);

        // Test constructor with null escape character
        Lexer lexerNullEscape = new Lexer(format, in);
        assertNotNull(lexerNullEscape);
        assertEquals(format, lexerNullEscape.format);
        assertEquals(in, lexerNullEscape.in);
        assertEquals(delimiter, lexerNullEscape.delimiter);
        assertEquals(DISABLED, lexerNullEscape.escape);
        assertEquals(quoteChar, lexerNullEscape.quoteChar);
        assertEquals(commmentStart, lexerNullEscape.commmentStart);
        assertEquals(ignoreSurroundingSpaces, lexerNullEscape.ignoreSurroundingSpaces);
        assertEquals(ignoreEmptyLines, lexerNullEscape.ignoreEmptyLines);
    }
    
    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(format, in);
        
        // Test mapping null to DISABLED
        char c1 = lexer.mapNullToDisabled(null);
        assertEquals(DISABLED, c1);
        
        // Test mapping non-null to non-null
        char c2 = lexer.mapNullToDisabled('a');
        assertEquals('a', c2);
    }
    
    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        
        // Test getting line number
        long lineNumber = lexer.getLineNumber();
        assertEquals(in.getLineNumber(), lineNumber);
    }
    
    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new Lexer(format, in);
        
        // Test reading valid escape sequence
        in.setLastChar('n');
        int escapeChar = lexer.readEscape();
        assertEquals(LF, escapeChar);
        
        // Test reading invalid escape sequence
        in.setLastChar('g');
        try {
            lexer.readEscape();
            fail("IOException was not thrown");
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);
        
        // Test trimming trailing spaces
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
        
        // Test not trimming if no trailing spaces
        StringBuilder buffer2 = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer2);
        assertEquals("test", buffer2.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(format, in);
        
        // Test reading LF end of line
        int c1 = LF;
        boolean isEndOfLine1 = lexer.readEndOfLine(c1);
        assertTrue(isEndOfLine1);
        
        // Test reading CR end of line
        int c2 = CR;
        boolean isEndOfLine2 = lexer.readEndOfLine(c2);
        assertTrue(isEndOfLine2);
        
        // Test reading CR LF end of line
        in.setLookAhead(LF);
        int c3 = CR;
        boolean isEndOfLine3 = lexer.readEndOfLine(c3);
        assertTrue(isEndOfLine3);
        
        // Test reading not end of line
        int c4 = TAB;
        boolean isEndOfLine4 = lexer.readEndOfLine(c4);
        assertFalse(isEndOfLine4);
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        
        // Test whitespace character
        int c1 = ' ';
        boolean isWhitespace1 = lexer.isWhitespace(c1);
        assertTrue(isWhitespace1);
        
        // Test non-whitespace character
        int c2 = 'a';
        boolean isWhitespace2 = lexer.isWhitespace(c2);
        assertFalse(isWhitespace2);
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        
        // Test start of line character
        int c1 = CR;
        boolean isStartOfLine1 = lexer.isStartOfLine(c1);
        assertTrue(isStartOfLine1);
        
        // Test end of line character
        int c2 = LF;
        boolean isStartOfLine2 = lexer.isStartOfLine(c2);
        assertTrue(isStartOfLine2);
        
        // Test undefined character
        int c3 = UNDEFINED;
        boolean isStartOfLine3 = lexer.isStartOfLine(c3);
        assertTrue(isStartOfLine3);
        
        // Test non-start of line character
        int c4 = 'a';
        boolean isStartOfLine4 = lexer.isStartOfLine(c4);
        assertFalse(isStartOfLine4);
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        
        // Test end of file character
        int c1 = END_OF_STREAM;
        boolean isEndOfFile1 = lexer.isEndOfFile(c1);
        assertTrue(isEndOfFile1);
        
        // Test non-end of file character
        int c2 = 'a';
        boolean isEndOfFile2 = lexer.isEndOfFile(c2);
        assertFalse(isEndOfFile2);
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        
        // Test delimiter character
        int c1 = delimiter;
        boolean isDelimiter1 = lexer.isDelimiter(c1);
        assertTrue(isDelimiter1);
        
        // Test non-delimiter character
        int c2 = 'a';
        boolean isDelimiter2 = lexer.isDelimiter(c2);
        assertFalse(isDelimiter2);
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        
        // Test escape character
        int c1 = escape;
        boolean isEscape1 = lexer.isEscape(c1);
        assertTrue(isEscape1);
        
        // Test non-escape character
        int c2 = 'a';
        boolean isEscape2 = lexer.isEscape(c2);
        assertFalse(isEscape2);
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        
        // Test quote character
        int c1 = quoteChar;
        boolean isQuoteChar1 = lexer.isQuoteChar(c1);
        assertTrue(isQuoteChar1);
        
        // Test non-quote character
        int c2 = 'a';
        boolean isQuoteChar2 = lexer.isQuoteChar(c2);
        assertFalse(isQuoteChar2);
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        
        // Test comment start character
        int c1 = commmentStart;
        boolean isCommentStart1 = lexer.isCommentStart(c1);
        assertTrue(isCommentStart1);
        
        // Test non-comment start character
        int c2 = 'a';
        boolean isCommentStart2 = lexer.isCommentStart(c2);
        assertFalse(isCommentStart2);
    }
    
    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new Lexer(format, in);
        
        // Test next token
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertNotNull(nextToken);
    }
}