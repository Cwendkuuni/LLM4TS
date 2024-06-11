import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(lexer.mapNullToDisabled(null), '\ufffe');
        assertEquals(lexer.mapNullToDisabled('a'), 'a');
    }

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        assertEquals(lexer.getLineNumber(), 0);
    }

    @Test
    public void testReadEscape() {
        Lexer lexer = new Lexer(format, in);
        int result;
        try {
            result = lexer.readEscape();
            assertEquals(result, -1); // Expected result when at end of stream
        } catch(IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals(buffer.toString(), "test");
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new Lexer(format, in);
        boolean result;
        try {
            result = lexer.readEndOfLine('\n');
            assertTrue(result);
        } catch(IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(LF));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('/'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('/'));
    }
}