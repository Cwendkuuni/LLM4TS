import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    // Test for constructor
    @Test
    public void testLexerConstructor() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertNotNull(lexer);
        assertEquals(format, lexer.format);
        assertEquals(in, lexer.in);
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(format.getEscape(), lexer.escape);
        assertEquals(format.getQuoteChar(), lexer.quoteChar);
        assertEquals(format.getCommentStart(), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }

    // Test for getLineNumber method
    @Test
    public void testGetLineNumber() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertEquals(in.getLineNumber(), lexer.getLineNumber());
    }

    // Test for readEscape method
    @Test
    public void testReadEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        // TODO: Add test cases for readEscape method
    }

    // Test for trimTrailingSpaces method
    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder("test  ");
        lexer.trimTrailingSpaces(buffer);

        assertEquals("test", buffer.toString());

        buffer = new StringBuilder("  test  ");
        lexer.trimTrailingSpaces(buffer);

        assertEquals("  test", buffer.toString());

        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);

        assertEquals("test", buffer.toString());
    }

    // Test for readEndOfLine method
    @Test
    public void testReadEndOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        // TODO: Add test cases for readEndOfLine method
    }

    // Test for isWhitespace method
    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isWhitespace('a'));
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace(','));
    }

    // Test for isStartOfLine method
    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertFalse(lexer.isStartOfLine('a'));
        assertFalse(lexer.isStartOfLine(','));
    }

    // Test for isEndOfFile method
    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isEndOfFile('a'));
        assertFalse(lexer.isEndOfFile(','));
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
    }

    // Test for isDelimiter method
    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isDelimiter('a'));
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter(Lexer.DISABLED));
    }

    // Test for isEscape method
    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isEscape('a'));
        assertFalse(lexer.isEscape(','));
        assertTrue(lexer.isEscape(format.getEscape()));
        assertFalse(lexer.isEscape(Lexer.DISABLED));
    }

    // Test for isQuoteChar method
    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar(','));
        assertTrue(lexer.isQuoteChar(format.getQuoteChar()));
        assertFalse(lexer.isQuoteChar(Lexer.DISABLED));
    }

    // Test for isCommentStart method
    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart(','));
        assertTrue(lexer.isCommentStart(format.getCommentStart()));
        assertFalse(lexer.isCommentStart(Lexer.DISABLED));
    }

    // Test for nextToken method
    @Test
    public void testNextToken() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);

        // TODO: Add test cases for nextToken method
    }
}