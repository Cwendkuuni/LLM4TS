import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        int escape = lexer.readEscape();
        Assert.assertEquals(0, escape);
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean endOfLine = lexer.readEndOfLine(10);
        Assert.assertEquals(true, endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isWhitespace = lexer.isWhitespace(32);
        Assert.assertEquals(true, isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isStartOfLine = lexer.isStartOfLine(10);
        Assert.assertEquals(true, isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        Assert.assertEquals(true, isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isDelimiter = lexer.isDelimiter(44);
        Assert.assertEquals(true, isDelimiter);
    }

    @Test
    public void testIsEscape() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isEscape = lexer.isEscape(92);
        Assert.assertEquals(true, isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isQuoteChar = lexer.isQuoteChar(34);
        Assert.assertEquals(true, isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        CSVFormat format = new CSVFormat();
        Lexer lexer = new Lexer(format, reader);
        boolean isCommentStart = lexer.isCommentStart(35);
        Assert.assertEquals(true, isCommentStart);
    }

    // Additional test methods for nextToken(Token reusableToken) can be implemented here

}