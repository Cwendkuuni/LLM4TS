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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.junit.Test;

/**
 * Test cases for {@link Lexer} class.
 */
public class LexerTest {

    @Test
    public void testMapNullToDisabled() {
        assertEquals(' ', Lexer.mapNullToDisabled((Character) null));
        assertEquals('a', Lexer.mapNullToDisabled('a'));
    }

    @Test
    public void testLexerBasic() throws IOException {
        CSVFormat format = new CSVFormat().withDelimiter(',').withHeader();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo,bar,baz"));
        Lexer lexer = new Lexer(format, reader);

        StringBuilder buffer = new StringBuilder();

        Token token = null;
        Token lastToken = null;

        for (int i = 0; i < 10; i++) {
            token = lexer.nextToken(token);

            switch (token.getType()) {
            case Token.TYPE_FIELD:
                buffer.append(token.getValue());
                buffer.append(", ");
                break;
            case Token.TYPE_START_ARRAY:
                buffer.append("[");
                break;
            case Token.TYPE_END_ARRAY:
                buffer.append("]");
                break;
            case Token.TYPE_START_OBJECT:
                buffer.append("{");
                break;
            case Token.TYPE_END_OBJECT:
                buffer.append("}");
                break;
            case Token.TYPE_COMMENT:
                // ignore comments
                break;
            case Token.TYPE_INVALID:
                // ignore invalid tokens
                break;
            default:
                throw new IllegalStateException("Unexpected token type: " + token.getType());
            }

            lastToken = token;
        }

        assertEquals("foo, bar, баз, , , , , , ", buffer.toString());

        assertEquals(11, lexer.getLineNumber());

        assertEquals(11, lastToken.getLineNumber());
        assertEquals(5, lastToken.getColumnNumber());

        assertFalse(lastToken.isQuoted());
        assertFalse(lastToken.isEscaped());
        assertFalse(lastToken.isComment());
        assertFalse(lastToken.isEndOfLine());
    }

