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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

/**
 * Test class for {@link Lexer}
 *
 * @author [your name]
 */
public class LexerTest {

    /**
     * Test method for {@link Lexer#Lexer(CSVFormat, ExtendedBufferedReader)}
     *
     * @throws IOException when reading from the mock input stream fails
     */
    @Test
    public void testLexerInitialization() throws IOException {
        // given
        final CSVFormat format = new CSVFormat(new CSVParserDefaultConfig.Builder().build());
        final ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        final Lexer lexer = new Lexer(format, in);

        // then
        assertEquals(format.getDelimiter(), lexer.delimiter);
        assertEquals(mapNullToDisabled(format.getEscape()), lexer.escape);
        assertEquals(mapNullToDisabled(format.getQuoteChar()), lexer.quoteChar);
        assertEquals(mapNullToDisabled(format.getCommentStart()), lexer.commmentStart);
        assertEquals(format.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(format.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
        assertEquals(in, lexer.in);
    }

    /**
     * Test method for {@link Lexer#mapNullToDisabled(Character)}
     *
     * @return the mapped character
     */
    private char mapNullToDisabled(Character c) {
        return c == null ? '\ufffe' : c.charValue();
    }

    /**
     * Test method for {@link Lexer#getLineNumber()}
     *
     * @throws IOException when reading from the mock input stream fails
     */
    @Test
    public void testGetLineNumber() throws IOException {
        // given
        final int lineNumber = 123;
        final ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.getLineNumber()).thenReturn(lineNumber);
        final Lexer lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), in);

        // then
        assertEquals(lineNumber, lexer.getLineNumber());
    }

    /**
     * Test method for {@link Lexer#readEscape()}
     *
     * @throws IOException when reading from the mock input stream fails
     */
    @Test
    public void testReadEscape() throws IOException {
        // given
        final ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn('r');

        final Lexer lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), in);

        // when
        final int escapedChar = lexer.readEscape();

        // then
        assertEquals(CR, escapedChar);
    }

    /**
     * Test method for {@link Lexer#trimTrailingSpaces(StringBuilder)}
     */
    @Test
    public void testTrimTrailingSpaces() {
        // given
        final StringBuilder buffer = new StringBuilder("  foo ");

        // when
        lexer.trimTrailingSpaces(buffer);

        // then
        assertEquals("foo", buffer.toString());
    }

    /**
     * Test method for {@link Lexer#isStartOfLine(int)}
     */
    @Test
    public void testIsStartOfLine() {
        // given
        final int cr = CR;
        final int lf = LF;

        // when
        final boolean startOfLineLF = lexer.isStartOfLine(lf);
        final boolean startOfLineCR = lexer.isStartOfLine(cr);
        final boolean startOfLineUndefined = lexer.isStartOfLine(UNDEFINED);

        // then
        assertTrue(startOfLineLF);
        assertTrue(startOfLineCR);
        assertTrue(startOfLineUndefined);
    }

    /**
     * Test method for {@link Lexer#isEndOfFile(int)}
     */
    @Test
    public void testIsEndOfFile() {
        // given
        final int endOfFile = END_OF_STREAM;

        // when
        final boolean isEndOfFile = lexer.isEndOfFile(endOfFile);

        // then
        assertTrue(isEndOfFile);
    }

    /**
     * Test method for {@link Lexer#isDelimiter(int)}
     */
    @Test
    public void testIsDelimiter() {
        // given
        final char delimiter = '\t';

        // when
        final boolean isDelimiter = lexer.isDelimiter((int) delimiter);

        // then
        assertTrue(isDelimiter);
    }

    /**
     * Test method for {@link Lexer#isEscape(int)}
     */
    @Test
    public void testIsEscape() {
        // given
        final char escape = '\u001b'; // ESC character for testing

        // when
        final boolean isEscape = lexer.isEscape((int) escape);

        // then
        assertFalse(isEscape);
    }

    /**
     * Test method for {@link Lexer#isQuoteChar(int)}
     */
    @Test
    public void testIsQuoteChar() {
        // given
        final char quoteChar = '\'';

        // when
        final boolean isQuoteChar = lexer.isQuoteChar((int) quoteChar);

        // then
        assertTrue(isQuoteChar);
    }

    /**
     * Test method for {@link Lexer#isCommentStart(int)}
     */
    @Test
    public void testIsCommentStart() {
        // given
        final char commentStart = '#';

        // when
        final boolean isCommentStart = lexer.isCommentStart((int) commentStart);

        // then
        assertTrue(isCommentStart);
    }

    /**
     * Test method for {@link Lexer#nextToken(Token)}
     *
     * @throws IOException when reading from the mock input stream fails
     */
    @Test
    public void testNextToken() throws IOException {
        // given
        final ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn('A');
        when(in.lookAhead()).thenReturn((char) 0);
        when(in.read()).thenReturn('B');
        when(in.read()).thenReturn(CR);

        final Lexer lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), in);
        final Token token = new Token();

        // when
        lexer.nextToken(token);

        // then
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("AB", token.getValue());
    }

    /**
     * Test method for {@link Lexer#isWhitespace(int)}
     */
    @Test
    public void testIsWhitespace() {
        // given
        final char whitespace = '\u0020'; // space character for testing

        // when
        final boolean isWhitespace = lexer.isWhitespace((int) whitespace);

        // then
        assertTrue(isWhitespace);
    }

    /**
     * Test method for {@link Lexer#readEndOfLine(int)}
     *
     * @throws IOException when reading from the mock input stream fails
     */
    @Test
    public void testReadEndOfLine() throws IOException {
        // given
        final ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        when(in.read()).thenReturn('r');
        when(in.lookAhead()).thenReturn('n');

        final Lexer lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), in);

        // when
        final boolean endOfLine = lexer.readEndOfLine(0);

        // then
        assertTrue(endOfLine);
    }
}