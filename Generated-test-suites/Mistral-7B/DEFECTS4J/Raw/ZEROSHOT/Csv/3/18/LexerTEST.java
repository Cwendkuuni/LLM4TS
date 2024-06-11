import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() throws Exception {
 String testFilePath = "src/test/resources/LexerTest.csv";
 format = new CSVFormat(new CSVParserDefaultConfig.Builder().build(), ',');
 in = new ExtendedBufferedReader(new FileReader(testFilePath));
 lexer = new Lexer(format, in);
 }

 @After
 public void tearDown() throws Exception {
 in.close();
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscapeCR() throws IOException {
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test
 public void testReadEscapeLF() throws IOException {
 assertEquals(Constants.LF, lexer.readEscape());
 }

 @Test
 public void testReadEscapeTab() throws IOException {
 assertEquals(Constants.TAB, lexer.readEscape());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeInvalid() throws IOException {
 assertEquals(Constants.END_OF_STREAM, lexer.readEscape());
 }

 @Test
 public void testIsWhitespace() throws IOException {
 assertTrue(lexer.isWhitespace(Constants.SPACE));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 assertTrue(lexer.isStartOfLine(Constants.LF));
 assertTrue(lexer.isStartOfLine(Constants.CR));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 assertTrue(lexer.isDelimiter(Constants.COMMA));
 }

 @Test
 public void testIsEscape() throws IOException {
 assertTrue(lexer.isEscape(Constants.BACKSLASH));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 assertTrue(lexer.isQuoteChar(Constants.QUOTE));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 assertTrue(lexer.isCommentStart(Constants.COMMENT_START));
 }

 @Test
 public void testNextToken() throws IOException {
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertTrue(nextToken.getValue().startsWith("Test"));
 }

 @Test
 public void testNextTokenQuotedString() throws IOException {
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertTrue(nextToken.getValue().equals("\"Apache Commons CSV\""));
 }

 @Test
 public void testNextTokenComment() throws IOException {
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.COMMENT, nextToken.getType());
 assertTrue(nextToken.getValue().startsWith("--"));
 }

 @Test
 public void testNextTokenDelimiter() throws IOException {
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.DELIMITER, nextToken.getType());
 assertEquals(Constants.COMMA, nextToken.getValue());
 }

 @Test
 public void testNextTokenEscape() throws IOException {
 Token token = new Token();
 int escapedChar = Constants.BACKSLASH;
 int nextChar = Constants.LF;
 int expectedEscapedChar = Constants.LF;

 Token nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertEquals(token.getValue(), Character.toString(escapedChar));

 token.reset();
 assertEquals(TokenType.DELIMITER, nextToken.getType());
 assertEquals(Constants.COMMA, nextToken.getValue());

 token.reset();
 escapedChar = Constants.BACKSLASH;
 nextChar = Constants.CR;
 expectedEscapedChar = Constants.CR;

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertEquals(token.getValue(), Character.toString(escapedChar));

 token.reset();
 escapedChar = Constants.BACKSLASH;
 nextChar = Constants.TAB;
 expectedEscapedChar = Constants.TAB;

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertEquals(token.getValue(), Character.toString(escapedChar));

 token.reset();
 escapedChar = Constants.BACKSLASH;
 nextChar = Constants.BACKSLASH;
 expectedEscapedChar = Constants.BACKSLASH;

 nextToken = lexer.nextToken(token);
 assertEquals(TokenType.TEXT, nextToken.getType());
 assertEquals(token.getValue(), Character.toString(escapedChar) + Character.toString(escapedChar));

 token.reset();
 escapedChar = Constants.BACKSLASH;
 nextChar = Constants.END_OF_STREAM;
 // Should throw an IOException
 }
}