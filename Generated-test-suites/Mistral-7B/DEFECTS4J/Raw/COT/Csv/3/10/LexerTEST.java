import org.apache.commons.csv.Constants;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for the Lexer class
 */
public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader mockInput;

    @Before
    public void setup() {
        String testData = "field1,\"field2\"\n" + "field3\n" + "field4, field5";
        mockInput = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, mockInput);
    }

    /**
     * Test method for {@link Lexer#Lexer(CSVFormat, ExtendedBufferedReader)}
     */
    @Test
    public void testLexerConstructor() {
        Assert.assertNotNull(lexer);
    }

    /**
     * Test method for {@link Lexer#getLineNumber()}
     */
    @Test
    public void testGetLineNumber() throws IOException {
        Assert.assertEquals(1, lexer.getLineNumber());
    }

    /**
     * Test method for {@link Lexer#readEscape()}
     */
    @Test
    public void testReadEscape() throws IOException {
        Assert.assertEquals(CR, lexer.readEscape());
        Assert.assertEquals(LF, lexer.readEscape());
        Assert.assertEquals(TAB, lexer.readEscape());
        Assert.assertEquals(BACKSPACE, lexer.readEscape());
        Assert.assertEquals(FF, lexer.readEscape());
        // Check invalid escape character
        Assert.assertEquals(END_OF_STREAM, lexer.readEscape());
    }

    /**
     * Test method for {@link Lexer#nextToken(Token)}
     */
    @Test
    public void testNextToken() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);

        Assert.assertEquals("field1", token.getValue());
        Assert.assertEquals(TokenType.STRING, token.getType());

        token.reset();
        nextToken = lexer.nextToken(token);

        Assert.assertEquals("field2", token.getValue());
        Assert.assertEquals(TokenType.STRING, token.getType());

        // Test reading whitespace and line terminators
        token.reset();
        nextToken = lexer.nextToken(token);
        Assert.assertEquals("field3", token.getValue());
        Assert.assertEquals(TokenType.STRING, token.getType());

        Assert.assertTrue(lexer.readEndOfLine(mockInput.read()));
        token.reset();
        nextToken = lexer.nextToken(token);
        Assert.assertEquals("field4", token.getValue());
        Assert.assertEquals(TokenType.STRING, token.getType());

        Assert.assertTrue(lexer.isStartOfLine(mockInput.read()));
        Assert.assertTrue(lexer.isEndOfFile(mockInput.read()));
    }

    /**
     * Test method for {@link Lexer#isWhitespace(int)}
     */
    @Test
    public void testIsWhitespace() {
        Assert.assertTrue(lexer.isWhitespace((int) ' '));
        Assert.assertTrue(lexer.isWhitespace((int) '\t'));
        Assert.assertTrue(lexer.isWhitespace((int) '\r'));
        Assert.assertTrue(lexer.isWhitespace((int) '\n'));
        Assert.assertFalse(lexer.isWhitespace((int) ','));
        Assert.assertFalse(lexer.isWhitespace((int) 'a'));
    }

    /**
     * Test method for {@link Lexer#isStartOfLine(int)}
     */
    @Test
    public void testIsStartOfLine() {
        Assert.assertTrue(lexer.isStartOfLine((int) '\r'));
        Assert.assertTrue(lexer.isStartOfLine((int) '\n'));
        Assert.assertTrue(lexer.isStartOfLine((int) '\u0000'));
        Assert.assertFalse(lexer.isStartOfLine((int) ' '));
        Assert.assertFalse(lexer.isStartOfLine((int) ','));
    }

    /**
     * Test method for {@link Lexer#isEndOfFile(int)}
     */
    @Test
    public void testIsEndOfFile() {
        Assert.assertFalse(lexer.isEndOfFile((int) Constants.LF));
        Assert.assertFalse(lexer.isEndOfFile((int) Constants.CR));
        Assert.assertFalse(lexer.isEndOfFile((int) ','));
        Assert.assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));
    }

    /**
     * Test method for {@link Lexer#isDelimiter(int)}
     */
    @Test
    public void testIsDelimiter() {
        Assert.assertTrue(lexer.isDelimiter((int) lexer.delimiter));
        Assert.assertFalse(lexer.isDelimiter((int) ','));
    }

    /**
     * Test method for {@link Lexer#isEscape(int)}
     */
    @Test
    public void testIsEscape() {
        Assert.assertFalse(lexer.isEscape((int) Constants.LF));
        Assert.assertFalse(lexer.isEscape((int) Constants.CR));
        Assert.assertFalse(lexer.isEscape((int) '\\'));
        Assert.assertFalse(lexer.isEscape((int) '\u0000'));
        Assert.assertTrue(lexer.isEscape((int) lexer.escape));
    }

    /**
     * Test method for {@link Lexer#isQuoteChar(int)}
     */
    @Test
    public void testIsQuoteChar() {
        Assert.assertFalse(lexer.isQuoteChar((int) Constants.LF));
        Assert.assertFalse(lexer.isQuoteChar((int) Constants.CR));
        Assert.assertFalse(lexer.isQuoteChar((int) ','));
        Assert.assertFalse(lexer.isQuoteChar((int) '\\'));
        Assert.assertTrue(lexer.isQuoteChar((int) lexer.quoteChar));
    }

    /**
     * Test method for {@link Lexer#isCommentStart(int)}
     */
    @Test
    public void testIsCommentStart() {
        Assert.assertFalse(lexer.isCommentStart((int) Constants.LF));
        Assert.assertFalse(lexer.isCommentStart((int) Constants.CR));
        Assert.assertFalse(lexer.isCommentStart((int) ','));
        Assert.assertFalse(lexer.isCommentStart((int) '\\'));
        Assert.assertTrue(lexer.isCommentStart((int) lexer.commmentStart));
    }
}