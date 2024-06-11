import org.apache.commons.csv.Constants;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() {
        format = new CSVFormat(new String[]{}, Constants.DEFAULT_LINE_ENDING);
        mockReader = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        Assert.assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        when(mockReader.read()).thenReturn(Constants.CR);
        Assert.assertEquals(Constants.CR, lexer.readEscape());
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        when(mockReader.read()).thenReturn(Constants.LF);
        Assert.assertEquals(Constants.LF, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_UnexpectedCharacter() throws IOException {
        when(mockReader.read()).thenReturn('x');
        lexer.readEscape();
    }

    @Test
    public void testReadEscape_EndOfStream() throws IOException {
        when(mockReader.read()).thenThrow(IOException.class);
        Assert.assertEquals(Constants.END_OF_STREAM, lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder(" test  \t string ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test string", buffer.toString());
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(lexer.isWhitespace((int) ' '));
        Assert.assertTrue(lexer.isWhitespace((int) '\t'));
        Assert.assertTrue(lexer.isWhitespace((int) '\r'));
        Assert.assertTrue(lexer.isWhitespace((int) '\n'));
        Assert.assertFalse(lexer.isWhitespace((int) 'a'));
        Assert.assertFalse(lexer.isWhitespace((int) '\u0001'));
    }

    @Test
    public void testIsStartOfLine() {
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.LF));
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.CR));
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.UNDEFINED));
        Assert.assertFalse(lexer.isStartOfLine((int) 'a'));
    }

    @Test
    public void testIsEndOfFile() {
        Assert.assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));
        Assert.assertFalse(lexer.isEndOfFile((int) 'a'));
    }

    @Test
    public void testIsDelimiter() {
        Assert.assertTrue(lexer.isDelimiter((int) lexer.delimiter));
        Assert.assertFalse(lexer.isDelimiter((int) 'a'));
    }

    @Test
    public void testIsEscape() {
        Assert.assertTrue(lexer.isEscape((int) lexer.escape));
        Assert.assertFalse(lexer.isEscape((int) 'a'));
    }

    @Test
    public void testIsQuoteChar() {
        Assert.assertTrue(lexer.isQuoteChar((int) lexer.quoteChar));
        Assert.assertFalse(lexer.isQuoteChar((int) 'a'));
    }

    @Test
    public void testIsCommentStart() {
        Assert.assertTrue(lexer.isCommentStart((int) lexer.commmentStart));
        Assert.assertFalse(lexer.isCommentStart((int) 'a'));
    }

    @Test
    public void testNextToken_NullToken() throws IOException {
        Token token = Mockito.mock(Token.class);
        when(mockReader.read()).thenReturn(Constants.LF);
        lexer.nextToken(token);
        Assert.assertNull(token.getValue());
    }

    @Test
    public void testNextToken_QuotedString() throws IOException {
        Token token = Mockito.mock(Token.class);
        when(mockReader.read()).thenReturn('"').thenReturn('a').thenReturn('"');
        when(mockReader.read()).thenReturn(Constants.LF);
        lexer.nextToken(token);
        Assert.assertEquals("\"a\"", token.getValue());
    }

    @Test
    public void testNextToken_UnquotedValue() throws IOException {
        Token token = Mockito.mock(Token.class);
        when(mockReader.read()).thenReturn('a').thenReturn(Constants.LF);
        lexer.nextToken(token);
        Assert.assertEquals("a", token.getValue());
    }

    @Test
    public void testNextToken_EmptyLine() throws IOException {
        Token token = Mockito.mock(Token.class);
        when(mockReader.read()).thenReturn(Constants.LF).thenReturn(Constants.LF);
        lexer.nextToken(token);
        Assert.assertNull(token.getValue());
    }

    @Test
    public void testNextToken_EmptyInput() throws IOException {
        Token token = Mockito.mock(Token.class);
        when(mockReader.read()).thenThrow(IOException.class);
        lexer.nextToken(token);
        Assert.assertNull(token.getValue());
    }

}