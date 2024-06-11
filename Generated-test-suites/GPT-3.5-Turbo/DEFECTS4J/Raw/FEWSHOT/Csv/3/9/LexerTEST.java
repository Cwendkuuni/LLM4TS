package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    private CSVFormat csvFormat;
    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
        reader = new ExtendedBufferedReader(null);
    }

    @Test
    public void testLexerConstructor() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer instanceof Lexer);
        assertEquals(csvFormat, lexer.format);
        assertEquals(reader, lexer.in);
        assertEquals(csvFormat.getDelimiter(), lexer.delimiter);
        assertEquals(csvFormat.getEscape().charValue(), lexer.escape);
        assertEquals(csvFormat.getQuoteChar().charValue(), lexer.quoteChar);
        assertEquals(csvFormat.getCommentStart().charValue(), lexer.commmentStart);
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), lexer.ignoreSurroundingSpaces);
        assertEquals(csvFormat.getIgnoreEmptyLines(), lexer.ignoreEmptyLines);
    }

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertEquals(0, lexer.getLineNumber());

        reader.setLineNumber(5);

        assertEquals(5, lexer.getLineNumber());
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(CR));
        assertFalse(lexer.isStartOfLine(' '));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('/'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('!'));
    }

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertEquals(lexer.mapNullToDisabled('a'), 'a');
        assertEquals(lexer.mapNullToDisabled(null), DISABLED);
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertFalse(lexer.readEndOfLine('a'));

        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));

        reader.unread('a');
        reader.unread('\r');

        assertTrue(lexer.readEndOfLine('\r'));
        assertEquals('\n', reader.read());
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(csvFormat, reader);

        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());

        buffer = new StringBuilder("  ");
        lexer.trimTrailingSpaces(buffer);
        assertTrue(buffer.toString().isEmpty());

        buffer = new StringBuilder("test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithEndOfStream() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);

        reader.setEOF(true);
        lexer.readEscape();
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());

        reader.unread('r');
        reader.unread('f');

        assertEquals('r', lexer.readEscape());
        assertEquals('f', lexer.readEscape());

        reader.setEOF(true);
        try {
            lexer.readEscape();
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new Lexer(csvFormat, reader);
        Token reusableToken = new Token();

        Token token = lexer.nextToken(reusableToken);
        assertNull(token);

        // TODO: Add more test cases for all possible token types
    }

}