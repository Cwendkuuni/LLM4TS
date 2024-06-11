import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.apache.commons.csv.format.CSVFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(1, lexer.getLineNumber());

        in.readLine();
        Assert.assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\\r"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Constants.CR, lexer.readEscape());
        Assert.assertEquals(Constants.CR, in.getLastChar());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_UnexpectedChar() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("x"));
        Lexer lexer = new Lexer(format, in);

        lexer.readEscape();
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\n"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Constants.LF, lexer.readEscape());
        Assert.assertEquals(Constants.LF, in.getLastChar());
    }

    @Test
    public void testReadEscape_Tab() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\t"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Constants.TAB, lexer.readEscape());
        Assert.assertEquals(Constants.TAB, in.getLastChar());
    }

    @Test
    public void testReadEscape_Backspace() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\b"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Constants.BACKSPACE, lexer.readEscape());
        Assert.assertEquals(Constants.BACKSPACE, in.getLastChar());
    }

    @Test
    public void testReadEscape_FF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\f"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Constants.FF, lexer.readEscape());
        Assert.assertEquals(Constants.FF, in.getLastChar());
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder("  foo   ");
        Lexer lexer = new Lexer(new CSVFormat(','), new ExtendedBufferedReader(new StringReader("")));

        lexer.trimTrailingSpaces(buffer);

        Assert.assertEquals("foo", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_CR() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("Line 1\r"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.readEndOfLine(in.read()));

        Assert.assertEquals(Constants.CR, in.getLastChar());
    }

    @Test
    public void testReadEndOfLine_CRn() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("Line 1\r\nLine 2"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.readEndOfLine(in.read()));
        Assert.assertTrue(lexer.readEndOfLine(in.read()));

        Assert.assertEquals(Constants.LF, in.getLastChar());
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("Line 1\nLine 2"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.readEndOfLine(in.read()));

        Assert.assertEquals(Constants.LF, in.getLastChar());
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLine_EOF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("Line 1"));
        Lexer lexer = new Lexer(format, in);

        lexer.readEndOfLine(in.read());
        lexer.readEndOfLine(in.read());
    }

    @Test
    public void testIsWhitespace() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(" "));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isWhitespace(in.read()));

        Assert.assertEquals(Constants.SPACE, in.getLastChar());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\nLine 1"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isStartOfLine(in.read()));

        Assert.assertEquals(Constants.LF, in.getLastChar());
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader(""));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isEndOfFile(in.read()));

        Assert.assertEquals(Constants.END_OF_STREAM, in.read());
    }

    @Test
    public void testIsDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(',', ',');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("1,2,3"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isDelimiter(in.read()));

        Assert.assertEquals(',', in.getLastChar());
    }

    @Test
    public void testIsEscape() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u0000');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\\"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isEscape(in.read()));

        Assert.assertEquals('\u0000', in.getLastChar());
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        CSVFormat format = new CSVFormat(',', '\'');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("\"foo\""));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isQuoteChar(in.read()));

        Assert.assertEquals('\'', in.getLastChar());
    }

    @Test
    public void testIsCommentStart() throws IOException {
        CSVFormat format = new CSVFormat(',', '#');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new StringReader("#This is a comment"));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isCommentStart(in.read()));

        Assert.assertEquals('#', in.getLastChar());
    }
}