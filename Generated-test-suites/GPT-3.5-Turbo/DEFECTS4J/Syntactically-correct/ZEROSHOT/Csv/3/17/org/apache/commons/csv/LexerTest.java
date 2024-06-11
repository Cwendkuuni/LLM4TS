import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {
    
    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;
    
    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        in = new ExtendedBufferedReader();
        lexer = new Lexer(format, in);
    }
    
    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() throws IOException {
        in.setChar('r');
        assertEquals(13, lexer.readEscape());
        
        in.setChar('n');
        assertEquals(10, lexer.readEscape());
        
        in.setChar('t');
        assertEquals(9, lexer.readEscape());
        
        in.setChar('b');
        assertEquals(8, lexer.readEscape());
        
        in.setChar('f');
        assertEquals(12, lexer.readEscape());
        
        in.setChar('\r');
        assertEquals('\r', lexer.readEscape());
        
        in.setChar('\n');
        assertEquals('\n', lexer.readEscape());
        
        in.setChar('\t');
        assertEquals('\t', lexer.readEscape());
        
        in.setChar('\b');
        assertEquals('\b', lexer.readEscape());
    }
    
    @Test(expected=IOException.class)
    public void testReadEscapeWithEOF() throws IOException {
        in.setChar(ExtendedBufferedReader.END_OF_STREAM);
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
        
        buffer = new StringBuilder();
        buffer.append("test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
        
        buffer = new StringBuilder();
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        in.setChar('\n');
        assertTrue(lexer.readEndOfLine('\n'));
        
        in.setChar('\r');
        assertTrue(lexer.readEndOfLine('\r'));
        
        in.setChar('\r');
        in.setLookAhead('\n');
        assertTrue(lexer.readEndOfLine('\r'));
        
        in.setChar('a');
        assertFalse(lexer.readEndOfLine('a'));
    }
    
    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace(','));
    }
    
    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine('a'));
    }
    
    @Test
    public void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }
    
    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
    }
    
    @Test
    public void testIsEscape() {
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('/'));
    }
    
    @Test
    public void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
    }
    
    @Test
    public void testIsCommentStart() {
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('!'));
    }
}