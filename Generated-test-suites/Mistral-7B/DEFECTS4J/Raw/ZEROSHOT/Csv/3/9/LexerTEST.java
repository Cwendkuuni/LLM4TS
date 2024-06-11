import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    private ExtendedBufferedReader mockInput;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setup() {
        mockInput = mock(ExtendedBufferedReader.class);
        format = new CSVFormatBuilder() // NOSONAR
                .setDelimiter(',') // NOSONAR
                .setHeaderSeparator(';') // NOSONAR
                .build();
        lexer = new Lexer(format, mockInput);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockInput.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        when(mockInput.read()).thenReturn('r');
        assertEquals(CR, lexer.readEscape());

        when(mockInput.read()).thenReturn('n');
        assertEquals(LF, lexer.readEscape());

        when(mockInput.read()).thenReturn('\n');
        assertEquals(LF, lexer.readEscape());

        when(mockInput.read()).thenReturn(CR);
        assertEquals(CR, lexer.readEscape());

        when(mockInput.read()).thenReturn(TAB);
        assertEquals(TAB, lexer.readEscape());

        when(mockInput.read()).thenReturn(BACKSPACE);
        assertEquals(BACKSPACE, lexer.readEscape());

        when(mockInput.read()).thenReturn(FF);
        assertEquals(FF, lexer.readEscape());

        when(mockInput.read()).thenThrow(new IOException("End of stream"));
        assertFalse(lexer.hasNext());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        when(mockInput.read()).thenReturn(escape);
        lexer.readEscape();
        when(mockInput.read()).thenThrow(new IOException("End of stream"));
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder stringBuilder = new StringBuilder("   foo   ");
        lexer.trimTrailingSpaces(stringBuilder);
        assertEquals("foo", stringBuilder.toString());
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(LF));
        assertFalse(lexer.isStartOfLine(0));
        assertFalse(lexer.isStartOfLine('x'));
    }

    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile(UNDEFINED));
        assertFalse(lexer.isEndOfFile(CR));
        assertFalse(lexer.isEndOfFile(LF));
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(UNDEFINED));
        assertFalse(lexer.isEscape(','));
        assertFalse(lexer.isEscape(CR));
        assertFalse(lexer.isEscape(LF));
        assertFalse(lexer.isEscape(TAB));
        assertFalse(lexer.isEscape(BACKSPACE));
        assertFalse(lexer.isEscape(FF));
        assertTrue(lexer.isEscape(escape));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(UNDEFINED));
        assertFalse(lexer.isQuoteChar(','));
        assertFalse(lexer.isQuoteChar(CR));
        assertFalse(lexer.isQuoteChar(LF));
        assertFalse(lexer.isQuoteChar(TAB));
        assertFalse(lexer.isQuoteChar(BACKSPACE));
        assertFalse(lexer.isQuoteChar(FF));
        assertFalse(lexer.isQuoteChar(quoteChar));
        assertTrue(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(UNDEFINED));
        assertFalse(lexer.isCommentStart(','));
        assertFalse(lexer.isCommentStart(CR));
        assertFalse(lexer.isCommentStart(LF));
        assertFalse(lexer.isCommentStart(TAB));
        assertFalse(lexer.isCommentStart(BACKSPACE));
        assertFalse(lexer.isCommentStart(FF));
        assertFalse(lexer.isCommentStart(commmentStart));
        assertTrue(lexer.isCommentStart(format.getCommentStart()));
    }

    @Test
    public void testNextToken() throws IOException {
        when(mockInput.read()).thenReturn(',');
        Token token = lexer.nextToken(null);
        assertEquals(TokenType.DELIMITER, token.getType());
        assertEquals(',', token.getValue()[0]);

        when(mockInput.read()).thenReturn('"');
        token = lexer.nextToken(null);
        assertEquals(TokenType.QUOTED_STRING, token.getType());
        assertEquals("", token.getValue());

        when(mockInput.read()).thenReturn('"').thenReturn('a');
        token = lexer.nextToken(null);
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("a", token.getValue());

        when(mockInput.read()).thenReturn(';');
        token = lexer.nextToken(null);
        assertEquals(TokenType.HEADER, token.getType());
        assertEquals("", token.getValue());

        when(mockInput.read()).thenReturn(CR);
        token = lexer.nextToken(null);
        assertEquals(TokenType.START_RECORD, token.getType());
        assertTrue(token.isStart());

        when(mockInput.read()).thenReturn(CR).thenReturn(LF);
        token = lexer.nextToken(null);
        assertEquals(TokenType.END_RECORD, token.getType());
        assertTrue(token.isEnd());

        when(mockInput.read()).thenReturn(LF);
        token = lexer.nextToken(null);
        assertEquals(TokenType.LINE_BREAK, token.getType());

        when(mockInput.read()).thenReturn('x');
        token = lexer.nextToken(null);
        assertEquals(TokenType.TEXT, token.getType());
        assertEquals("x", token.getValue());

        when(mockInput.read()).thenReturn(UNDEFINED);
        token = lexer.nextToken(null);
        assertNull(token);

        when(mockInput.read()).thenThrow(new IOException("End of stream"));
        assertFalse(lexer.hasNext());
    }

    @Test
    public void testNextTokenWithTokenBuffer() throws IOException {
        when(mockInput.read()).thenReturn(',');
        Token token = new Token(TokenType.DELIMITER, new char[] { ',' });
        Token reusableToken = lexer.nextToken(token);
        assertEquals(',', reusableToken.getValue()[0]);
        assertEquals(TokenType.DELIMITER, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn('"');
        token = new Token(TokenType.QUOTED_STRING, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertEquals("", reusableToken.getValue());
        assertEquals(TokenType.QUOTED_STRING, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn('"').thenReturn('a');
        token = new Token(TokenType.STRING, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertEquals("a", reusableToken.getValue());
        assertEquals(TokenType.STRING, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn(';');
        token = new Token(TokenType.HEADER, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertEquals("", reusableToken.getValue());
        assertEquals(TokenType.HEADER, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn(CR);
        token = new Token(TokenType.START_RECORD, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertTrue(reusableToken.isStart());
        assertEquals(TokenType.START_RECORD, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn(CR).thenReturn(LF);
        token = new Token(TokenType.END_RECORD, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertTrue(reusableToken.isEnd());
        assertEquals(TokenType.END_RECORD, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn(LF);
        token = new Token(TokenType.LINE_BREAK, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertEquals(TokenType.LINE_BREAK, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn('x');
        token = new Token(TokenType.TEXT, new char[] { 'x' });
        reusableToken = lexer.nextToken(token);
        assertEquals("x", reusableToken.getValue());
        assertEquals(TokenType.TEXT, reusableToken.getType());
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenReturn(UNDEFINED);
        token = new Token(TokenType.NULL, new char[0]);
        reusableToken = lexer.nextToken(token);
        assertNull(reusableToken);
        assertEquals(token, lexer.getLastReadToken());

        when(mockInput.read()).thenThrow(new IOException("End of stream"));
        assertFalse(lexer.hasNext());
    }

    @Test
    public void testHasNext() throws IOException {
        List<Character> input = new ArrayList<Character>();
        ExtendedBufferedReader mockInput2 = mock(ExtendedBufferedReader.class);
        when(mockInput2.read()).thenReturn(input.remove(0));
        when(mockInput2.getLastChar()).thenReturn(input.isEmpty() ? UNDEFINED : input.get(0));
        when(mockInput2.readLine()).thenReturn(null);

        lexer = new Lexer(format, mockInput2);

        input.add(',');
        assertTrue(lexer.hasNext());

        input.add('"');
        assertTrue(lexer.hasNext());

        input.add('a');
        assertTrue(lexer.hasNext());

        input.add(';');
        assertTrue(lexer.hasNext());

        input.add(CR);
        assertTrue(lexer.hasNext());

        input.add(CR);
        assertTrue(lexer.hasNext());

        input.add(LF);
        assertTrue(lexer.hasNext());

        input.add('x');
        assertTrue(lexer.hasNext());

        input.clear();
        assertFalse(lexer.hasNext());
    }
}