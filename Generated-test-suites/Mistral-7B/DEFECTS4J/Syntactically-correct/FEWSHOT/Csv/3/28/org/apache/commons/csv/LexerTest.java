package org.apache.commons.csv;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;

public class LexerTest {

    @Test
    public void testLexerBasic() throws IOException {
        final String test = "foo,bar\nqux,quux";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT;
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertEquals("foo", nextToken.getString());
        assertEquals(0, nextToken.getStartPosition());
        assertEquals(3, nextToken.getEndPosition());
        assertEquals(',', nextToken.getType());

        assertEquals("bar", lexer.nextToken(token).getString());
        assertEquals(4, token.getStartPosition());
        assertEquals(6, token.getEndPosition());
        assertEquals(LF, lexer.nextToken(token).getType());

        assertEquals("qux", lexer.nextToken(token).getString());
        assertEquals(7, token.getStartPosition());
        assertEquals(10, token.getEndPosition());
        assertEquals(',', lexer.nextToken(token).getType());

        assertEquals("quux", lexer.nextToken(token).getString());
        assertEquals(-1, token.getEndPosition()); // EOF
    }

    @Test
    public void testLexerEmpty() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream("".getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT;
        final Lexer lexer = new Lexer(format, reader);
        final Token token = new Token();

        assertFalse(lexer.isEndOfFile(0));
        assertEquals(0, lexer.getLineNumber());

        assertEquals(UNDEFINED, lexer.nextToken(token).getType());
        assertEquals(-1, token.getEndPosition()); // EOF
    }

    @Test
    public void testLexerComment() throws IOException {
        final String test = "# This is a comment\nfoo,bar\nqux,quux";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT.withCommentStart("#");
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertTrue(nextToken.isComment());
        assertEquals(0, token.getStartPosition());
        assertEquals(14, token.getEndPosition());

        assertEquals("foo", lexer.nextToken(token).getString());
        assertEquals(15, token.getStartPosition());
        assertEquals(17, token.getEndPosition());
        assertEquals(',', lexer.nextToken(token).getType());

        assertEquals("bar", lexer.nextToken(token).getString());
        assertEquals(18, token.getStartPosition());
        assertEquals(21, token.getEndPosition());
        assertEquals(LF, lexer.nextToken(token).getType());

        assertEquals("qux", lexer.nextToken(token).getString());
        assertEquals(22, token.getStartPosition());
        assertEquals(25, token.getEndPosition());
        assertEquals(',', lexer.nextToken(token).getType());

        assertEquals("quux", lexer.nextToken(token).getString());
        assertEquals(-1, token.getEndPosition()); // EOF
    }

    @Test
    public void testLexerQuoted() throws IOException {
        final String test = "\"foo,bar\",baz\n\"quoted,\"quoted\",qux\"";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT;
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertEquals("foo,bar", nextToken.getString());
        assertEquals(0, token.getStartPosition());
        assertEquals(10, token.getEndPosition());
        assertEquals(',', nextToken.getType());

        assertEquals("baz", lexer.nextToken(token).getString());
        assertEquals(11, token.getStartPosition());
        assertEquals(12, token.getEndPosition());
        assertEquals(LF, lexer.nextToken(token).getType());

        final Token quotedToken = new Token();
        final Token quotedNextToken = lexer.nextToken(quotedToken);

        assertEquals("quoted", quotedNextToken.getString());
        assertEquals(13, quotedToken.getStartPosition());
        assertEquals(17, quotedToken.getEndPosition());
        assertEquals(',', lexer.nextToken(quotedToken).getType());

        assertEquals("quoted", lexer.nextToken(quotedToken).getString());
        assertEquals(18, quotedToken.getStartPosition());
        assertEquals(21, quotedToken.getEndPosition());
        assertEquals(',', lexer.nextToken(quotedToken).getType());

        final StringBuilder buffer = new StringBuilder();
        lexer.nextToken(token).accept(buffer);
        assertEquals("qux\"", buffer.toString());
    }

    @Test
    public void testLexerEscapes() throws IOException {
        final String test = "\"foo,\"bar,\"baz\\\\,\"qux\"";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT;
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertEquals("foo,\"", nextToken.getString());
        assertEquals(0, token.getStartPosition());
        assertEquals(7, token.getEndPosition());
        assertEquals(',', nextToken.getType());

        assertEquals("bar", lexer.nextToken(token).getString());
        assertEquals(8, token.getStartPosition());
        assertEquals(9, token.getEndPosition());
        assertEquals(',', lexer.nextToken(token).getType());

        final Token escapedToken = new Token();
        final Token escapedNextToken = lexer.nextToken(escapedToken);

        assertEquals("baz\\", escapedNextToken.getString());
        assertEquals(10, token.getStartPosition());
        assertEquals(12, token.getEndPosition());
        assertEquals(',', lexer.nextToken(escapedToken).getType());

        assertEquals("qux\"", lexer.nextToken(escapedToken).getString());
        assertEquals(-1, token.getEndPosition()); // EOF
    }

    @Test
    public void testLexerIgnoreSurroundingSpaces() throws IOException {
        final String test = " foo,\t bar\n   qux,\t quux";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertEquals("foo", nextToken.getString());
        assertEquals(0, token.getStartPosition());
        assertEquals(4, token.getEndPosition());
        assertEquals(',', nextToken.getType());

        assertEquals("bar", lexer.nextToken(token).getString());
        assertEquals(5, token.getStartPosition());
        assertEquals(7, token.getEndPosition());
        assertEquals(LF, lexer.nextToken(token).getType());

        assertEquals("qux", lexer.nextToken(token).getString());
        assertEquals(8, token.getStartPosition());
        assertEquals(10, token.getEndPosition());
        assertEquals(',', lexer.nextToken(token).getType());

        assertEquals("quux", lexer.nextToken(token).getString());
        assertEquals(-1, token.getEndPosition()); // EOF
    }

    @Test
    public void testLexerIgnoreEmptyLines() throws IOException {
        final String test = "\nfoo,\t bar\n\n   qux,\t quux";

        final InputStream inputStream = new ByteArrayInputStream(test.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        final CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        final Lexer lexer = new Lexer(format, reader);

        final Token token = new Token();
        final Token nextToken = lexer.nextToken(token);

        assertNull(nextToken);
        assertEquals(1, lexer.getLineNumber());

        final Token nextToken2 = lexer.nextToken(token);

        assertEquals("foo", nextToken2.getString());
        assertEquals(1, token.getStartPosition());
        assertEquals(4, token.getEndPosition());
        assertEquals(',', nextToken2.getType());

        assertEquals("bar", lexer.nextToken(token).getString());
        assertEquals(5, token.getStartPosition());
        assertEquals(7, token.getEndPosition());
        assertEquals(LF, lexer.nextToken(token).getType());

        final Token nextToken3 = lexer.nextToken(token);

        assertNull(nextToken3);
        assertEquals(2, lexer.getLineNumber());

        final Token nextToken4 = lexer.nextToken(token);

        assertEquals("qux", nextToken4.getString());
        assertEquals(1, token.getStartPosition());
        assertEquals(4, token.getEndPosition());
        assertEquals(',', nextToken4.getType());

        assertEquals("quux", lexer.nextToken(token).getString());
        assertEquals(-1, token.getEndPosition()); // EOF
    }
}