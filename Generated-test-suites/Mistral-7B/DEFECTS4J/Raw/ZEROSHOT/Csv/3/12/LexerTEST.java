import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LexerTest {

    private ExtendedBufferedReader input;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        String testString = "a,b,c\r\n" +
                "d,\"e,f\",g\r\n" +
                "h,i,j\n"; // No trailing whitespace
        ByteArrayInputStream testIS = new ByteArrayInputStream(testString.getBytes());
        format = new CSVFormatBuilder()
                .setDelimiter(',')
                .setHeaderExcluded(true)
                .build();
        input = new ExtendedBufferedReader(new InputStreamReader(testIS));
    }

    @After
    public void tearDown() throws IOException {
        input.close();
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.getLastChar();
        boolean result = lexer.isStartOfLine(c);
        assertThat(result, is(true));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        boolean result = lexer.isEndOfFile(c);
        assertThat(result, is(false));

        c = -1; // End of stream
        result = lexer.isEndOfFile(c);
        assertThat(result, is(true));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        boolean result = lexer.isDelimiter(c);
        assertThat(result, is(false));

        c = ',';
        result = lexer.isDelimiter(c);
        assertThat(result, is(true));
    }

    @Test
    public void testIsEscape() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.escape;
        boolean result = lexer.isEscape(c);
        assertThat(result, is(true));

        c = ',';
        result = lexer.isEscape(c);
        assertThat(result, is(false));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.quoteChar;
        boolean result = lexer.isQuoteChar(c);
        assertThat(result, is(true));

        c = ',';
        result = lexer.isQuoteChar(c);
        assertThat(result, is(false));
    }

    @Test
    public void testIsWhitespace() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        boolean result = lexer.isWhitespace(c);
        assertThat(result, is(true));

        c = ',';
        result = lexer.isWhitespace(c);
        assertThat(result, is(false));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        boolean result = lexer.readEndOfLine(c);
        assertThat(result, is(false));

        // CR
        c = lexer.read();
        result = lexer.readEndOfLine(c);
        assertThat(result, is(true));

        // CR + LF
        c = lexer.read();
        result = lexer.readEndOfLine(c);
        assertThat(result, is(true));
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        int escapeChar = lexer.readEscape();
        assertThat(escapeChar, is(CR));

        // Check for meta-characters
        c = lexer.read();
        escapeChar = lexer.readEscape();
        assertThat(escapeChar, is(isWhitespace(c) ? TAB : '\\'));
    }

    @Test(expected = IOException.class)
    public void testReadEscapeAtEndOfStream() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        int c = lexer.read();
        int escapeChar = lexer.readEscape();
        assertThat(escapeChar, is(CR));

        c = -1; // End of stream
        escapeChar = lexer.readEscape();
        throw new AssertionError();
    }

    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = new LexerTestLexer(format, input);
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.START_LINE));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.DATA));
        assertThat(nextToken.getValue(), is("a"));

        // Quoted string
        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.START_QUOTE));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.DATA));
        assertThat(nextToken.getValue(), is("e,f"));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.END_QUOTE));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.DATA));
        assertThat(nextToken.getValue(), is("g"));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.END_OF_RECORD));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.START_LINE));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        // Empty line
        if (lexer.ignoreEmptyLines) {
            nextToken = lexer.nextToken(token);
        }

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.END_OF_RECORD));
        assertThat(nextToken.getValue().length(), is(0));

        token.clear();
        nextToken = lexer.nextToken(token);

        assertThat(nextToken.getTokenType(), is(Token.TokenType.END_OF_STREAM));
        assertThat(nextToken.getValue().length(), is(0));
    }

    private int isWhitespace(int c) {
        return Character.isWhitespace((char) c);
    }

    private static class LexerTestLexer extends Lexer {
        public LexerTestLexer(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        protected abstract Token nextToken(Token reusableToken) throws IOException;
    }
}