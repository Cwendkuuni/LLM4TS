import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    // Test constructor
    @Test
    public void testConstructor() {
        Lexer lexer1 = new Lexer(format, in);
        assertNotNull(lexer1);

        Lexer lexer2 = new Lexer(null, null);
        assertNotNull(lexer2);
    }

    // Test getLineNumber method
    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    // Test readEscape method
    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new Lexer(format, in);

        // Test escape sequence '\r'
        in.setLastChar('r');
        int result = lexer.readEscape();
        assertEquals('\r', result);

        // Test escape sequence '\n'
        in.setLastChar('n');
        result = lexer.readEscape();
        assertEquals('\n', result);

        // Test escape sequence '\t'
        in.setLastChar('t');
        result = lexer.readEscape();
        assertEquals('\t', result);

        // Test escape sequence '\b'
        in.setLastChar('b');
        result = lexer.readEscape();
        assertEquals('\b', result);

        // Test escape sequence '\f'
        in.setLastChar('f');
        result = lexer.readEscape();
        assertEquals('\f', result);

        // Test escape sequence '\\'
        in.setLastChar('\\');
        result = lexer.readEscape();
        assertEquals('\\', result);

        // Test escape sequence unsupported character
        in.setLastChar('x');
        try {
            result = lexer.readEscape();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    // Test trimTrailingSpaces method
    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);

        // Test buffer with trailing spaces
        StringBuilder buffer1 = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(buffer1);
        assertEquals("abc", buffer1.toString());

        // Test buffer without trailing spaces
        StringBuilder buffer2 = new StringBuilder("abc");
        lexer.trimTrailingSpaces(buffer2);
        assertEquals("abc", buffer2.toString());

        // Test empty buffer
        StringBuilder buffer3 = new StringBuilder();
        lexer.trimTrailingSpaces(buffer3);
        assertEquals("", buffer3.toString());
    }

    // Test readEndOfLine method
    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(format, in);

        // Test LF character
        in.setLookAhead('A');
        in.setLastChar('\n');
        boolean result1 = lexer.readEndOfLine(in.getLastChar());
        assertTrue(result1);

        // Test CR character
        in.setLookAhead('A');
        in.setLastChar('\r');
        boolean result2 = lexer.readEndOfLine(in.getLastChar());
        assertTrue(result2);

        // Test CR LF character
        in.setLookAhead('A');
        in.setLastChar('\r');
        in.read();
        boolean result3 = lexer.readEndOfLine(in.getLastChar());
        assertTrue(result3);

        // Test character sequence not representing end of line
        in.setLookAhead('A');
        in.setLastChar('x');
        boolean result4 = lexer.readEndOfLine(in.getLastChar());
        assertFalse(result4);
    }

    // Test nextToken method
    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new Lexer(format, in);
        Token reusableToken = new Token();

        // Test implementation of nextToken method
        Token result = lexer.nextToken(reusableToken);
        assertNotNull(result);
    }

    // Test isWhitespace method
    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);

        // Test whitespace character
        boolean result1 = lexer.isWhitespace(' ');
        assertTrue(result1);

        // Test non-whitespace character
        boolean result2 = lexer.isWhitespace('A');
        assertFalse(result2);
    }

    // Test isStartOfLine method
    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);

        // Test start of line character
        boolean result1 = lexer.isStartOfLine('\n');
        assertTrue(result1);

        // Test non-start of line character
        boolean result2 = lexer.isStartOfLine('A');
        assertFalse(result2);
    }

    // Test isEndOfFile method
    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);

        // Test end of file character
        boolean result1 = lexer.isEndOfFile(-1);
        assertTrue(result1);

        // Test non-end of file character
        boolean result2 = lexer.isEndOfFile('A');
        assertFalse(result2);
    }

    // Test isDelimiter method
    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);

        // Test delimiter character
        boolean result1 = lexer.isDelimiter(',');
        assertTrue(result1);

        // Test non-delimiter character
        boolean result2 = lexer.isDelimiter('A');
        assertFalse(result2);
    }

    // Test isEscape method
    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);

        // Test escape character
        boolean result1 = lexer.isEscape('\\');
        assertTrue(result1);

        // Test non-escape character
        boolean result2 = lexer.isEscape('A');
        assertFalse(result2);
    }

    // Test isQuoteChar method
    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);

        // Test quote character
        boolean result1 = lexer.isQuoteChar('"');
        assertTrue(result1);

        // Test non-quote character
        boolean result2 = lexer.isQuoteChar('A');
        assertFalse(result2);
    }

    // Test isCommentStart method
    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);

        // Test comment start character
        boolean result1 = lexer.isCommentStart('#');
        assertTrue(result1);

        // Test non-comment start character
        boolean result2 = lexer.isCommentStart('A');
        assertFalse(result2);
    }
}