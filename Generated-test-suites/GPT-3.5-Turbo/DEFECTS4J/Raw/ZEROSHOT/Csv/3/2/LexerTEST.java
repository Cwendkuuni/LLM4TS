import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testgetToken() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        Token token = new Token();
        Token result = lexer.nextToken(token);
        Assert.assertEquals(result, token);
    }

    @Test
    public void testgetLineNumber() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        long result = lexer.getLineNumber();
        Assert.assertEquals(result, in.getLineNumber());
    }

    @Test
    public void testreadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        int result = lexer.readEscape();
        Assert.assertEquals(result, in.read());
    }

    @Test
    public void testtrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder();
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals(buffer.length(), 0);
    }

    @Test
    public void testreadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.readEndOfLine(in.read());
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isWhitespace(32);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isStartOfLine(10);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEndOfFile(-1);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isDelimiter(44);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEscape(92);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isQuoteChar(34);
        Assert.assertEquals(result, true);
    }

    @Test
    public void testisCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isCommentStart(35);
        Assert.assertEquals(result, true);
    }
}