import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Before
 public void setUp() {
 format = new CSVFormat(',', null);
 try {
 in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(
 "foo,bar\nbaz,qux\r\n"
 .getBytes())));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 lexer = new Lexer(format, in);
 }

 @After
 public void tearDown() throws IOException {
 in.close();
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape_CR() throws IOException {
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test
 public void testReadEscape_LF() throws IOException {
 assertEquals(Constants.LF, lexer.readEscape());
 }

 @Test
 public void testReadEscape_Tab() throws IOException {
 assertEquals(Constants.TAB, lexer.readEscape());
 }

 @Test
 public void testReadEscape_Backspace() throws IOException {
 assertEquals(Constants.BACKSPACE, lexer.readEscape());
 }

 @Test
 public void testReadEscape_InvalidSequence() throws IOException {
 assertEquals(Constants.UNDEFINED, lexer.readEscape());
 }

 @Test(expected = IOException.class)
 public void testReadEscape_EndOfStream() throws IOException {
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace_Delimiter() {
 assertFalse(lexer.isWhitespace(','));
 }

 @Test
 public void testIsWhitespace_Space() {
 assertTrue(lexer.isWhitespace(' '));
 }

 @Test
 public void testIsStartOfLine_CR() {
 assertTrue(lexer.isStartOfLine(Constants.CR));
 }

 @Test
 public void testIsStartOfLine_LF() {
 assertTrue(lexer.isStartOfLine(Constants.LF));
 }

 @Test
 public void testIsStartOfLine_StartOfFile() {
 assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
 }

 @Test
 public void testIsEndOfFile_EndOfStream() {
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsEndOfFile_Other() {
 assertFalse(lexer.isEndOfFile('a'));
 }

 @Test
 public void testIsDelimiter_Delimiter() {
 assertTrue(lexer.isDelimiter(','));
 }

 @Test
 public void testIsDelimiter_Other() {
 assertFalse(lexer.isDelimiter('a'));
 }

 @Test
 public void testIsEscape_Escape() {
 assertTrue(lexer.isEscape(lexer.escape));
 }

 @Test
 public void testIsEscape_Other() {
 assertFalse(lexer.isEscape('a'));
 }

 @Test
 public void testIsQuoteChar_QuoteChar() {
 assertTrue(lexer.isQuoteChar(lexer.quoteChar));
 }

 @Test
 public void testIsQuoteChar_Other() {
 assertFalse(lexer.isQuoteChar('a'));
 }

 @Test
 public void testIsCommentStart_CommentStart() {
 assertTrue(lexer.isCommentStart(lexer.commmentStart));
 }

 @Test
 public void testIsCommentStart_Other() {
 assertFalse(lexer.isCommentStart('a'));
 }

 @Test
 public void testNextToken() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(1, token.getColumn());
 assertEquals("foo", token.getValue());
 assertEquals(TokenType.STRING, token.getType());
 }

 @Test
 public void testNextToken_IgnoreSurroundingSpaces() throws IOException {
 lexer = new Lexer(format, in);
 lexer.ignoreSurroundingSpaces = true;
 Token token = lexer.nextToken(null);
 assertEquals(1, token.getColumn());
 assertEquals("foo", token.getValue());
 assertEquals(TokenType.STRING, token.getType());
 }

 @Test
 public void testNextToken_IgnoreEmptyLines() throws IOException {
 lexer = new Lexer(format, in);
 lexer.ignoreEmptyLines = true;
 assertEquals(2, lexer.getLineNumber()); // skip first line
 Token token = lexer.nextToken(null);
 assertEquals(1, token.getColumn());
 assertEquals("baz", token.getValue());
 assertEquals(TokenType.STRING, token.getType());
 }
}