import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        lexer = new Lexer(format, new ExtendedBufferedReader(reader));
    }

    @Test
    public void testConstructor() {
        assertNotNull(lexer);
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        // TODO implement test case for readEscape() method
    }

    @Test
    public void testTrimTrailingSpaces() {
        // TODO implement test case for trimTrailingSpaces() method
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        // TODO implement test case for readEndOfLine() method
    }

    @Test
    public void testNextToken() throws IOException {
        // TODO implement test case for nextToken() method
    }

    @Test
    public void testIsWhitespace() {
        // TODO implement test case for isWhitespace() method
    }

    @Test
    public void testIsStartOfLine() {
        // TODO implement test case for isStartOfLine() method
    }

    @Test
    public void testIsEndOfFile() {
        // TODO implement test case for isEndOfFile() method
    }

    @Test
    public void testIsDelimiter() {
        // TODO implement test case for isDelimiter() method
    }

    @Test
    public void testIsEscape() {
        // TODO implement test case for isEscape() method
    }

    @Test
    public void testIsQuoteChar() {
        // TODO implement test case for isQuoteChar() method
    }

    @Test
    public void testIsCommentStart() {
        // TODO implement test case for isCommentStart() method
    }
}