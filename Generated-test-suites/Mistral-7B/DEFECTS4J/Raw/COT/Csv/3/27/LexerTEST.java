import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new BasicCSVFormat(',', '\u001E', '\u001C', null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("Line 1\nLine 2\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(1, lexer.getLineNumber());

        lexer.in.readLine();
        Assert.assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        CSVFormat format = new BasicCSVFormat(',', '\u001E', '\u001C', null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\r".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(CR, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EOF() throws IOException {
        CSVFormat format = new BasicCSVFormat(',', '\u001E', '\u001C', null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
        Lexer lexer = new Lexer(format, in);

        lexer.readEscape();
    }

    @Test
    public void testReadEscape_Invalid() throws IOException {
        CSVFormat format = new BasicCSVFormat(',', '\u001E', '\u001C', null);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\q".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(-1, lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder stringBuilder = new StringBuilder("   foo   ");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(stringBuilder);
        Assert.assertEquals("foo", stringBuilder.toString());
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);

        Assert.assertTrue(lexer.isWhitespace((int) ' '));
        Assert.assertTrue(lexer.isWhitespace((int) '\t'));
        Assert.assertTrue(lexer.isWhitespace((int) '\n'));
        Assert.assertFalse(lexer.isWhitespace((int) ','));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);

        Assert.assertTrue(lexer.isStartOfLine(UNDEFINED));
        Assert.assertTrue(lexer.isStartOfLine(LF));
        Assert.assertTrue(lexer.isStartOfLine(CR));
        Assert.assertFalse(lexer.isStartOfLine((int) ','));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);

        Assert.assertFalse(lexer.isEndOfFile(UNDEFINED));
        Assert.assertFalse(lexer.isEndOfFile(LF));
        Assert.assertFalse(lexer.isEndOfFile(CR));
        Assert.assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(new BasicCSVFormat(',', null, null, null), null);

        Assert.assertTrue(lexer.isDelimiter((int) ','));
        Assert.assertFalse(lexer.isDelimiter((int) ' '));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(new BasicCSVFormat(',', null, '\u001E', null), null);

        Assert.assertTrue(lexer.isEscape((int) '\u001E'));
        Assert.assertFalse(lexer.isEscape((int) ','));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(new BasicCSVFormat(',', null, null, '\u001C'), null);

        Assert.assertTrue(lexer.isQuoteChar((int) '\u001C'));
        Assert.assertFalse(lexer.isQuoteChar((int) ','));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(new BasicCSVFormat(',', null, null, '\u0023'), null);

        Assert.assertTrue(lexer.isCommentStart((int) '\u0023'));
        Assert.assertFalse(lexer.isCommentStart((int) ','));
    }
}