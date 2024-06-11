package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new DummyLexer();
        int[] chars = {CR, LF, UNDEFINED};

        for (int c : chars) {
            assertTrue(lexer.isStartOfLine(c));
        }
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new DummyLexer();
        int[] chars = {UNDEFINED, END_OF_STREAM};

        for (int c : chars) {
            assertTrue(lexer.isEndOfFile(c));
        }
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new DummyLexer();
        char delimiter = ',';
        lexer = new DummyLexer(delimiter);

        assertTrue(lexer.isDelimiter((int) delimiter));
        assertFalse(lexer.isDelimiter((int) '\n'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new DummyLexer();
        char escape = '\\';
        lexer = new DummyLexer(escape);

        assertTrue(lexer.isEscape((int) escape));
        assertFalse(lexer.isEscape((int) ','));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new DummyLexer();
        char quoteChar = '\'';
        lexer = new DummyLexer(quoteChar);

        assertTrue(lexer.isQuoteChar((int) quoteChar));
        assertFalse(lexer.isQuoteChar((int) '\n'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new DummyLexer();
        char commentStart = '#';
        lexer = new DummyLexer(commentStart);

        assertTrue(lexer.isCommentStart((int) commentStart));
        assertFalse(lexer.isCommentStart((int) '\n'));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new DummyLexer();
        char[] chars = {' ', '\t', '\r'};

        for (char c : chars) {
            assertTrue(lexer.isWhitespace((int) c));
        }
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new DummyLexer();

        int[] chars = {CR, LF, CR, LF};

        for (int i = 0; i < chars.length; i++) {
            assertTrue(lexer.readEndOfLine(chars[i]));
        }
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLineWithInvalidChar() throws IOException {
        Lexer lexer = new DummyLexer();
        lexer.readEndOfLine('a');
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder(" foo ");
        Lexer lexer = new DummyLexer();
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo", buffer.toString());
    }

    @Test
    public void testHandleEscape() {
        Lexer lexer = new DummyLexer();
        char escape = '\\';
        lexer = new DummyLexer(escape);

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());

        int unexpectedChar = 'a';
        assertEquals(unexpectedChar, lexer.readEscape()); // should throw an exception
    }
}

class DummyLexer extends Lexer {

    public DummyLexer() {
        this(null, null, null, null, null);
    }

    public DummyLexer(char quoteChar) {
        this(null, null, null, null, null, quoteChar);
    }

    public DummyLexer(char delimiter, char quoteChar, char escape, char commentStart, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines) {
        super(new CSVFormat(), null);
        this.delimiter = delimiter;
        this.quoteChar = quoteChar;
        this.escape = escape;
        this.commmentStart = commentStart;
        this.ignoreSurroundingSpaces = ignoreSurroundingSpaces;
        this.ignoreEmptyLines = ignoreEmptyLines;
    }

    @Override
    abstract Token nextToken(Token reusableToken) throws IOException;
}