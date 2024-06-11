import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        //TODO: Assert the value of lineNumber
    }

    @Test
    public void testReadEscape() throws IOException {
        int escapedChar = lexer.readEscape();
        //TODO: Assert the value of escapedChar
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        //TODO: Assert the length of buffer
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        boolean endOfLine = lexer.readEndOfLine(13);
        //TODO: Assert the value of endOfLine
    }

    @Test
    public void testNextToken() throws IOException {
        Token reusableToken = new Token();
        Token nextToken = lexer.nextToken(reusableToken);
        //TODO: Assert the value of nextToken
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = lexer.isWhitespace(32);
        //TODO: Assert the value of whitespace
    }

    @Test
    public void testIsStartOfLine() {
        boolean startOfLine = lexer.isStartOfLine(10);
        //TODO: Assert the value of startOfLine
    }

    @Test
    public void testIsEndOfFile() {
        boolean endOfFile = lexer.isEndOfFile(-1);
        //TODO: Assert the value of endOfFile
    }

    @Test
    public void testIsDelimiter() {
        boolean delimiter = lexer.isDelimiter(44);
        //TODO: Assert the value of delimiter
    }

    @Test
    public void testIsEscape() {
        boolean escape = lexer.isEscape(92);
        //TODO: Assert the value of escape
    }

    @Test
    public void testIsQuoteChar() {
        boolean quoteChar = lexer.isQuoteChar(34);
        //TODO: Assert the value of quoteChar
    }

    @Test
    public void testIsCommentStart() {
        boolean commentStart = lexer.isCommentStart(35);
        //TODO: Assert the value of commentStart
    }
}