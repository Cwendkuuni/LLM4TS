import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class LexerTest {
    
    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new DummyLexer(new CSVFormat());
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testMapNullToDisabled() {
        assertEquals('\ufffe', lexer.mapNullToDisabled(null));
        assertEquals('a', lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testReadEscape() {
        // Test escaping characters
        assertEquals('\r', lexer.readEscape());
        assertEquals('\n', lexer.readEscape());
        assertEquals('\t', lexer.readEscape());
        assertEquals('\b', lexer.readEscape());
        assertEquals('\f', lexer.readEscape());
        // Test invalid escape character
        assertEquals(-1, lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        assertTrue(lexer.readEndOfLine('\r'));
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\n'));
        assertFalse(lexer.isStartOfLine(' '));
    }

    @Test
    public void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
    }

    @Test
    public void testIsEscape() {
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape(' '));
    }

    @Test
    public void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar(' '));
    }

    @Test
    public void testIsCommentStart() {
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart(' '));
    }
}