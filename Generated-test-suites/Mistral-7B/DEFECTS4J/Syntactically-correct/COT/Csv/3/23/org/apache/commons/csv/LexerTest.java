import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader in;
 private Lexer lexer;

 @Test
 public void testLexerConstruction() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test");
 lexer = new Lexer(format, in);
 assertEquals(',', lexer.delimiter);
 assertEquals(Constants.UNDEFINED, lexer.escape);
 assertEquals(Constants.UNDEFINED, lexer.quoteChar);
 assertEquals(Constants.UNDEFINED, lexer.commmentStart);
 assertTrue(lexer.ignoreSurroundingSpaces);
 assertTrue(lexer.ignoreEmptyLines);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test\nline1\nline2\nline3");
 lexer = new Lexer(format, in);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setEscapeCharacter('\\')
 .build();
 in = new StringReader("test\\r\\n");
 lexer = new Lexer(format, in);
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test
 public void testReadEscapeUnexpected() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setEscapeCharacter('\\')
 .build();
 in = new StringReader("test\\q");
 lexer = new Lexer(format, in);
 assertThrows(IOException.class, () -> lexer.readEscape());
 }

 @Test
 public void testIsWhitespace() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader(" test\n  line 1  \t\n");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isWhitespace(' '));
 assertTrue(lexer.isWhitespace('\t'));
 assertTrue(lexer.isWhitespace('\n'));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("line1\nline2\nline3");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isStartOfLine(Constants.LF));
 assertTrue(lexer.isStartOfLine(Constants.CR));
 assertFalse(lexer.isStartOfLine('a'));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test");
 lexer = new Lexer(format, in);
 assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
 lexer.close();
 in.close();
 lexer = new Lexer(format, in);
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test,test");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isDelimiter(','));
 assertFalse(lexer.isDelimiter(' '));
 }

 @Test
 public void testIsEscape() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setEscapeCharacter('\\')
 .build();
 in = new StringReader("test\\");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isEscape(Constants.BACKSLASH));
 assertFalse(lexer.isEscape(' '));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setQuoteChar('"')
 .build();
 in = new StringReader("test\"test");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isQuoteChar('"'));
 assertFalse(lexer.isQuoteChar(' '));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setCommentStart("#")
 .build();
 in = new StringReader("test#comment");
 lexer = new Lexer(format, in);
 assertTrue(lexer.isCommentStart('#'));
 assertFalse(lexer.isCommentStart(' '));
 }

 @Test
 public void nextTokenTest() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test,test,test");
 lexer = new Lexer(format, in);
 Token token = new Token();
 Token nextToken = lexer.nextToken(token);
 assertEquals("test", nextToken.getValue());
 assertEquals(TokenType.STRING, nextToken.getType());
 nextToken = lexer.nextToken(token);
 assertEquals(",", nextToken.getValue());
 assertEquals(TokenType.DELIMITER, nextToken.getType());
 nextToken = lexer.nextToken(token);
 assertEquals("test", nextToken.getValue());
 assertEquals(TokenType.STRING, nextToken.getType());
 nextToken = lexer.nextToken(token);
 assertEquals(",", nextToken.getValue());
 assertEquals(TokenType.DELIMITER, nextToken.getType());
 assertTrue(lexer.isEndOfFile(lexer.read()));
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader(" test\tline1  \t\n");
 lexer = new Lexer(format, in);
 StringBuilder buffer = new StringBuilder();
 lexer.readString(buffer, 100);
 assertEquals("line1", buffer.toString().trim());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 format = new CSVFormatBuilder()
 .setDelimiter(',')
 .setHeaderExcluded(true)
 .build();
 in = new StringReader("test\nline1\r\nline2\n");
 lexer = new Lexer(format, in);
 assertTrue(lexer.readEndOfLine(Constants.CR));
 assertTrue(lexer.readEndOfLine(Constants.LF));
 assertFalse(lexer.readEndOfLine(Constants.BACKSLASH));
 }
}