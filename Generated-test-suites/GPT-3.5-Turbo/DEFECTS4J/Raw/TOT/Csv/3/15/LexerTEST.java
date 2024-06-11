import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testMapNullToDisabled() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(Lexer.DISABLED, lexer.mapNullToDisabled(null));
        Assert.assertEquals('a', lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testReadEscape() throws IOException {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        int result = lexer.readEscape();
        Assert.assertEquals(Lexer.END_OF_STREAM, result);
    }

    @Test
    public void testTrimTrailingSpaces() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder(" abc ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals(" abc", buffer.toString());
    }

    @Test
    public void testIsWhitespace() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isWhitespace(' '));
        Assert.assertFalse(lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isStartOfLine(Lexer.LF));
        Assert.assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        Assert.assertFalse(lexer.isEndOfFile(','));

    }

    @Test
    public void testIsDelimiter() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isDelimiter(','));
        Assert.assertFalse(lexer.isDelimiter(' '));
    }

    @Test
    public void testIsEscape() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isEscape('"'));
        Assert.assertFalse(lexer.isEscape(' '));
    }

    @Test
    public void testIsQuoteChar() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isQuoteChar('"'));
        Assert.assertFalse(lexer.isQuoteChar(' '));
    }

    @Test
    public void testIsCommentStart() {
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        CSVFormat format = new CSVFormat(',', '"', '#', false, false);

        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isCommentStart('#'));
        Assert.assertFalse(lexer.isCommentStart(' '));
    }
}