import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private CSVFormat format;

 @Before
 public void setup() throws IOException {
 mockReader = new MockExtendedBufferedReader();
 format = new CSVFormat();
 lexer = new Lexer(format, mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 // Test normal line number
 lexer.getLineNumber();
 assertEquals(1, mockReader.getLineNumber());

 // Test line number after reading a line
 mockReader.readLine();
 lexer.getLineNumber();
 assertEquals(2, mockReader.getLineNumber());
 }

 @Test
 public void testMapNullToDisabled() {
 assertEquals(Constants.DISABLED, lexer.mapNullToDisabled(null));
 assertEquals('a', lexer.mapNullToDisabled('a'));
 }

 @Test
 public void testReadEscape() throws IOException {
 // Test reading valid escape sequences
 assertEquals(Constants.CR, lexer.readEscape());
 assertEquals(Constants.LF, lexer.readEscape());
 assertEquals(Constants.TAB, lexer.readEscape());
 assertEquals(Constants.BACKSPACE, lexer.readEscape());
 assertEquals(Constants.FF, lexer.readEscape());

 // Test reading invalid escape sequence
 mockReader.setLastChar(Constants.END_OF_STREAM);
 try {
 lexer.readEscape();
 fail("Should have thrown an IOException");
 } catch (IOException e) {
 // Expected
 }
}

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder(" abc def ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());

 buffer.setLength(5);
 buffer.append(" abc ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc", buffer.toString());
}

 @Test
 public void testReadEndOfLine() throws IOException {
 // Test CR
 int c = Constants.CR;
 assertTrue(lexer.readEndOfLine(c));
 assertEquals(Constants.CR, mockReader.getLastChar());
 mockReader.reset();

 // Test LF
 c = Constants.LF;
 assertTrue(lexer.readEndOfLine(c));
 assertEquals(Constants.LF, mockReader.getLastChar());
 mockReader.reset();

 // Test CR+LF
 c = Constants.CR;
 mockReader.setLastChar(Constants.LF);
 assertTrue(lexer.readEndOfLine(c));
 assertEquals(Constants.LF, mockReader.getLastChar());
 mockReader.reset();

 // Test invalid line terminator
 mockReader.setLastChar(Constants.BACKSPACE);
 assertFalse(lexer.readEndOfLine((int) Constants.BACKSPACE));
 assertEquals((int) Constants.BACKSPACE, mockReader.getLastChar());
}

 @Test(expected = IOException.class)
 public void testReadEndOfLine_atEndOfStream() throws IOException {
 // Test EOF
 int c = Constants.END_OF_STREAM;
 lexer.readEndOfLine(c);
 fail("Should have thrown an IOException");
}

 @Test
 public void testNextToken() throws IOException {
 // Create test token
 Token token = new Token();
 token.setValue("test");
 token.setStart(0);
 token.setEnd(4);

 // Test nextToken with no reusable token
 Token nextToken = lexer.nextToken(null);
 assertNull(nextToken);

 // Test nextToken with reusable token
 nextToken = lexer.nextToken(token);
 assertNotNull(nextToken);
 assertEquals("test", nextToken.getValue());
 assertEquals(4, nextToken.getLength());
 assertEquals(0, nextToken.getStart());
 assertEquals(4, nextToken.getEnd());
}

 @Test
 public void testIsWhitespace() {
 assertTrue(lexer.isWhitespace(Constants.TAB));
 assertTrue(lexer.isWhitespace(Constants.SPACE));
 assertFalse(lexer.isWhitespace(Constants.COMMA));
}

 @Test
 public void testIsStartOfLine() {
 // Test CR
 assertTrue(lexer.isStartOfLine((int) Constants.CR));

 // Test LF
 assertTrue(lexer.isStartOfLine((int) Constants.LF));

 // Test UNDEFINED
 assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));

 // Test non-start-of-line
 assertFalse(lexer.isStartOfLine((int) Constants.COMMA));
}

 @Test
 public void testIsEndOfFile() {
 // Test valid EOF
 assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));

 // Test non-EOF
 assertFalse(lexer.isEndOfFile((int) Constants.COMMA));
}

 @Test
 public void testIsDelimiter() {
 // Test valid delimiter
 assertTrue(lexer.isDelimiter((int) Constants.COMMA));

 // Test non-delimiter
 assertFalse(lexer.isDelimiter((int) Constants.SPACE));
}

 @Test
 public void testIsEscape() {
 // Test valid escape character
 assertTrue(lexer.isEscape((int) Constants.BACKSLASH));

 // Test invalid character
 assertFalse(lexer.isEscape((int) Constants.COMMA));
}

 @Test
 public void testIsQuoteChar() {
 // Test valid quote character
 assertTrue(lexer.isQuoteChar((int) Constants.QUOTE_CHAR));

 // Test invalid character
 assertFalse(lexer.isQuoteChar((int) Constants.COMMA));
}

 @Test
 public void testIsCommentStart() {
 // Test valid comment start character
 assertTrue(lexer.isCommentStart((int) Constants.COMMENT_START));

 // Test invalid character
 assertFalse(lexer.isCommentStart((int) Constants.COMMA));
}
}

 class MockExtendedBufferedReader extends ExtendedBufferedReader {
 @Override
 public int read() throws IOException {
 return 0;
 }

 @Override
 public int read(char[] cbuf) throws IOException {
 return 0;
 }

 @Override
 public int read(char[] cbuf, int off, int len) throws IOException {
 return 0;
 }

 @Override
 public long skip(long n) throws IOException {
 return 0;
 }

 @Override
 public String readLine() throws IOException {
 return null;
 }

 @Override
 public boolean markSupported() {
 return false;
 }

 @Override
 public void mark(int readAheadLimit) {
 // Empty
 }

 @Override
 public void reset() {
 // Empty
 }

 @Override
 public int available() throws IOException {
 return 0;
 }

 @Override
 public boolean readLine(StringBuilder buf, int max) throws IOException {
 return false;
 }

 @Override
 public boolean hasNextLine() throws IOException {
 return false;
 }

 @Override
 public void close() throws IOException {
 // Empty
 }
 }