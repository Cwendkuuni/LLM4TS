import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() throws IOException {
 // Set up test data
 format = new CSVFormat(',', '\u001E'); // use a custom delimiter and escape char
 in = new ExtendedBufferedReader(new StringReader("")); // empty input stream
 lexer = new Lexer(format, in);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(0, lexer.getLineNumber());
 // Assuming setUp() sets up the input stream to have a line number > 0
 int lineNumber = in.getLineNumber();
 lexer = new Lexer(format, in);
 assertEquals(lineNumber, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape() throws IOException {
 int c;
 for (int i = 0; i < 10; i++) {
 c = lexer.readEscape();
 assertEquals(i % 5 == 0 ? Constants.CR : i % 5 == 1 ? Constants.LF : i % 5 == 2 ? Constants.TAB : i % 5 == 3 ? Constants.BACKSPACE : Constants.FF, c);
 }
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder(" test string   ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("test string", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 int c;
 for (int i = 0; i < 10; i++) {
 c = lexer.read();
 if (i == 0) {
 assertTrue(lexer.readEndOfLine(c));
 } else {
 assertFalse(lexer.readEndOfLine(c));
 }
 }
 }

 @Test(expected = IOException.class)
 public void testReadEscapeAtEndOfStream() throws IOException {
 lexer.readEscape();
 lexer.readEscape(); // should throw an IOException
 }

 @Test
 public void testIsWhitespace() throws IOException {
 for (int i = 0; i < 256; i++) {
 if (Character.isWhitespace((char) i)) {
 assertTrue(lexer.isWhitespace(i));
 } else {
 assertFalse(lexer.isWhitespace(i));
 }
 }
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 for (int i = 0; i < 256; i++) {
 switch (i) {
 case '\r':
 case '\n':
 case Constants.UNDEFINED:
 assertTrue(lexer.isStartOfLine(i));
 break;
 default:
 assertFalse(lexer.isStartOfLine(i));
 }
 }
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 int c = (int) Constants.END_OF_STREAM;
 assertTrue(lexer.isEndOfFile(c));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 int c = (int) lexer.delimiter;
 assertTrue(lexer.isDelimiter(c));
 }

 @Test
 public void testIsEscape() throws IOException {
 int c = (int) lexer.escape;
 assertTrue(lexer.isEscape(c));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 int c = (int) lexer.quoteChar;
 assertTrue(lexer.isQuoteChar(c));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 int c = (int) lexer.commmentStart;
 assertTrue(lexer.isCommentStart(c));
 }

 @Test
 public void testNextToken() throws IOException {
 Token token = new Token(TokenType.START_ARRAY, "", 0); // create a reusable token
 nextTokenMethodCall(token); // test nextToken() call
 assertEquals(TokenType.START_ARRAY, token.getType());
 // Test for other token types here
 }

 private void nextTokenMethodCall(Token token) throws IOException {
 // Call nextToken() method and check the resulting token type
 lexer.nextToken(token);
 }
}