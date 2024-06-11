
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {
    
    private Lexer lexer;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        Reader reader = new StringReader("");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, bufferedReader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_validEscapeSequence() throws IOException {
        ExtendedBufferedReader reader = lexer.in;
        reader.setLastChar('n');
        
        int result = lexer.readEscape();
        
        assertEquals('\n', result);
        assertEquals('n', reader.getLastChar());
    }

    @Test
    public void testReadEscape_invalidEscapeSequence() throws IOException {
        ExtendedBufferedReader reader = lexer.in;
        reader.setLastChar('x');
        
        IOException exception = assertThrows(IOException.class, () -> lexer.readEscape());
        
        assertEquals("EOF whilst processing escape sequence", exception.getMessage());
        assertEquals('x', reader.getLastChar());
    }

    @Test
    public void testTrimTrailingSpaces_bufferWithTrailingSpaces_bufferWithoutTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_bufferWithoutTrailingSpaces_sameBuffer() {
        StringBuilder buffer = new StringBuilder("test");
        
        lexer.trimTrailingSpaces(buffer);
        
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_nextCharacterIsLF_true() throws IOException {
        ExtendedBufferedReader reader = lexer.in;
        reader.setLastChar('\r');
        reader.setLookAhead('\n');
        
        boolean result = lexer.readEndOfLine('\r');
        
        assertTrue(result);
        assertEquals('\n', reader.getLastChar());
    }

    @Test
    public void testReadEndOfLine_nextCharacterIsNotLF_false() throws IOException {
        ExtendedBufferedReader reader = lexer.in;
        reader.setLastChar('\r');
        reader.setLookAhead('x');
        
        boolean result = lexer.readEndOfLine('\r');
        
        assertFalse(result);
        assertEquals('x', reader.getLastChar());
    }

    @Test
    public void testIsWhitespace_whitespaceCharacter_true() {
        boolean result = lexer.isWhitespace(' ');
        
        assertTrue(result);
    }
    
    @Test
    public void testIsWhitespace_nonWhitespaceCharacter_false() {
        boolean result = lexer.isWhitespace('a');
        
        assertFalse(result);
    }
    
    @Test
    public void testIsStartOfLine_startOfLineCharacter_true() {
        boolean result = lexer.isStartOfLine('\n');
        
        assertTrue(result);
    }
    
    @Test
    public void testIsStartOfLine_nonStartOfLineCharacter_false() {
        boolean result = lexer.isStartOfLine('a');
        
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_endOfFileCharacter_true() {
        boolean result = lexer.isEndOfFile(-1);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsEndOfFile_nonEndOfFileCharacter_false() {
        boolean result = lexer.isEndOfFile(0);
        
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_delimiterCharacter_true() {
        boolean result = lexer.isDelimiter(',');
        
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_nonDelimiterCharacter_false() {
        boolean result = lexer.isDelimiter('a');
        
        assertFalse(result);
    }

    @Test
    public void testIsEscape_escapeCharacter_true() {
        boolean result = lexer.isEscape('\\');
        
        assertTrue(result);
    }

    @Test
    public void testIsEscape_nonEscapeCharacter_false() {
        boolean result = lexer.isEscape('a');
        
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_quoteCharacter_true() {
        boolean result = lexer.isQuoteChar('"');
        
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_nonQuoteCharacter_false() {
        boolean result = lexer.isQuoteChar('a');
        
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_commentStartCharacter_true() {
        boolean result = lexer.isCommentStart('#');
        
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_nonCommentStartCharacter_false() {
        boolean result = lexer.isCommentStart('a');
        
        assertFalse(result);
    }
}

