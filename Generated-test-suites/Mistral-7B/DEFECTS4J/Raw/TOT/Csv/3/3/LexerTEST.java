import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.util.DateFormatUtil;

/**
 * Test class for {@link Lexer}.
 */
public class LexerTest {

    private static final char COMMENT_START = '#';
    private static final char DEFAULT_DELIMITER = ',';
    private static final char DEFAULT_ESCAPE = '\\';
    private static final char DEFAULT_QUOTE_CHAR = '"';

    @org.junit.Test
    public void testSimpleTokenizing() throws IOException {
        final String input = "col1,col2,col3";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(3, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("col2", tokens.get(1).getString());
        assertEquals("col3", tokens.get(2).getString());

        reader.close();
    }

    @org.junit.Test
    public void testEmptyLine() throws IOException {
        final String input = "\r\n";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        assertFalse(lexer.ignoreEmptyLines);
        assertFalse(lexer.nextToken(new Token()) instanceof Token);

        reader.close();
    }

    @org.junit.Test
    public void testWhitespaceHandling() throws IOException {
        final String input = "\r\n   col1,    col2, col3\r\n";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(3, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("col2", tokens.get(1).getString());
        assertEquals("col3", tokens.get(2).getString());

        lexer.trimTrailingSpaces(new StringBuilder());
        assertEquals(0, lexer.getLineNumber());
        reader.close();
    }

    @org.junit.Test
    public void testUnescapedSpecialCharacters() throws IOException {
        final String input = "col1,\r col2,\n col3,\f col4,\b col5,\t col6";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(6, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals(CR, tokens.get(1).getType());
        assertEquals("col2", tokens.get(2).getString());
        assertEquals(LF, tokens.get(3).getType());
        assertEquals("col3", tokens.get(4).getString());
        assertEquals(FF, tokens.get(5).getType());

        reader.close();
    }

    @org.junit.Test
    public void testEscapedSpecialCharacters() throws IOException {
        final String input = "col1,\"col2,\\\"col3,col4\\r,col5\\n,col6\\f,col7\\b,col8\\t\"";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setEscape(DEFAULT_ESCAPE)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(8, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("col2,\"col3,", tokens.get(1).getString());
        assertEquals(Token.Type.STRING, tokens.get(1).getType());
        assertEquals("col4", tokens.get(2).getString());
        assertEquals(CR, tokens.get(3).getType());
        assertEquals("col5", tokens.get(4).getString());
        assertEquals(LF, tokens.get(5).getType());
        assertEquals("col6", tokens.get(6).getString());
        assertEquals(FF, tokens.get(7).getType());

        reader.close();
    }

    @org.junit.Test
    public void testCommentHandling() throws IOException {
        final String input = "col1,\r #Comments are ignored\n col2,\n  # Multiline comments are ignored,\r # until EOL.\r";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setCommentStart(COMMENT_START)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(2, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("col2", tokens.get(1).getString());

        reader.close();
    }

    @org.junit.Test
    public void testMalformedInput() throws IOException {
        final String input = "col1,\r col2,\n col3,\f col4,\b col5,\t col6\"col7,\r";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        assertFalse(lexer.nextToken(new Token()) instanceof Token);
        assertEquals(1, lexer.getLineNumber());

        reader.close();
    }

    @org.junit.Test
    public void testTokenizerWithCustomLineEnding() throws IOException {
        final String input = "col1\u0002col2\u0002col3\u0003col4\u0003col5";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(5, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals(UNDEFINED, tokens.get(1).getType());
        assertEquals("col2", tokens.get(2).getString());
        assertEquals(UNDEFINED, tokens.get(3).getType());
        assertEquals("col3", tokens.get(4).getString());

        reader.close();
    }

    @org.junit.Test
    public void testTokenizerWithCustomCommentStart() throws IOException {
        final String input = "col1,\r %Comment are ignored here\n col2,\n  % Multiline comments are ignored,% until EOL.\r";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setCommentStart('%')
                .setHeader(null)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(2, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("col2", tokens.get(1).getString());

        reader.close();
    }

    @org.junit.Test
    public void testDateParsing() throws IOException {
        final String input = "col1,\"2019-01-01\",\"2019-01-02T15:30:00Z\"";
        final CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(DEFAULT_DELIMITER)
                .setHeader(null)
                .setDateFormat(DateFormatUtil.DATE_FORMAT_ISO8601_DATE_HOUR_MINUTE_SECOND_ZONE_OFFSET)
                .build();

        final ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        final ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(is));
        final Lexer lexer = new Lexer(format, reader);
        final List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = lexer.nextToken(new Token())) != null) {
            tokens.add(token);
        }
        assertEquals(3, tokens.size());
        assertEquals("col1", tokens.get(0).getString());
        assertEquals("2019-01-01", tokens.get(1).getString());
        assertEquals("2019-01-02T15:30:00Z", tokens.get(2).getString());

        reader.close();
    }
}