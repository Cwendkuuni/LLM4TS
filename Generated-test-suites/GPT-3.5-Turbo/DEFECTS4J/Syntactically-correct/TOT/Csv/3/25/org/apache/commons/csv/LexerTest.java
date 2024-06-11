
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    // Tokenize

    @Test
    public void testTokenizeWithNormalInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("abc,def,ghi"));
        Token token = lexer.nextToken(null);
        assertEquals(Token.Type.VALUE, token.getType());
        assertEquals("abc", token.getText());

        token = lexer.nextToken(null);
        assertEquals(Token.Type.DELIMITER, token.getType());
        assertEquals(",", token.getText());

        token = lexer.nextToken(null);
        assertEquals(Token.Type.VALUE, token.getType());
        assertEquals("def", token.getText());

        token = lexer.nextToken(null);
        assertEquals(Token.Type.DELIMITER, token.getType());
        assertEquals(",", token.getText());

        token = lexer.nextToken(null);
        assertEquals(Token.Type.VALUE, token.getType());
        assertEquals("ghi", token.getText());
    }

    @Test
    public void testTokenizeWithEmptyInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader(""));
        Token token = lexer.nextToken(null);
        assertNull(token);
    }

    @Test
    public void testTokenizeWithWhitespaceInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("  "));
        Token token = lexer.nextToken(null);
        assertNull(token);
    }

    // getLineNumber

    @Test
    public void testGetLineNumberWithNormalInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("abc"));
        long lineNumber = lexer.getLineNumber();
        assertEquals(1, lineNumber);
    }

    @Test
    public void testGetLineNumberWithLineTerminatorInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\n"));
        long lineNumber = lexer.getLineNumber();
        assertEquals(2, lineNumber);
    }

    @Test
    public void testGetLineNumberWithMultipleLinesInput() {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("abc\ndef\nghi"));
        long lineNumber = lexer.getLineNumber();
        assertEquals(3, lineNumber);
    }

    // readEscape

    @Test
    public void testReadEscapeWithValidEscapeCharacter() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\\n"));
        int escapedChar = lexer.readEscape();
        assertEquals('\n', escapedChar);
        assertEquals('\\', lexer.in.getLastChar());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidEscapeCharacter() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\\x"));
        lexer.readEscape();
    }

    // trimTrailingSpaces

    @Test
    public void testTrimTrailingSpacesWithTrailingSpaces() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        StringBuilder buffer = new StringBuilder("abc  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpacesWithoutTrailingSpaces() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        StringBuilder buffer = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("abc", buffer.toString());
    }

    // readEndOfLine

    @Test
    public void testReadEndOfLineWithLFInput() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\n"));
        boolean isEndOfLine = lexer.readEndOfLine('\n');
        assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithCRInput() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\r"));
        boolean isEndOfLine = lexer.readEndOfLine('\r');
        assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithCRLFInput() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("\r\n"));
        boolean isEndOfLine = lexer.readEndOfLine('\r');
        assertTrue(isEndOfLine);
    }

    @Test
    public void testReadEndOfLineWithNonLineTerminatorInput() throws IOException {
        Lexer lexer = new Lexer(new CSVFormat(), new ExtendedBufferedReader("a"));
        boolean isEndOfLine = lexer.readEndOfLine('a');
        assertFalse(isEndOfLine);
    }

    // isWhitespace

    @Test
    public void testIsWhitespaceWithWhitespaceInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isWhitespace = lexer.isWhitespace(' ');
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isWhitespace = lexer.isWhitespace('a');
        assertFalse(isWhitespace);
    }

    // isStartOfLine

    @Test
    public void testIsStartOfLineWithStartOfLineInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isStartOfLine = lexer.isStartOfLine('\n');
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsStartOfLineWithNonStartOfLineInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isStartOfLine = lexer.isStartOfLine('a');
        assertFalse(isStartOfLine);
    }
    
    // isEndOfFile

    @Test
    public void testIsEndOfFileWithEndOfFileInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsEndOfFileWithNonEndOfFileInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isEndOfFile = lexer.isEndOfFile(0);
        assertFalse(isEndOfFile);
    }
    
    // isDelimiter

    @Test
    public void testIsDelimiterWithDelimiterInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isDelimiter = lexer.isDelimiter(',');
        assertTrue(isDelimiter);
    }

    @Test
    public void testIsDelimiterWithNonDelimiterInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isDelimiter = lexer.isDelimiter(';');
        assertFalse(isDelimiter);
    }
    
    // isEscape

    @Test
    public void testIsEscapeWithEscapeInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isEscape = lexer.isEscape('\\');
        assertTrue(isEscape);
    }

    @Test
    public void testIsEscapeWithNonEscapeInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isEscape = lexer.isEscape('/');
        assertFalse(isEscape);
    }
    
    // isQuoteChar

    @Test
    public void testIsQuoteCharWithQuoteCharInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isQuoteChar = lexer.isQuoteChar('"');
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsQuoteCharWithNonQuoteCharInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isQuoteChar = lexer.isQuoteChar('\'');
        assertFalse(isQuoteChar);
    }
    
    // isCommentStart

    @Test
    public void testIsCommentStartWithCommentStartInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isCommentStart = lexer.isCommentStart('#');
        assertTrue(isCommentStart);
    }

    @Test
    public void testIsCommentStartWithNonCommentStartInput() {
        Lexer lexer = new Lexer(new CSVFormat(), null);
        boolean isCommentStart = lexer.isCommentStart('/');
        assertFalse(isCommentStart);
    }
}

