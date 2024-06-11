import org.apache.commons.csv.Constants;
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

 @Test
 public void testGetLineNumber() throws IOException {
 String input = "Line 1\nLine 2\nLine 3";
 in = mock(ExtendedBufferedReader.class);
 when(in.getLineNumber()).thenReturn(new int[] {1, 2, 3});
 format = new CSVFormatBuilder().setDelimiter(',').build();
 lexer = new Lexer(format, in);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape_CR() throws IOException {
 in = mock(ExtendedBufferedReader.class);
 when(in.read()).thenReturn(Constants.CR);
 lexer = new Lexer(null, in);
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test(expected = IOException.class)
 public void testReadEscape_EOF() throws IOException {
 in = mock(ExtendedBufferedReader.class);
 when(in.read()).thenReturn(Constants.CR);
 when(in.read()).thenReturn(Constants.END_OF_STREAM);
 lexer = new Lexer(null, in);
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace() {
 char c = '\t';
 assertTrue(Lexer.isWhitespace(c));
 }

 @Test
 public void testIsStartOfLine() {
 assertTrue(Lexer.isStartOfLine(Constants.LF));
 }

 @Test
 public void testIsEndOfFile() {
 assertTrue(Lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() {
 char c = ',';
 assertTrue(Lexer.isDelimiter(c));
 }

 @Test
 public void testIsEscape() {
 char c = '\\';
 assertTrue(Lexer.isEscape(c));
 }

 @Test
 public void testIsQuoteChar() {
 char c = '\'';
 assertTrue(Lexer.isQuoteChar(c));
 }

 @Test
 public void testIsCommentStart() {
 char c = '/';
 assertTrue(Lexer.isCommentStart(c));
 }

 @Test
 public void testNextToken() throws IOException {
 String input = "Line 1,\"Field 1\",Field 2,Field 3\nLine 2,\"Field 4\",Field 5,Field 6";
 in = mock(ExtendedBufferedReader.class);
 when(in.readLine()).thenReturn(input.split("\\r?\\n"));
 format = new CSVFormatBuilder().setDelimiter(',').build();
 lexer = new Lexer(format, in);
 Token reusableToken = new Token();
 Token token = lexer.nextToken(reusableToken);
 assertEquals("Line 1", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 1", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 2", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 3", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Line 2", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 4", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 5", token.getString());
 token = lexer.nextToken(reusableToken);
 assertEquals("Field 6", token.getString());
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 String input = "Line 1   \nLine 2     ";
 StringBuilder buffer = new StringBuilder();
 in = mock(ExtendedBufferedReader.class);
 when(in.readLine()).thenReturn(input.split("\\r?\\n"));
 format = new CSVFormatBuilder().setDelimiter(',').build();
 lexer = new Lexer(format, in);
 lexer.nextToken(new Token());
 lexer.nextToken(new Token());
 buffer.append(lexer.nextToken(new Token()).getString());
 lexer.trimTrailingSpaces(buffer);
 assertEquals("Line 1", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 int c = Constants.CR;
 assertTrue(lexer.readEndOfLine(c));
 }

 @Test
 public void testIsStartOfLine_LF() {
 boolean result = Lexer.isStartOfLine(Constants.LF);
 assertTrue(result);
 }

 @Test
 public void testIsStartOfLine_CR() {
 boolean result = Lexer.isStartOfLine(Constants.CR);
 assertTrue(result);
 }

 @Test
 public void testIsStartOfLine_UNDEFINED() {
 boolean result = Lexer.isStartOfLine(Constants.UNDEFINED);
 assertTrue(result);
 }

 @Test
 public void testIsStartOfLine_Other() {
 char c = 'X';
 assertFalse(Lexer.isStartOfLine(c));
 }
}