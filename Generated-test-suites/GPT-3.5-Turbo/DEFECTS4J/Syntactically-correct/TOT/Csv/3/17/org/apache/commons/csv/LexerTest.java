import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        lexer = new Lexer(csvFormat, reader);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscapeValid() throws IOException {
        int result = lexer.readEscape();
        Assert.assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalid() throws IOException {
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("example   ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("example", buffer.toString());
    }

    @Test
    public void testReadEndOfLineLF() throws IOException {
        boolean result = lexer.readEndOfLine(10);
        Assert.assertTrue(result);
    }

    @Test
    public void testReadEndOfLineCR() throws IOException {
        boolean result = lexer.readEndOfLine(13);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceTrue() {
        boolean result = lexer.isWhitespace(32);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsWhitespaceFalse() {
        boolean result = lexer.isWhitespace(44);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsStartOfLineTrueLF() {
        boolean result = lexer.isStartOfLine(10);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLineTrueCR() {
        boolean result = lexer.isStartOfLine(13);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLineTrueUndefined() {
        boolean result = lexer.isStartOfLine(-2);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsStartOfLineFalse() {
        boolean result = lexer.isStartOfLine(32);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEndOfFileTrue() {
        boolean result = lexer.isEndOfFile(-1);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEndOfFileFalse() {
        boolean result = lexer.isEndOfFile(10);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsDelimiterTrue() {
        boolean result = lexer.isDelimiter(44);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsDelimiterFalse() {
        boolean result = lexer.isDelimiter(32);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEscapeTrue() {
        boolean result = lexer.isEscape('\\');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEscapeFalse() {
        boolean result = lexer.isEscape(44);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsQuoteCharTrue() {
        boolean result = lexer.isQuoteChar('\"');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsQuoteCharFalse() {
        boolean result = lexer.isQuoteChar(44);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsCommentStartTrue() {
        boolean result = lexer.isCommentStart('#');
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCommentStartFalse() {
        boolean result = lexer.isCommentStart(44);
        Assert.assertFalse(result);
    }
}