package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.test.AbstractCsvTest;
import org.junit.Test;

/**
 * Unit tests for {@link Lexer} class.
 */
public class LexerTest extends AbstractCsvTest {

    @Test
    public void testLexer() throws IOException {
        String testString = "a,b,c\r\n" + // CRLF line ending
                "d,e,f\n" + // LF line ending
                "g,h,i"; // no line ending

        CSVFormat format = new CSVFormat(',', CSVParser.STANDARD_EXCEL_FORMAT);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testString.getBytes())));
        Lexer lexer = new Lexer(format, in);

        Token token = null;
        int lineNumber = lexer.getLineNumber();

        // Test nextToken method for 6 tokens
        for (int i = 0; i < 6; i++) {
            token = lexer.nextToken(token);
            assertEquals("Test token value is incorrect: " + i, getExpectedTokenValue(i), token.getValue());
            assertNotNull("Test token is null: " + i, token);
            switch (i) {
            case 0:
                assertEquals(1, token.getColumnNumber());
                break;
            case 1:
                assertEquals(2, token.getColumnNumber());
                break;
            case 2:
                assertEquals(3, token.getColumnNumber());
                break;
            case 3:
                assertEquals(1, token.getColumnNumber()); // test case insensitivity
                break;
            case 4:
                assertEquals(2, token.getColumnNumber()); // test case insensitivity
                break;
            case 5:
                assertEquals(3, token.getColumnNumber()); // test case insensitivity
                break;
            }
            assertEquals(lineNumber, lexer.getLineNumber());
        }

        // Test end of stream
        token = lexer.nextToken(token);
        assertNull("Test nextToken returned a non-null token at end of stream", token);
        assertEquals(-1, lexer.getLineNumber());

        // Test isWhitespace method
        for (char c = ' '; c <= '~'; c++) {
            boolean result = lexer.isWhitespace((int) c);
            assertTrue("Test isWhitespace for character: " + c, result);
        }

        // Test isStartOfLine method
        assertEquals(true, lexer.isStartOfLine(UNDEFINED));
        assertEquals(true, lexer.isStartOfLine(CR));
        assertEquals(true, lexer.isStartOfLine(LF));
        assertEquals(false, lexer.isStartOfLine('a'));

        // Test isEndOfFile method
        assertEquals(true, lexer.isEndOfFile(END_OF_STREAM));
        assertEquals(true, lexer.isEndOfFile(UNDEFINED));
        assertEquals(false, lexer.isEndOfFile(CR));

        // Test isDelimiter method
        assertEquals(true, lexer.isDelimiter(','));
        assertEquals(false, lexer.isDelimiter('a'));

        // Test isEscape method
        assertEquals(false, lexer.isEscape(CR));
        assertEquals(false, lexer.isEscape(LF));
        assertEquals(false, lexer.isEscape('a'));
        assertEquals(true, lexer.isEscape(format.getEscape()));

        // Test isQuoteChar method
        assertEquals(false, lexer.isQuoteChar('a'));
        assertEquals(true, lexer.isQuoteChar(format.getQuoteChar()));

        // Test isCommentStart method
        assertEquals(false, lexer.isCommentStart('a'));
        assertEquals(true, lexer.isCommentStart(format.getCommentStart()));

        // Test trimTrailingSpaces method
        StringBuilder sb = new StringBuilder();
        String testString2 = " a\tb\tc ";
        ExtendedBufferedReader in2 = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testString2.getBytes())));
        Lexer lexer2 = new Lexer(format, in2);
        lexer2.nextToken(null); // consume first token
        lexer2.nextToken(null);
        lexer2.nextToken(null);
        lexer2.trimTrailingSpaces(sb);
        assertEquals("Test trimTrailingSpaces result is incorrect: ", "", sb.toString());

        // Test readEndOfLine method
        int c = in2.read();
        assertTrue("Test readEndOfLine for character: " + c, lexer2.readEndOfLine(c));
        c = in2.read();
        assertFalse("Test readEndOfLine for character: " + c, lexer2.readEndOfLine(c));

        // Test nextToken method with reusable token
        Token tokenReusable = new Token();
        for (int i = 0; i < 6; i++) {
            token = lexer.nextToken(tokenReusable);
            assertEquals("Test token value is incorrect: " + i, getExpectedTokenValue(i), token.getValue());
            assertNotNull("Test token is null: " + i, token);
            assertEquals(lineNumber, lexer.getLineNumber());
        }

        // Test nextToken method with null token
        for (int i = 0; i < 6; i++) {
            token = lexer.nextToken(null);
            assertEquals("Test token value is incorrect: " + i, getExpectedTokenValue(i), token.getValue());
            assertNotNull("Test token is null: " + i, token);
            switch (i) {
            case 0:
                assertEquals(1, token.getColumnNumber());
                break;
            case 1:
                assertEquals(2, token.getColumnNumber());
                break;
            case 2:
                assertEquals(3, token.getColumnNumber());
                break;
            case 3:
                assertEquals(1, token.getColumnNumber()); // test case insensitivity
                break;
            case 4:
                assertEquals(2, token.getColumnNumber()); // test case insensitivity
                break;
            case 5:
                assertEquals(3, token.getColumnNumber()); // test case insensitivity
                break;
            }
            assertEquals(lineNumber, lexer.getLineNumber());
        }

        // Test end of stream with reusable token
        token = lexer.nextToken(tokenReusable);
        assertNull("Test nextToken returned a non-null token at end of stream", token);
        assertEquals(-1, lexer.getLineNumber());

        // Test readEscape method
        lexer.in.mark(1);
        assertEquals(CR, lexer.readEscape());
        assertEquals(CR, lexer.in.read());
        assertEquals(LF, lexer.readEscape());
        assertEquals(LF, lexer.in.read());
        lexer.in.reset();

        // Test readEscape method with invalid escape sequence
        lexer.in.mark(1);
        try {
            lexer.readEscape();
            fail("Test readEscape with invalid escape sequence did not throw IOException");
        } catch (IOException e) {
            // expected
        }
        lexer.in.reset();

        // Test readEscape method with end of stream
        lexer.in.mark(1);
        try {
            lexer.readEscape();
            fail("Test readEscape with end of stream did not throw IOException");
        } catch (IOException e) {
            // expected
        }
        lexer.in.reset();
    }

    private String getExpectedTokenValue(int index) {
        switch (index) {
        case 0:
            return "a";
        case 1:
            return "b";
        case 2:
            return "c";
        case 3:
            return "d";
        case 4:
            return "e";
        case 5:
            return "g";
        default:
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
    }
}