    @Test
    public void testLexerWithEscapes() throws IOException {
        CSVFormat format = new CSVFormat().withDelimiter(',').withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true)
                .withEscapeCharacter('\\')
                .withQuoteCharacter('"')
                .withCommentStart("--")
                .withDelimiterForQuotedFields('^')
                .withHeader();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo,\"bar,baz\",\"quoted\\^field\",--comment"));
        Lexer lexer = new Lexer(format, reader);

        StringBuilder buffer = new StringBuilder();

        Token token = null;
        Token lastToken = null;

        for (int i = 0; i < 10; i++) {
            token = lexer.nextToken(token);

            switch (token.getType()) {
            case Token.TYPE_FIELD:
                buffer.append(token.getValue());
                buffer.append(", ");
                break;
            case Token.TYPE_START_ARRAY:
                buffer.append("[");
                break;
            case Token.TYPE_END_ARRAY:
                buffer.append("]");
                break;
            case Token.TYPE_START_OBJECT:
                buffer.append("{");
                break;
            case Token.TYPE_END_OBJECT:
                buffer.append("}");
                break;
            case Token.TYPE_COMMENT:
                // ignore comments
                break;
            case Token.TYPE_INVALID:
                // ignore invalid tokens
                break;
            default:
                throw new IllegalStateException("Unexpected token type: " + token.getType());
            }

            lastToken = token;
        }

        assertEquals("foo,\"bar,baz\",\"quoted^field\", , , , , ", buffer.toString());

        assertEquals(1, lexer.getLineNumber());

        assertEquals(1, lastToken.getLineNumber());
        assertEquals(6, lastToken.getColumnNumber());

        assertTrue(lastToken.isQuoted());
        assertFalse(lastToken.isEscaped());
        assertFalse(lastToken.isComment());
        assertFalse(lastToken.isEndOfLine());

        // test escaped characters
        assertEquals("foo,\"bar,\"baz\",\"quoted^field\",--comment", lexer.in.readLine());
        assertEquals(7, lexer.getLineNumber());
        assertEquals(1, lexer.getColumnNumber());

        buffer.setLength(0);
        token = null;
        lastToken = null;

        for (int i = 0; i < 10; i++) {
            token = lexer.nextToken(token);

            switch (token.getType()) {
            case Token.TYPE_FIELD:
                buffer.append(token.getValue());
                buffer.append(", ");
                break;
            case Token.TYPE_START_ARRAY:
                buffer.append("[");
                break;
            case Token.TYPE_END_ARRAY:
                buffer.append("]");
                break;
            case Token.TYPE_START_OBJECT:
                buffer.append("{");
                break;
            case Token.TYPE_END_OBJECT:
                buffer.append("}");
                break;
            case Token.TYPE_COMMENT:
                // ignore comments
                break;
            case Token.TYPE_INVALID:
                // ignore invalid tokens
                break;
            default:
                throw new IllegalStateException("Unexpected token type: " + token.getType());
            }

            lastToken = token;
        }

        assertEquals("foo,\"bar,\"baz\",\"quoted^field\",--comment, , , , , ", buffer.toString());

        assertEquals(8, lexer.getLineNumber());

        assertEquals(8, lastToken.getLineNumber());
        assertEquals(6, lastToken.getColumnNumber());

        assertTrue(lastToken.isQuoted());
        assertTrue(lastToken.isEscaped());
        assertFalse(lastToken.isComment());
        assertFalse(lastToken.isEndOfLine());

        // test escaped characters
        assertEquals("foo,\"bar,\"baz\",\"quoted^field\",--comment, \\r, \\n, \\t, \\b, \\ff", lexer.in.readLine());
        assertEquals(9, lexer.getLineNumber());
        assertEquals(1, lexer.getColumnNumber());

        buffer.setLength(0);
        token = null;
        lastToken = null;

        for (int i = 0; i < 10; i++) {
            token = lexer.nextToken(token);

            switch (token.getType()) {
            case Token.TYPE_FIELD:
                buffer.append(token.getValue());
                buffer.append(", ");
                break;
            case Token.TYPE_START_ARRAY:
                buffer.append("[");
                break;
            case Token.TYPE_END_ARRAY:
                buffer.append("]");
                break;
            case Token.TYPE_START_OBJECT:
                buffer.append("{");
                break;
            case Token.TYPE_END_OBJECT:
                buffer.append("}");
                break;
            case Token.TYPE_COMMENT:
                // ignore comments
                break;
            case Token.TYPE_INVALID:
                // ignore invalid tokens
                break;
            default:
                throw new IllegalStateException("Unexpected token type: " + token.getType());
            }

            lastToken = token;
        }

        assertEquals("foo,\"bar,\"baz\",\"quoted^field\",--comment, \\r, \\n, \\t, \\b, \\ff, , , , , ", buffer.toString());

        assertEquals(10, lexer.getLineNumber());

        assertEquals(10, lastToken.getLineNumber());
        assertEquals(6, lastToken.getColumnNumber());

        assertTrue(lastToken.isQuoted());
        assertTrue(lastToken.isEscaped());
        assertFalse(lastToken.isComment());
        assertFalse(lastToken.isEndOfLine());

        reader.close();
    }

    @Test(expected = IOException.class)
    public void testLexerEndOfStream() throws IOException {
        CSVFormat format = new CSVFormat().withDelimiter(',').withHeader();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo,"));
        Lexer lexer = new Lexer(format, reader);

        StringBuilder buffer = new StringBuilder();

        Token token = null;

        for (int i = 0; i < 10; i++) {
            token = lexer.nextToken(token);

            switch (token.getType()) {
            case Token.TYPE_FIELD:
                buffer.append(token.getValue());
                buffer.append(", ");
                break;
            case Token.TYPE_START_ARRAY:
                buffer.append("[");
                break;
            case Token.TYPE_END_ARRAY:
                buffer.append("]");
                break;
            case Token.TYPE_START_OBJECT:
                buffer.append("{");
                break;
            case Token.TYPE_END_OBJECT:
                buffer.append("}");
                break;
            case Token.TYPE_COMMENT:
                // ignore comments
                break;
            case Token.TYPE_INVALID:
                // ignore invalid tokens
                break;
            default:
                throw new IllegalStateException("Unexpected token type: " + token.getType());
            }
        }

        reader.close();

        lexer.nextToken(null);

        // this should fail
        lexer.nextToken(null);
    }
}