import org.junit.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscapeValidEscapeSequence() throws IOException {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        int escapeChar = lexer.readEscape();
        Assert.assertEquals(LF, escapeChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidEscapeSequence() throws IOException {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        StringBuilder buffer = new StringBuilder("   Hello World   ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("   Hello World", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean endOfLine = lexer.readEndOfLine(LF);
        Assert.assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean whitespace = lexer.isWhitespace(' ');
        Assert.assertTrue(whitespace);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean startOfLine = lexer.isStartOfLine(LF);
        Assert.assertTrue(startOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean endOfFile = lexer.isEndOfFile(-1);
        Assert.assertTrue(endOfFile);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean delimiter = lexer.isDelimiter(',');
        Assert.assertTrue(delimiter);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean escape = lexer.isEscape('\\');
        Assert.assertTrue(escape);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean quoteChar = lexer.isQuoteChar('\"');
        Assert.assertTrue(quoteChar);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, new ExtendedBufferedReader());
        boolean commentStart = lexer.isCommentStart('#');
        Assert.assertTrue(commentStart);
    }
}