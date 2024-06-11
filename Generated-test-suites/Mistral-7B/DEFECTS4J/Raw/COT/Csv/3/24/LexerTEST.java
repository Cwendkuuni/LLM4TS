import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for Lexer class
 */
public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader mockReader;

    @Before
    public void setup() {
        String testData = "column1,column2\n" +
                "value1,value2\n" +
                "value3,value4\n";

        CSVFormat format = new CSVFormat(',', '\0', '\0');
        mockReader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes()))) {
            @Override
            public boolean readEndOfLine(int c) throws IOException {
                lexer.getLineNumber(); // increment line number before checking for EOL
                return false;
            }
        };
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Assert.assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        Assert.assertEquals(LF, lexer.readEscape());
        Assert.assertEquals(UNDEFINED, lexer.readEscape()); // unescaped character
        Assert.assertEquals(CR, lexer.readEscape());
        Assert.assertEquals(BACKSPACE, lexer.readEscape());
        Assert.assertEquals(FF, lexer.readEscape());
        Assert.assertEquals(TAB, lexer.readEscape());
        Assert.assertEquals(UNDEFINED, lexer.readEscape()); // invalid escape sequence
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidInput() throws IOException {
        lexer.readEscape(); // read a valid character to set up the stream
        lexer.readEscape(); // read an invalid character
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder(" value1");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("value1", buffer.toString());

        buffer.append(" value2");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("value2", buffer.toString());
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = lexer.nextToken(null);
        Assert.assertEquals(TokenType.DELIMITER, token.getType());
        Assert.assertEquals(',', token.getValue().charAt(0));

        token = lexer.nextToken(token);
        Assert.assertEquals(TokenType.STRING, token.getType());
        Assert.assertEquals("value1", token.getValue());

        token = lexer.nextToken(token);
        Assert.assertEquals(TokenType.DELIMITER, token.getType());
        Assert.assertEquals(',', token.getValue().charAt(0));

        token = lexer.nextToken(token);
        Assert.assertEquals(TokenType.STRING, token.getType());
        Assert.assertEquals("value2", token.getValue());

        token = lexer.nextToken(token);
        Assert.assertEquals(TokenType.END_OF_LINE, token.getType());

        token = lexer.nextToken(token);
        Assert.assertEquals(TokenType.END_OF_FILE, token.getType());
    }

    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(lexer.isWhitespace(32));
        Assert.assertTrue(lexer.isWhitespace(13));
        Assert.assertFalse(lexer.isWhitespace(44));
    }

    @Test
    public void testIsStartOfLine() {
        Assert.assertTrue(lexer.isStartOfLine(Constants.LF));
        Assert.assertTrue(lexer.isStartOfLine(Constants.CR));
        Assert.assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
        Assert.assertFalse(lexer.isStartOfLine(44));
    }

    @Test
    public void testIsEndOfFile() {
        Assert.assertTrue(lexer.isEndOfFile(-1));
        Assert.assertFalse(lexer.isEndOfFile(32));
    }

    @Test
    public void testIsDelimiter() {
        Assert.assertTrue(lexer.isDelimiter(44));
        Assert.assertFalse(lexer.isDelimiter(32));
    }

    @Test
    public void testIsEscape() {
        Assert.assertFalse(lexer.isEscape(32));
        Assert.assertTrue(lexer.isEscape(92));
    }

    @Test
    public void testIsQuoteChar() {
        Assert.assertFalse(lexer.isQuoteChar(32));
        Assert.assertFalse(lexer.isQuoteChar(44));
        Assert.assertTrue(lexer.isQuoteChar(34));
    }

    @Test
    public void testIsCommentStart() {
        Assert.assertFalse(lexer.isCommentStart(32));
        Assert.assertFalse(lexer.isCommentStart(44));
        Assert.assertFalse(lexer.isCommentStart(34));
        Assert.assertFalse(lexer.isCommentStart(47));
        Assert.assertTrue(lexer.isCommentStart(45));
    }
}