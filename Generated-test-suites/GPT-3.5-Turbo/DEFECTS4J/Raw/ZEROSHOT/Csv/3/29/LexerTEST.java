import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class LexerTest {
    
    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, in);
        assertEquals(0, lexer.getLineNumber());
    }
    
    @Test
    public void testReadEscape() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, in);
        assertEquals(END_OF_STREAM, lexer.readEscape());
    }
    
    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }
    
    @Test
    public void testReadEndOfLine() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, in);
        assertFalse(lexer.readEndOfLine('\n'));
        assertFalse(lexer.readEndOfLine('\r'));
        assertFalse(lexer.readEndOfLine('\r', '\n'));
    }
    
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(','));
    }
    
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\ufffe'));
        assertFalse(lexer.isStartOfLine(' '));
    }
    
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isEndOfFile(-1));
        assertFalse(lexer.isEndOfFile(0));
    }
    
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
    }
    
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isEscape('\ufffe'));
        assertFalse(lexer.isEscape(' '));
    }
    
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isQuoteChar('\ufffe'));
        assertFalse(lexer.isQuoteChar(' '));
    }
    
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader());
        assertTrue(lexer.isCommentStart('\ufffe'));
        assertFalse(lexer.isCommentStart(' '));
    }
}