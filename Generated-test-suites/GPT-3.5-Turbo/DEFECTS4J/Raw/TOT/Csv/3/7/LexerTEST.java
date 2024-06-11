import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

public class LexerTest {

    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setup() {
        format = CSVFormat.DEFAULT;
    }

    @Test
    public void testGetLineNumber() {
        StringReader reader = new StringReader("abc\ndef");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape_validEscapeCharacter() throws IOException {
        StringReader reader = new StringReader("\\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        int escapeChar = lexer.readEscape();
        Assert.assertEquals('\n', escapeChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_invalidEscapeCharacter() throws IOException {
        StringReader reader = new StringReader("\\w");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces_withSpaces() {
        StringBuilder buffer = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("abc", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpaces_noSpaces() {
        StringBuilder buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("abc", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_newLineCharacter() throws IOException {
        StringReader reader = new StringReader("\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        boolean isEndOfLine = lexer.readEndOfLine(in.read());
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLine_carriageReturnCharacter() throws IOException {
        StringReader reader = new StringReader("\r");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        boolean isEndOfLine = lexer.readEndOfLine(in.read());
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLine_carriageReturnNewLineCharacters() throws IOException {
        StringReader reader = new StringReader("\r\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in);
        boolean isEndOfLine = lexer.readEndOfLine(in.read());
        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void testIsWhitespace_true() {
        lexer = new Lexer(format, null);
        boolean isWhitespace = lexer.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespace_false() {
        lexer = new Lexer(format, null);
        boolean isWhitespace = lexer.isWhitespace('a');
        Assert.assertFalse(isWhitespace);
    }

    @Test
    public void testIsStartOfLine_true() {
        lexer = new Lexer(format, null);
        boolean isStartOfLine = lexer.isStartOfLine(-1);
        Assert.assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLine_false() {
        lexer = new Lexer(format, null);
        boolean isStartOfLine = lexer.isStartOfLine('a');
        Assert.assertFalse(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile_true() {
        lexer = new Lexer(format, null);
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        Assert.assertTrue(isEndOfFile);
    }

    @Test
    public void testIsEndOfFile_false() {
        lexer = new Lexer(format, null);
        boolean isEndOfFile = lexer.isEndOfFile('a');
        Assert.assertFalse(isEndOfFile);
    }

    @Test
    public void testIsDelimiter_true() {
        lexer = new Lexer(format, null);
        boolean isDelimiter = lexer.isDelimiter(',');
        Assert.assertTrue(isDelimiter);
    }

    @Test
    public void testIsDelimiter_false() {
        lexer = new Lexer(format, null);
        boolean isDelimiter = lexer.isDelimiter('a');
        Assert.assertFalse(isDelimiter);
    }

    @Test
    public void testIsEscape_true() {
        lexer = new Lexer(format, null);
        boolean isEscape = lexer.isEscape('\\');
        Assert.assertTrue(isEscape);
    }

    @Test
    public void testIsEscape_false() {
        lexer = new Lexer(format, null);
        boolean isEscape = lexer.isEscape('a');
        Assert.assertFalse(isEscape);
    }

    @Test
    public void testIsQuoteChar_true() {
        lexer = new Lexer(format, null);
        boolean isQuoteChar = lexer.isQuoteChar('\"');
        Assert.assertTrue(isQuoteChar);
    }

    @Test
    public void testIsQuoteChar_false() {
        lexer = new Lexer(format, null);
        boolean isQuoteChar = lexer.isQuoteChar('a');
        Assert.assertFalse(isQuoteChar);
    }

    @Test
    public void testIsCommentStart_true() {
        lexer = new Lexer(format, null);
        boolean isCommentStart = lexer.isCommentStart('#');
        Assert.assertTrue(isCommentStart);
    }

    @Test
    public void testIsCommentStart_false() {
        lexer = new Lexer(format, null);
        boolean isCommentStart = lexer.isCommentStart('a');
        Assert.assertFalse(isCommentStart);
    }
}