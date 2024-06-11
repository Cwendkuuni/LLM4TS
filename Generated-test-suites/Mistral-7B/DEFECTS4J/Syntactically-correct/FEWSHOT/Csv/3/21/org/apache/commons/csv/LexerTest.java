package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.DISABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(true, lexer.isStartOfLine(UNDEFINED));
        assertEquals(true, lexer.isStartOfLine(CR));
        assertEquals(true, lexer.isStartOfLine(LF));
        assertEquals(true, lexer.isStartOfLine(-1)); // end of stream is considered start of a line

        String testString = "foo\r\nbar\n";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(testString));
        assertEquals(true, lexer.isStartOfLine(UNDEFINED));
        assertEquals(true, lexer.isStartOfLine(CR));
        assertEquals(true, lexer.isStartOfLine(LF));
        assertEquals(true, lexer.isStartOfLine(testString.charAt(testString.length() - 2)));
        assertEquals(false, lexer.isStartOfLine(testString.charAt(testString.length() - 1)));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(false, lexer.isEndOfFile(UNDEFINED));
        assertEquals(false, lexer.isEndOfFile(CR));
        assertEquals(false, lexer.isEndOfFile(LF));
        assertEquals(true, lexer.isEndOfFile(-1));

        String testString = "foo\r\nbar\n";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(testString));
        assertEquals(false, lexer.isEndOfFile(UNDEFINED));
        assertEquals(false, lexer.isEndOfFile(CR));
        assertEquals(false, lexer.isEndOfFile(LF));
        assertEquals(false, lexer.isEndOfFile(testString.charAt(testString.length() - 2)));
        assertEquals(true, lexer.isEndOfFile(testString.charAt(testString.length() - 1)));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(false, lexer.isDelimiter(UNDEFINED));
        assertEquals(false, lexer.isDelimiter(CR));
        assertEquals(false, lexer.isDelimiter(LF));
        assertEquals(false, lexer.isDelimiter(TAB));
        assertEquals(false, lexer.isDelimiter(BACKSPACE));
        assertEquals(false, lexer.isDelimiter(FF));
        assertEquals(false, lexer.isDelimiter('x'));

        String delimiter = ",";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT.withDelimiter(delimiter.charAt(0)), new StringReader(""));
        assertEquals(false, lexer.isDelimiter(UNDEFINED));
        assertEquals(false, lexer.isDelimiter(CR));
        assertEquals(false, lexer.isDelimiter(LF));
        assertEquals(false, lexer.isDelimiter(TAB));
        assertEquals(false, lexer.isDelimiter(BACKSPACE));
        assertEquals(false, lexer.isDelimiter(FF));
        assertEquals(true, lexer.isDelimiter(','));
        assertEquals(true, lexer.isDelimiter(delimiter.charAt(0)));
    }

    @Test
    public void testIsEscape() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(false, lexer.isEscape(UNDEFINED));
        assertEquals(false, lexer.isEscape(CR));
        assertEquals(false, lexer.isEscape(LF));
        assertEquals(false, lexer.isEscape(TAB));
        assertEquals(false, lexer.isEscape(BACKSPACE));
        assertEquals(false, lexer.isEscape(FF));
        assertEquals(false, lexer.isEscape('x'));

        String escape = "\\";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT.withEscape(escape.charAt(0)), new StringReader(""));
        assertEquals(false, lexer.isEscape(UNDEFINED));
        assertEquals(false, lexer.isEscape(CR));
        assertEquals(false, lexer.isEscape(LF));
        assertEquals(false, lexer.isEscape(TAB));
        assertEquals(false, lexer.isEscape(BACKSPACE));
        assertEquals(false, lexer.isEscape(FF));
        assertEquals(true, lexer.isEscape(escape.charAt(0)));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(false, lexer.isQuoteChar(UNDEFINED));
        assertEquals(false, lexer.isQuoteChar(CR));
        assertEquals(false, lexer.isQuoteChar(LF));
        assertEquals(false, lexer.isQuoteChar(TAB));
        assertEquals(false, lexer.isQuoteChar(BACKSPACE));
        assertEquals(false, lexer.isQuoteChar(FF));
        assertEquals(false, lexer.isQuoteChar('x'));

        String quoteChar = "\"";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT.withQuoteChar(quoteChar.charAt(0)), new StringReader(""));
        assertEquals(false, lexer.isQuoteChar(UNDEFINED));
        assertEquals(false, lexer.isQuoteChar(CR));
        assertEquals(false, lexer.isQuoteChar(LF));
        assertEquals(false, lexer.isQuoteChar(TAB));
        assertEquals(false, lexer.isQuoteChar(BACKSPACE));
        assertEquals(false, lexer.isQuoteChar(FF));
        assertEquals(true, lexer.isQuoteChar('"'));
        assertEquals(true, lexer.isQuoteChar(quoteChar.charAt(0)));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(false, lexer.isCommentStart(UNDEFINED));
        assertEquals(false, lexer.isCommentStart(CR));
        assertEquals(false, lexer.isCommentStart(LF));
        assertEquals(false, lexer.isCommentStart(TAB));
        assertEquals(false, lexer.isCommentStart(BACKSPACE));
        assertEquals(false, lexer.isCommentStart(FF));
        assertEquals(false, lexer.isCommentStart('x'));

        String commentStart = "#";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT.withCommentStart(commentStart.charAt(0)), new StringReader(""));
        assertEquals(false, lexer.isCommentStart(UNDEFINED));
        assertEquals(false, lexer.isCommentStart(CR));
        assertEquals(false, lexer.isCommentStart(LF));
        assertEquals(false, lexer.isCommentStart(TAB));
        assertEquals(false, lexer.isCommentStart(BACKSPACE));
        assertEquals(false, lexer.isCommentStart(FF));
        assertEquals(true, lexer.isCommentStart('#'));
        assertEquals(true, lexer.isCommentStart(commentStart.charAt(0)));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertFalse(lexer.readEndOfLine(UNDEFINED));
        assertFalse(lexer.readEndOfLine(CR));
        assertFalse(lexer.readEndOfLine(LF));
        assertFalse(lexer.readEndOfLine(TAB));
        assertFalse(lexer.readEndOfLine(BACKSPACE));
        assertFalse(lexer.readEndOfLine(FF));
        assertFalse(lexer.readEndOfLine('x'));
        assertFalse(lexer.readEndOfLine(-1));

        String testString = "foo\r\nbar\n";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(testString));
        assertFalse(lexer.readEndOfLine(UNDEFINED));
        assertFalse(lexer.readEndOfLine(CR));
        assertFalse(lexer.readEndOfLine(LF));
        assertFalse(lexer.readEndOfLine(TAB));
        assertFalse(lexer.readEndOfLine(BACKSPACE));
        assertFalse(lexer.readEndOfLine(FF));
        assertFalse(lexer.readEndOfLine('x'));
        assertTrue(lexer.readEndOfLine(-1));
        assertTrue(lexer.readEndOfLine(testString.charAt(testString.length() - 2)));
        assertTrue(lexer.readEndOfLine(testString.charAt(testString.length() - 1)));
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder builder = new StringBuilder();
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader("foo bar\t baz  "));
        lexer.nextToken(null);
        builder.append(lexer.getLastTokenValue());
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.trimTrailingSpaces(builder);
        assertEquals("foo bar\tbaz", builder.toString());

        builder.setLength(0);
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader("foo bar\t baz  "));
        lexer.nextToken(null);
        builder.append(lexer.getLastTokenValue());
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.nextToken(null);
        lexer.trimTrailingSpaces(builder);
        assertEquals("foo bar\tbaz", builder.toString());

        builder.setLength(0);
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader("foo bar\t baz  "));
        lexer.nextToken(null);
        lexer.trimTrailingSpaces(builder);
        assertEquals("foo bar\tbaz", builder.toString());
    }

    @Test
    public void testIsWhitespace() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertFalse(lexer.isWhitespace(UNDEFINED));
        assertFalse(lexer.isWhitespace(CR));
        assertFalse(lexer.isWhitespace(LF));
        assertFalse(lexer.isWhitespace(TAB));
        assertFalse(lexer.isWhitespace(BACKSPACE));
        assertFalse(lexer.isWhitespace(FF));
        assertFalse(lexer.isWhitespace('x'));

        String testString = " foo\t bar\n baz ";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(testString));
        assertFalse(lexer.isWhitespace(UNDEFINED));
        assertFalse(lexer.isWhitespace(CR));
        assertFalse(lexer.isWhitespace(LF));
        assertFalse(lexer.isWhitespace(TAB));
        assertFalse(lexer.isWhitespace(BACKSPACE));
        assertFalse(lexer.isWhitespace(FF));
        assertFalse(lexer.isWhitespace('x'));
        assertFalse(lexer.isWhitespace(testString.charAt(0)));
        assertFalse(lexer.isWhitespace(testString.charAt(1)));
        assertFalse(lexer.isWhitespace(testString.charAt(testString.length() - 1)));
        assertFalse(lexer.isWhitespace(testString.charAt(testString.length() - 2)));

        for (int i = 2; i < testString.length(); i++) {
            char c = testString.charAt(i);
            assertTrue(lexer.isWhitespace(c));
        }
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        assertEquals(UNDEFINED, lexer.readEscape());

        String testString = "foo\\r\\nbar\\t\\baz\\n\\quux";
        lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(testString));
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals('u', lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertEquals(END_OF_STREAM, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidChar() throws IOException {
        Lexer lexer = new DelimitedFileLexer(CSVFormat.DEFAULT, new StringReader(""));
        lexer.readEscape(); // consume initial backslash
        lexer.readEscape(); // consume second backslash
        lexer.readEscape(); // consume third invalid char
    }
}