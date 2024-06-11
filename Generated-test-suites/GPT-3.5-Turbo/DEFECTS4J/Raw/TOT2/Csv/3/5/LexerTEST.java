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
    public void testMapNullToDisabled() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertEquals('\ufffe', lexer.mapNullToDisabled(null));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, in);
        
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertFalse(lexer.isWhitespace('a'));
        assertTrue(lexer.isWhitespace(' '));
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\ufffe'));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isEndOfFile(-1));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader());

        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('a'));
    }

    // Additional test cases for specific methods and scenarios can be added here

}