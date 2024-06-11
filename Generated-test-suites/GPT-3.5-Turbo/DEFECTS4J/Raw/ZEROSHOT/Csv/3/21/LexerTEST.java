import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        int escape = lexer.readEscape();
        Assert.assertEquals(-1, escape);
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.readEndOfLine(10);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsWhitespace() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isWhitespace(32);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isStartOfLine(10);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEndOfFile(-1);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDelimiter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isDelimiter(44);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isEscape(92);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isQuoteChar(34);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCommentStart() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        boolean result = lexer.isCommentStart(35);
        Assert.assertTrue(result);
    }
}