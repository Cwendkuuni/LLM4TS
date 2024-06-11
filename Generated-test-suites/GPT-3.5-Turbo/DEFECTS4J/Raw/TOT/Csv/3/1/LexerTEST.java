import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        long lineNum = lexer.getLineNumber();
        
        assertEquals(1, lineNum);
    }
    
    @Test
    public void testReadEscape_withValidEscapeSequence() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        in.addChars(new char[] {'\\'});
        in.setLastChar('\\');
        in.addChars(new char[] {'r'});
        
        int unescapedChar = lexer.readEscape();
        
        assertEquals(Lexer.CR, unescapedChar);
    }
    
    @Test(expected = IOException.class)
    public void testReadEscape_withInvalidEscapeSequence() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        in.addChars(new char[] {'\\'});
        in.setLastChar('\\');
        in.addChars(new char[] {'%'});  // Invalid escape sequence
        
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces_withTrailingSpaces() {
        Lexer lexer = new Lexer(format, null);
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("abc   ");
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("abc", buffer.toString());
    }
    
    @Test
    public void testTrimTrailingSpaces_withoutTrailingSpaces() {
        Lexer lexer = new Lexer(format, null);
        
        StringBuilder buffer = new StringBuilder();
        buffer.append("abc");
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("abc", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine_withLF() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        boolean isEndOfLine = lexer.readEndOfLine('\n');
        
        assertTrue(isEndOfLine);
    }
    
    @Test
    public void testReadEndOfLine_withCR() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        boolean isEndOfLine = lexer.readEndOfLine('\r');
        
        assertTrue(isEndOfLine);
    }
    
    @Test
    public void testReadEndOfLine_withCRLF() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        in.setLastChar('\r');
        in.addChars(new char[] {'\n'});
        
        boolean isEndOfLine = lexer.readEndOfLine('\r');
        
        assertTrue(isEndOfLine);
    }
    
    @Test
    public void testReadEndOfLine_withOtherChar() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        
        boolean isEndOfLine = lexer.readEndOfLine('a');
        
        assertFalse(isEndOfLine);
    }
    
    @Test
    public void testIsWhitespace_withWhitespaceChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isWhitespace = lexer.isWhitespace(' ');
        
        assertTrue(isWhitespace);
    }
    
    @Test
    public void testIsWhitespace_withNonWhitespaceChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isWhitespace = lexer.isWhitespace('a');
        
        assertFalse(isWhitespace);
    }
    
    @Test
    public void testIsStartOfLine_withStartOfLineChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        
        assertTrue(isStartOfLine);
    }
    
    @Test
    public void testIsStartOfLine_withNonStartOfLineChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isStartOfLine = lexer.isStartOfLine('a');
        
        assertFalse(isStartOfLine);
    }
    
    @Test
    public void testisEndOfFile_withEndOfFileChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        
        assertTrue(isEndOfFile);
    }
    
    @Test
    public void testisEndOfFile_withNonEndOfFileChar() {
        Lexer lexer = new Lexer(format, null);
        
        boolean isEndOfFile = lexer.isEndOfFile(1);
        
        assertFalse(isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter_withDelimiterChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.delimiter = ',';
        
        boolean isDelimiter = lexer.isDelimiter(',');
        
        assertTrue(isDelimiter);
    }
    
    @Test
    public void testIsDelimiter_withNonDelimiterChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.delimiter = ',';
        
        boolean isDelimiter = lexer.isDelimiter(';');
        
        assertFalse(isDelimiter);
    }
    
    @Test
    public void testIsEscape_withEscapeChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.escape = '\\';
        
        boolean isEscape = lexer.isEscape('\\');
        
        assertTrue(isEscape);
    }
    
    @Test
    public void testIsEscape_withNonEscapeChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.escape = '\\';
        
        boolean isEscape = lexer.isEscape('/');
        
        assertFalse(isEscape);
    }
    
    @Test
    public void testIsQuoteChar_withQuoteChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.quoteChar = '\"';
        
        boolean isQuoteChar = lexer.isQuoteChar('\"');
        
        assertTrue(isQuoteChar);
    }
    
    @Test
    public void testIsQuoteChar_withNonQuoteChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.quoteChar = '\"';
        
        boolean isQuoteChar = lexer.isQuoteChar('\'');
        
        assertFalse(isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart_withCommentStartChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.commmentStart = '#';
        
        boolean isCommentStart = lexer.isCommentStart('#');
        
        assertTrue(isCommentStart);
    }
    
    @Test
    public void testIsCommentStart_withNonCommentStartChar() {
        Lexer lexer = new Lexer(format, null);
        lexer.commmentStart = '#';
        
        boolean isCommentStart = lexer.isCommentStart('/');
        
        assertFalse(isCommentStart);
    }
}