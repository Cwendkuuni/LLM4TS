import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() {
 format = new CSVFormat();
 in = mock(ExtendedBufferedReader.class);
 lexer = new Lexer(format, in);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 int lineNumber = lexer.getLineNumber();
 when(in.getLineNumber()).thenReturn(5);
 assertEquals(5, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeInvalidCharacter() throws IOException {
 when(in.read()).thenReturn(-1);
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace() {
 assertEquals(true, lexer.isWhitespace(32)); // Space
 assertEquals(true, lexer.isWhitespace(10)); // Line Feed
 assertEquals(true, lexer.isWhitespace(13)); // Carriage Return
 }

 @Test
 public void testIsStartOfLine() {
 assertEquals(true, lexer.isStartOfLine(10)); // Line Feed
 assertEquals(true, lexer.isStartOfLine(13)); // Carriage Return
 assertEquals(true, lexer.isStartOfLine(UNDEFINED)); // Start of file
 }

 @Test
 public void testIsEndOfFile() {
 assertEquals(true, lexer.isEndOfFile(END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() {
 char delimiter = ',';
 format.setDelimiter(delimiter);
 lexer = new Lexer(format, in);
 assertEquals(true, lexer.isDelimiter(delimiter));
 }

 @Test
 public void testIsEscape() {
 char escape = '\\';
 format.setEscape(escape);
 lexer = new Lexer(format, in);
 assertEquals(true, lexer.isEscape(escape));
 }

 @Test
 public void testIsQuoteChar() {
 char quoteChar = '\'';
 format.setQuoteChar(quoteChar);
 lexer = new Lexer(format, in);
 assertEquals(true, lexer.isQuoteChar(quoteChar));
 }

 @Test
 public void testIsCommentStart() {
 char commentStart = '/';
 format.setCommentStart(commentStart);
 lexer = new Lexer(format, in);
 assertEquals(true, lexer.isCommentStart(commentStart));
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 int c = 10;
 assertTrue(lexer.readEndOfLine(c));
 }

 @Test
 public void testTrimTrailingSpaces() {
 StringBuilder buffer = new StringBuilder("    test    ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("test", buffer.toString());
 }

 @Test
 public void testNextTokenNullToken() throws IOException {
 Token reusableToken = null;
 Token token = lexer.nextToken(reusableToken);
 assertFalse(token.isValueSet());
 assertNull(token.getValue());
 }

 @Test
 public void testNextTokenEmptyString() throws IOException {
 StringBuilder reusableToken = new StringBuilder();
 StringBuilder expectedValue = new StringBuilder();
 Token token = lexer.nextToken(reusableToken);
 expectedValue.append(" ");
 reusableToken.setLength(0);
 assertEquals(expectedValue.toString(), token.getValue());
 token = lexer.nextToken(reusableToken);
 expectedValue.setLength(0);
 assertFalse(token.isValueSet());
 }

 @Test
 public void testNextTokenString() throws IOException {
 StringBuilder reusableToken = new StringBuilder();
 StringBuilder expectedValue = new StringBuilder("test");
 Token token = lexer.nextToken(reusableToken);
 assertEquals(expectedValue.toString(), token.getValue());
 reusableToken.setLength(0);
 }

 @Test
 public void testNextTokenInteger() throws IOException {
 StringBuilder reusableToken = new StringBuilder();
 int expectedValue = 42;
 when(in.readInt()).thenReturn(expectedValue);
 Token token = lexer.nextToken(reusableToken);
 assertEquals(Integer.valueOf(expectedValue), token.getValue());
 reusableToken.setLength(0);
 }
}