import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.constants.Constants;
import org.apache.commons.csv.parser.Lexer;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for {@link Lexer}.
 */
public class LexerTest {

    @org.junit.Test
    public void testNewInstance() {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertNotNull(lexer);
        }
    }

    @org.junit.Test
    public void testGetLineNumber() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertEquals(1, lexer.getLineNumber());
            lexer.nextToken(null); // consume first line
            assertEquals(2, lexer.getLineNumber());
        }
    }

    @org.junit.Test
    public void testReadEscape() throws IOException {
        final String csv = "foo\\r\\nb\\t\\fbar\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, ',', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertEquals(CR, lexer.readEscape());
            assertEquals(LF, lexer.readEscape());
            assertEquals(TAB, lexer.readEscape());
            assertEquals(Constants.FF, lexer.readEscape());
        }
    }

    @org.junit.Test
    public void testIsWhitespace() {
        final Lexer lexer = new Lexer(null, null);
        assertFalse(lexer.isWhitespace(0));
        assertFalse(lexer.isWhitespace(1));
        assertFalse(lexer.isWhitespace(2));
        assertFalse(lexer.isWhitespace(3));
        assertFalse(lexer.isWhitespace(4));
        assertFalse(lexer.isWhitespace(5));
        assertFalse(lexer.isWhitespace(6));
        assertTrue(lexer.isWhitespace(7));
        assertTrue(lexer.isWhitespace(8));
        assertTrue(lexer.isWhitespace(9));
    }

    @org.junit.Test
    public void testIsStartOfLine() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isStartOfLine(0));
            assertFalse(lexer.isStartOfLine(1));
            assertFalse(lexer.isStartOfLine(2));
            assertFalse(lexer.isStartOfLine(3));
            assertFalse(lexer.isStartOfLine(4));
            assertFalse(lexer.isStartOfLine(5));
            assertFalse(lexer.isStartOfLine(6));
            assertTrue(lexer.isStartOfLine(7));
        }
    }

    @org.junit.Test
    public void testIsEndOfFile() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isEndOfFile(0));
            assertFalse(lexer.isEndOfFile(1));
            assertFalse(lexer.isEndOfFile(2));
            assertFalse(lexer.isEndOfFile(3));
            assertFalse(lexer.isEndOfFile(4));
            assertFalse(lexer.isEndOfFile(5));
            assertFalse(lexer.isEndOfFile(6));
            assertFalse(lexer.isEndOfFile(7));
            assertTrue(lexer.isEndOfFile(8));
        }
    }

    @org.junit.Test
    public void testIsDelimiter() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isDelimiter(0));
            assertFalse(lexer.isDelimiter(1));
            assertFalse(lexer.isDelimiter(2));
            assertFalse(lexer.isDelimiter(3));
            assertFalse(lexer.isDelimiter(4));
            assertFalse(lexer.isDelimiter(5));
            assertFalse(lexer.isDelimiter(6));
            assertTrue(lexer.isDelimiter(7));
        }
    }

    @org.junit.Test
    public void testIsEscape() throws IOException {
        final String csv = "foo\\r\\nb\\t\\fbar\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, ',', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isEscape(0));
            assertFalse(lexer.isEscape(1));
            assertFalse(lexer.isEscape(2));
            assertFalse(lexer.isEscape(3));
            assertFalse(lexer.isEscape(4));
            assertFalse(lexer.isEscape(5));
            assertFalse(lexer.isEscape(6));
            assertTrue(lexer.isEscape(7));
        }
    }

    @org.junit.Test
    public void testIsQuoteChar() throws IOException {
        final String csv = "foo,\"quoted\",bar\n" +
                "1,\"quoted\",2,3\n" +
                "\"quoted\",value,\"quoted\",quoted\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isQuoteChar(0));
            assertFalse(lexer.isQuoteChar(1));
            assertFalse(lexer.isQuoteChar(2));
            assertFalse(lexer.isQuoteChar(3));
            assertFalse(lexer.isQuoteChar(4));
            assertFalse(lexer.isQuoteChar(5));
            assertFalse(lexer.isQuoteChar(6));
            assertTrue(lexer.isQuoteChar(7));
            assertTrue(lexer.isQuoteChar(8));
            assertFalse(lexer.isQuoteChar(9));
            assertTrue(lexer.isQuoteChar(10));
            assertFalse(lexer.isQuoteChar(11));
            assertTrue(lexer.isQuoteChar(12));
            assertFalse(lexer.isQuoteChar(13));
            assertTrue(lexer.isQuoteChar(14));
            assertTrue(lexer.isQuoteChar(15));
        }
    }

    @org.junit.Test
    public void testIsCommentStart() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            assertFalse(lexer.isCommentStart(0));
            assertFalse(lexer.isCommentStart(1));
            assertFalse(lexer.isCommentStart(2));
            assertFalse(lexer.isCommentStart(3));
            assertFalse(lexer.isCommentStart(4));
            assertFalse(lexer.isCommentStart(5));
            assertFalse(lexer.isCommentStart(6));
            assertTrue(lexer.isCommentStart(7));
        }
    }

    @org.junit.Test
    public void testNextToken() throws IOException {
        final String csv = "foo,bar\n" +
                "1,2,3\n" +
                "\"quoted\",value\n" +
                "#comment\n" +
                "a,b,c\n";
        Token token = new Token();
        try (ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(csv))) {
            final CSVFormat format = new CSVFormat(null, '\t', ',', null);
            final Lexer lexer = new Lexer(format, reader);
            // consume first token
            lexer.nextToken(token);
            assertEquals("foo", token.getValue());
            token = new Token();
            // consume second token
            lexer.nextToken(token);
            assertEquals("1", token.getValue());
            token = new Token();
            // consume third token
            lexer.nextToken(token);
            assertEquals("quoted", token.getValue());
            token = new Token();
            // consume fourth token
            lexer.nextToken(token);
            assertEquals("value", token.getValue());
            token = new Token();
            // consume fifth token
            lexer.nextToken(token);
            assertEquals("#comment", token.getValue());
            token = new Token();
            // consume sixth token
            lexer.nextToken(token);
            assertEquals("a", token.getValue());
            token = new Token();
            // consume seventh token
            lexer.nextToken(token);
            assertEquals("b", token.getValue());
            token = new Token();
            // consume eighth token
            lexer.nextToken(token);
            assertEquals("c", token.getValue());
            // EOF
            assertNull(lexer.nextToken(token));
        }
    }
}