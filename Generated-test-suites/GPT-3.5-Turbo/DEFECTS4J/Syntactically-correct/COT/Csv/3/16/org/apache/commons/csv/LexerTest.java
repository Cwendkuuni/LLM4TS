import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {

    /*  */

    @Test
    public void testGetLineNumber() {
        Lexer lexer = createLexer();
        Assert.assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = createLexer();
        int escape = lexer.readEscape();
        Assert.assertEquals(Lexer.END_OF_STREAM, escape);
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = createLexer();
        StringBuilder buffer = new StringBuilder("  test  ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("  test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = createLexer();
        Assert.assertFalse(lexer.readEndOfLine('\r'));
        Assert.assertTrue(lexer.readEndOfLine('\n'));
        Assert.assertTrue(lexer.readEndOfLine('\r'));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = createLexer();
        Assert.assertTrue(lexer.isWhitespace(' '));
        Assert.assertFalse(lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = createLexer();
        Assert.assertTrue(lexer.isStartOfLine(Lexer.LF));
        Assert.assertTrue(lexer.isStartOfLine(Lexer.CR));
        Assert.assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        Assert.assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = createLexer();
        Assert.assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        Assert.assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = createLexer();
        Assert.assertTrue(lexer.isDelimiter(','));
        Assert.assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = createLexer();
        Assert.assertFalse(lexer.isEscape('\\'));
        Assert.assertTrue(lexer.isEscape(Lexer.DISABLED));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = createLexer();
        Assert.assertFalse(lexer.isQuoteChar('"'));
        Assert.assertTrue(lexer.isQuoteChar(Lexer.DISABLED));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = createLexer();
        Assert.assertFalse(lexer.isCommentStart('#'));
        Assert.assertTrue(lexer.isCommentStart(Lexer.DISABLED));
    }

    /*  */

    private Lexer createLexer() {
        // Create a lexer instance with necessary dependencies
        CSVFormat format = new CSVFormat(',', '"', '\\', '#', true, true);
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        return new LexerImpl(format, reader);
    }

    // Dummy implementation of ExtendedBufferedReader
    private static class ExtendedBufferedReader {
        public int read() {
            return 0;
        }

        public int lookAhead() {
            return 0;
        }

        public int getLineNumber() {
            return 0;
        }
    }

    // Dummy implementation of Lexer
    private static class LexerImpl extends Lexer {
        public LexerImpl(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) throws IOException {
            return null;
        }
    }
}