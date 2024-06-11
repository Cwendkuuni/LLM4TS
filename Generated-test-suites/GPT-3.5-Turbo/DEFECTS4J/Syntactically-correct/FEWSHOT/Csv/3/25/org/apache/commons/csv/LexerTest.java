package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    private CSVFormat format;

    @Before
    public void setUp() {
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');
        when(format.getQuoteChar()).thenReturn('"');
        when(format.getCommentStart()).thenReturn('#');
        when(format.getIgnoreSurroundingSpaces()).thenReturn(false);
        when(format.getIgnoreEmptyLines()).thenReturn(false);
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.getLineNumber()).thenReturn(5L);

        assertEquals(5L, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_backslash() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('r');

        int result = lexer.readEscape();

        assertEquals(Util.stripLeadingAndTrailingQuotes("r"), result);
    }

    @Test
    public void testReadEscape_newLine() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('n');

        int result = lexer.readEscape();

        assertEquals(Util.stripLeadingAndTrailingQuotes("n"), result);
    }

    @Test
    public void testReadEscape_tab() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('t');

        int result = lexer.readEscape();

        assertEquals(Util.stripLeadingAndTrailingQuotes("t"), result);
    }

    @Test
    public void testReadEscape_backspace() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('b');

        int result = lexer.readEscape();

        assertEquals(Util.stripLeadingAndTrailingQuotes("b"), result);
    }

    @Test
    public void testReadEscape_formFeed() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('f');

        int result = lexer.readEscape();

        assertEquals(Util.stripLeadingAndTrailingQuotes("f"), result);
    }

    @Test
    public void testReadEscape_unexpectedChar() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn('x');

        int result = lexer.readEscape();

        assertEquals('x', result);
    }

    @Test(expected = IOException.class)
    public void testReadEscape_endOfStream() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn(-1);

        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("foo  ");

        Lexer lexer = new Lexer(format, null);
        lexer.trimTrailingSpaces(buffer);

        assertEquals("foo", buffer.toString());
    }

    @Test
    public void testReadEndOfLine_LF() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.read()).thenReturn(LF);

        boolean result = lexer.readEndOfLine(LF);

        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CR_LF() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.lookAhead()).thenReturn(LF);

        boolean result = lexer.readEndOfLine(CR);

        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_CR_noLF() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);
        when(in.lookAhead()).thenReturn(-1);

        boolean result = lexer.readEndOfLine(CR);

        assertTrue(result);
    }

    @Test
    public void testReadEndOfLine_notLineTerminator() throws IOException {
        ExtendedBufferedReader in = mock(ExtendedBufferedReader.class);
        Lexer lexer = new Lexer(format, in);

        boolean result = lexer.readEndOfLine('a');

        assertFalse(result);
    }

    @Test
    public void testIsWhitespace_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isWhitespace(' ');

        assertTrue(result);
    }

    @Test
    public void testIsWhitespace_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isWhitespace('a');

        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isStartOfLine('\n');

        assertTrue(result);
    }

    @Test
    public void testIsStartOfLine_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isStartOfLine('a');

        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isEndOfFile(-1);

        assertTrue(result);
    }

    @Test
    public void testIsEndOfFile_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isEndOfFile('a');

        assertFalse(result);
    }

    @Test
    public void testIsDelimiter_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isDelimiter(',');

        assertTrue(result);
    }

    @Test
    public void testIsDelimiter_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isDelimiter('a');

        assertFalse(result);
    }

    @Test
    public void testIsEscape_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isEscape('\\');

        assertTrue(result);
    }

    @Test
    public void testIsEscape_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isEscape('a');

        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isQuoteChar('"');

        assertTrue(result);
    }

    @Test
    public void testIsQuoteChar_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isQuoteChar('a');

        assertFalse(result);
    }

    @Test
    public void testIsCommentStart_true() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isCommentStart('#');

        assertTrue(result);
    }

    @Test
    public void testIsCommentStart_false() {
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isCommentStart('a');

        assertFalse(result);
    }
}