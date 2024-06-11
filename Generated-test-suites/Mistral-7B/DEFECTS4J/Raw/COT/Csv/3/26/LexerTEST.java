import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader mockReader;
    private CSVFormat mockFormat;

    @Before
    public void setUp() {
        mockFormat = mock(CSVFormat.class);
        mockReader = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(mockFormat, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        Assert.assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        when(mockReader.read()).thenReturn('r').thenReturn(CR);
        Assert.assertEquals(CR, lexer.readEscape());

        when(mockReader.read()).thenReturn('n').thenReturn(LF);
        Assert.assertEquals(LF, lexer.readEscape());

        when(mockReader.read()).thenReturn('t');
        Assert.assertEquals(TAB, lexer.readEscape());

        when(mockReader.read()).thenReturn(BACKSPACE);
        Assert.assertEquals(BACKSPACE, lexer.readEscape());

        when(mockReader.read()).thenReturn(FF);
        Assert.assertEquals(FF, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
        Assert.assertThrows(IOException.class, () -> lexer.readEscape());

        when(mockReader.read()).thenReturn('x');
        Assert.assertEquals(Constants.UNDEFINED, lexer.readEscape());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        when(mockReader.getLastChar()).thenReturn((char) Constants.LF);
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.CR));
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.LF));
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.CR));
        Assert.assertTrue(lexer.isStartOfLine((int) Constants.CR));
        Assert.assertFalse(lexer.isStartOfLine((int) 'a'));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        when(mockReader.read()).thenReturn((int) Constants.END_OF_STREAM);
        Assert.assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));

        when(mockReader.read()).thenReturn('a');
        Assert.assertFalse(lexer.isEndOfFile((int) 'a'));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        Assert.assertTrue(lexer.isDelimiter((int) lexer.delimiter));
        Assert.assertFalse(lexer.isDelimiter((int) 'a'));
    }

    @Test
    public void testIsEscape() throws IOException {
        Assert.assertTrue(lexer.isEscape((int) lexer.escape));
        Assert.assertFalse(lexer.isEscape((int) 'a'));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        Assert.assertTrue(lexer.isQuoteChar((int) lexer.quoteChar));
        Assert.assertFalse(lexer.isQuoteChar((int) 'a'));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        Assert.assertTrue(lexer.isCommentStart((int) lexer.commmentStart));
        Assert.assertFalse(lexer.isCommentStart((int) 'a'));
    }

    @Test
    public void testNextToken() throws IOException {
        String input = "some,data,with \"quotes\" and comments# ";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        when(mockReader.read()).thenReturn(inputStream);
        when(mockReader.readLine()).thenAnswer(line -> inputStream.readAllBytes());

        Token token = new Token();
        Token result = lexer.nextToken(token);

        Assert.assertEquals("some", token.getValue());
        Assert.assertEquals(Token.TYPE_STRING, token.getType());
        Assert.assertEquals("some", result.getValue());
        Assert.assertEquals(Token.TYPE_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals(",", token.getValue());
        Assert.assertEquals(Token.TYPE_DELIMITER, token.getType());
        Assert.assertEquals(",", result.getValue());
        Assert.assertEquals(Token.TYPE_DELIMITER, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals("data", token.getValue());
        Assert.assertEquals(Token.TYPE_STRING, token.getType());
        Assert.assertEquals("data", result.getValue());
        Assert.assertEquals(Token.TYPE_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals("with ", token.getValue());
        Assert.assertEquals(Token.TYPE_STRING, token.getType());
        Assert.assertEquals("with ", result.getValue());
        Assert.assertEquals(Token.TYPE_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals("quotes", token.getValue());
        Assert.assertEquals(Token.TYPE_STRING, token.getType());
        Assert.assertEquals("quotes", result.getValue());
        Assert.assertEquals(Token.TYPE_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals("and", token.getValue());
        Assert.assertEquals(Token.TYPE_STRING, token.getType());
        Assert.assertEquals("and", result.getValue());
        Assert.assertEquals(Token.TYPE_STRING, result.getType());

        token.reset();
        result = lexer.nextToken(token);

        Assert.assertEquals("# ", token.getValue());
        Assert.assertEquals(Token.TYPE_COMMENT, token.getType());
        Assert.assertEquals("# ", result.getValue());
        Assert.assertEquals(Token.TYPE_COMMENT, result.getType());

        Assert.assertNull(lexer.nextToken(token));
    }
}