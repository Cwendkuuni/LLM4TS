import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {
    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        int escape = lexer.readEscape();
        Assert.assertEquals(-1, escape);
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        boolean endOfLine = lexer.readEndOfLine('\n');
        Assert.assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = lexer.isWhitespace(' ');
        Assert.assertTrue(whitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean startOfLine = lexer.isStartOfLine(UNDEFINED);
        Assert.assertTrue(startOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean endOfFile = lexer.isEndOfFile(END_OF_STREAM);
        Assert.assertTrue(endOfFile);
    }

    @Test
    public void testIsDelimiter() {
        boolean delimiter = lexer.isDelimiter(',');
        Assert.assertTrue(delimiter);
    }

    @Test
    public void testIsEscape() {
        boolean escape = lexer.isEscape('\\');
        Assert.assertTrue(escape);
    }

    @Test
    public void testIsQuoteChar() {
        boolean quoteChar = lexer.isQuoteChar('\"');
        Assert.assertTrue(quoteChar);
    }

    @Test
    public void testIsCommentStart() {
        boolean commentStart = lexer.isCommentStart('#');
        Assert.assertTrue(commentStart);
    }
}