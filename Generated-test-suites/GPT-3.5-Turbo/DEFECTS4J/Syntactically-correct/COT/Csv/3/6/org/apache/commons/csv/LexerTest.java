
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.*;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private static final String CSV_FILE = "test.csv";

    private Lexer lexer;

    @Before
    public void setUp() throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, new ExtendedBufferedReader(reader));
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        lexer.in.setLastChar('r');
        int escapedChar = lexer.readEscape();
        assertEquals('\r', escapedChar);
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        lexer.in.setLastChar('n');
        int escapedChar = lexer.readEscape();
        assertEquals('\n', escapedChar);
    }

    @Test
    public void testReadEscape_TAB() throws IOException {
        lexer.in.setLastChar('t');
        int escapedChar = lexer.readEscape();
        assertEquals('\t', escapedChar);
    }

    @Test
    public void testReadEscape_BACKSPACE() throws IOException {
        lexer.in.setLastChar('b');
        int escapedChar = lexer.readEscape();
        assertEquals('\b', escapedChar);
    }

    @Test
    public void testReadEscape_FF() throws IOException {
        lexer.in.setLastChar('f');
        int escapedChar = lexer.readEscape();
        assertEquals('\f', escapedChar);
    }

    @Test
    public void testReadEscape_END_OF_STREAM() {
        lexer.in.setLastChar(-1);
        try {
            lexer.readEscape();
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        boolean isEndOfLine = lexer.readEndOfLine('\n');
        assertEquals(true, isEndOfLine);
    }

    @Test
    public void testReadEndOfLine_CR_LF() throws IOException {
        lexer.in.setLastChar('\n');
        boolean isEndOfLine = lexer.readEndOfLine('\r');
        assertEquals(true, isEndOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertEquals(true, isWhitespace);
    }

    @Test
    public void testIsStartOfLine_LF() {
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        assertEquals(true, isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_CR() {
        boolean isStartOfLine = lexer.isStartOfLine('\r');
        assertEquals(true, isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_UNDEFINED() {
        boolean isStartOfLine = lexer.isStartOfLine(-1);
        assertEquals(true, isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        assertEquals(true, isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(',');
        assertEquals(true, isDelimiter);
    }

    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape('\\');
        assertEquals(true, isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar('"');
        assertEquals(true, isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart('#');
        assertEquals(true, isCommentStart);
    }
}

