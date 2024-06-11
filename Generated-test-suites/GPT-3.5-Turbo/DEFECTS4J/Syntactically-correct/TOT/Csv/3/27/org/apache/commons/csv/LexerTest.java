import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, new ExtendedBufferedReader(new StringReader("")));
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());

        lexer.in.setLineNumber(10);
        assertEquals(10, lexer.getLineNumber());
    }

    @Test
    public void testMapNullToDisabled() {
        assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        assertEquals('c', lexer.mapNullToDisabled('c'));
    }

    @Test
    public void testReadEscape() {
        try {
            assertEquals(Lexer.END_OF_STREAM, lexer.readEscape());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test    ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());

        buffer = new StringBuilder("   test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());

        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        try {
            assertFalse(lexer.readEndOfLine(10));
            assertTrue(lexer.readEndOfLine(Lexer.CR));
            assertTrue(lexer.readEndOfLine(Lexer.LF));
            assertTrue(lexer.readEndOfLine(Lexer.CR));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace('a'));
        assertFalse(lexer.isWhitespace(Lexer.CR));
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        assertTrue(lexer.isEscape(Lexer.DISABLED));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        assertTrue(lexer.isQuoteChar(Lexer.DISABLED));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        assertTrue(lexer.isCommentStart(Lexer.DISABLED));
        assertFalse(lexer.isCommentStart('a'));
    }
}