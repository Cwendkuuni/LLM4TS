
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("line 1\nline 2\nline 3");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);

        Lexer lexer = new Lexer(CSVFormat.DEFAULT, in);

        assertEquals(1L, lexer.getLineNumber());

        in.readLine();
        assertEquals(2L, lexer.getLineNumber());

        in.readLine();
        assertEquals(3L, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        StringReader reader = new StringReader("\\r\\n\\t\\b\\f");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);

        Lexer lexer = new Lexer(CSVFormat.DEFAULT, in);

        // Test regular escapes
        assertEquals('\r', lexer.readEscape());
        assertEquals('\n', lexer.readEscape());
        assertEquals('\t', lexer.readEscape());
        assertEquals('\b', lexer.readEscape());
        assertEquals('\f', lexer.readEscape());

        // Test unexpected character
        assertEquals('\\', in.getLastChar());
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test   ");

        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);
        lexer.trimTrailingSpaces(buffer);

        assertEquals("   test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        StringReader reader = new StringReader("\r\n\ntest\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);

        Lexer lexer = new Lexer(CSVFormat.DEFAULT, in);

        assertTrue(lexer.readEndOfLine(in.read()));
        assertTrue(lexer.readEndOfLine(in.read()));
        assertFalse(lexer.readEndOfLine(in.read()));
        assertFalse(lexer.readEndOfLine(in.read()));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        assertTrue(lexer.isWhitespace('\r'));
        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine(' '));
        assertFalse(lexer.isStartOfLine(','));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(' '));
        assertFalse(lexer.isEndOfFile('\n'));
        assertFalse(lexer.isEndOfFile(','));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('\n'));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('\n'));
        assertFalse(lexer.isEscape(','));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\n'));
        assertFalse(lexer.isQuoteChar(','));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart(','));
        assertFalse(lexer.isCommentStart('a'));
    }
}

