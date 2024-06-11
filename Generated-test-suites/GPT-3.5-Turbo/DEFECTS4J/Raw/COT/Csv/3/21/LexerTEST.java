import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        try {
            int escape = lexer.readEscape();
            assertEquals(-1, escape);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("hello ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        try {
            boolean endOfLine = lexer.readEndOfLine(10);
            assertEquals(false, endOfLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(32);
        assertEquals(true, isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(10);
        assertEquals(true, isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        assertEquals(true, isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(44);
        assertEquals(false, isDelimiter);
    }

    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape(92);
        assertEquals(true, isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar(34);
        assertEquals(false, isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart(47);
        assertEquals(false, isCommentStart);
    }

}