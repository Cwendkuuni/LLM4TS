import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_EscapeCharacter() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\\', result);
            assertEquals('r', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEscape_CarriageReturn() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\r', result);
            assertEquals('n', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEscape_LineFeed() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\n', result);
            assertEquals('t', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEscape_Tab() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\t', result);
            assertEquals('b', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEscape_Backspace() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\b', result);
            assertEquals('f', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEscape_InvalidChar() {
        try {
            char result = (char)lexer.readEscape();
            assertEquals('\ufffe', result);
            assertEquals('x', in.getLastChar());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EndOfStream() throws IOException {
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_NoSpaces() {
        StringBuilder buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_SpacesRemoved() {
        StringBuilder buffer = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LineFeed() {
        try {
            boolean result = lexer.readEndOfLine('\n');
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEndOfLine_CarriageReturn() {
        try {
            boolean result = lexer.readEndOfLine('\r');
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEndOfLine_CarriageReturnAndLineFeed() {
        try {
            boolean result = lexer.readEndOfLine('\r');
            assertFalse(result);
            result = lexer.readEndOfLine('\n');
            assertTrue(result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadEndOfLine_NotEndOfLine() {
        try {
            boolean result = lexer.readEndOfLine('a');
            assertFalse(result);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace_WhitespaceCharacter() {
        boolean result = lexer.isWhitespace(' ');
        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_NotWhitespaceCharacter() {
        boolean result = lexer.isWhitespace('a');
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_StartOfLine() {
        boolean result = lexer.isStartOfLine('\n');
        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_NotStartOfLine() {
        boolean result = lexer.isStartOfLine('a');
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_EndOfFile() {
        boolean result = lexer.isEndOfFile(-1);
        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_NotEndOfFile() {
        boolean result = lexer.isEndOfFile(0);
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_DelimiterCharacter() {
        boolean result = lexer.isDelimiter(',');
        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_NotDelimiterCharacter() {
        boolean result = lexer.isDelimiter('a');
        assertFalse(result);
    }

    @Test
    public void testIsEscape_EscapeCharacter() {
        boolean result = lexer.isEscape('\\');
        assertTrue(result);
    }

    @Test
    public void testIsEscape_NotEscapeCharacter() {
        boolean result = lexer.isEscape('a');
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_QuoteCharacter() {
        boolean result = lexer.isQuoteChar('\'');
        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_NotQuoteCharacter() {
        boolean result = lexer.isQuoteChar('a');
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_CommentStarterCharacter() {
        boolean result = lexer.isCommentStart('#');
        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_NotCommentStarterCharacter() {
        boolean result = lexer.isCommentStart('a');
        assertFalse(result);
    }
}