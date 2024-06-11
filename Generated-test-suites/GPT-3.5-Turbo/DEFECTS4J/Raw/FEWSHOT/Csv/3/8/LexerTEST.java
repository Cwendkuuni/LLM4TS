import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    
    private CSVFormat formatMock;
    private ExtendedBufferedReader readerMock;
    private Lexer lexer;
    
    @Before
    public void setUp() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(formatMock, readerMock) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                // Implement the abstract method
                return null;
            }
        };
    }
    
    @Test
    public void testGetLineNumber() {
        when(readerMock.getLineNumber()).thenReturn(5L);
        
        long lineNumber = lexer.getLineNumber();
        
        assertEquals(5L, lineNumber);
    }
    
    @Test
    public void testReadEscape() throws IOException {
        when(readerMock.read()).thenReturn('r');
        
        int unescapedCharacter = lexer.readEscape();
        
        assertEquals(13, unescapedCharacter);
    }
    
    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        when(readerMock.read()).thenReturn(Lexer.END_OF_STREAM);
        
        lexer.readEscape();
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("foo ");
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("foo", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLineLF() throws IOException {
        when(readerMock.lookAhead()).thenReturn(Lexer.LF);
        
        boolean isEndOfLine = lexer.readEndOfLine(Lexer.LF);
        
        assertEquals(true, isEndOfLine);
    }
    
    @Test
    public void testReadEndOfLineCR_LF() throws IOException {
        when(readerMock.lookAhead()).thenReturn(Lexer.LF);
        
        boolean isEndOfLine = lexer.readEndOfLine(Lexer.CR);
        
        assertEquals(true, isEndOfLine);
    }
    
    @Test
    public void testReadEndOfLineCR() throws IOException {
        when(readerMock.lookAhead()).thenReturn(Lexer.UNDEFINED);
        
        boolean isEndOfLine = lexer.readEndOfLine(Lexer.CR);
        
        assertEquals(true, isEndOfLine);
    }
    
    @Test
    public void testIsWhitespace() {
        when(formatMock.getDelimiter()).thenReturn(',');
        
        boolean isWhitespace = lexer.isWhitespace(' ');
        
        assertEquals(true, isWhitespace);
    }
    
    @Test
    public void testIsStartOfLineLF() {
        boolean isStartOfLine = lexer.isStartOfLine(Lexer.LF);
        
        assertEquals(true, isStartOfLine);
    }
    
    @Test
    public void testIsStartOfLineCR() {
        boolean isStartOfLine = lexer.isStartOfLine(Lexer.CR);
        
        assertEquals(true, isStartOfLine);
    }
    
    @Test
    public void testIsStartOfLineUndefined() {
        boolean isStartOfLine = lexer.isStartOfLine(Lexer.UNDEFINED);
        
        assertEquals(true, isStartOfLine);
    }
    
    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);
        
        assertEquals(true, isEndOfFile);
    }
    
    @Test
    public void testIsDelimiter() {
        when(formatMock.getDelimiter()).thenReturn(',');
        
        boolean isDelimiter = lexer.isDelimiter(',');
        
        assertEquals(true, isDelimiter);
    }
    
    @Test
    public void testIsEscape() {
        when(formatMock.getEscape()).thenReturn('\\');
        
        boolean isEscape = lexer.isEscape('\\');
        
        assertEquals(true, isEscape);
    }
    
    @Test
    public void testIsQuoteChar() {
        when(formatMock.getQuoteChar()).thenReturn('\"');
        
        boolean isQuoteChar = lexer.isQuoteChar('\"');
        
        assertEquals(true, isQuoteChar);
    }
    
    @Test
    public void testIsCommentStart() {
        when(formatMock.getCommentStart()).thenReturn('#');
        
        boolean isCommentStart = lexer.isCommentStart('#');
        
        assertEquals(true, isCommentStart);
    }
}