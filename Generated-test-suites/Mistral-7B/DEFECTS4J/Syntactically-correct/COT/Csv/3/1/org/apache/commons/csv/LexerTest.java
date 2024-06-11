import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private CSVFormat mockFormat;

 @Before
 public void setUp() {
 mockFormat = mock(CSVFormat.class);
 mockReader = mock(ExtendedBufferedReader.class);
 lexer = new Lexer(mockFormat, mockReader);
 }

 @After
 public void tearDown() throws IOException {
 mockReader.close();
 }

 @Test
 public void testGetLineNumber() throws IOException {
 when(mockReader.getLineNumber()).thenReturn(1);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeWithEndOfStream() throws IOException {
 when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace() throws IOException {
 assertEquals(true, lexer.isWhitespace(' '));
 assertEquals(true, lexer.isWhitespace('\t'));
 assertEquals(true, lexer.isWhitespace('\n'));
 assertEquals(true, lexer.isWhitespace('\r'));
 assertEquals(true, lexer.isWhitespace('\f'));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 when(mockReader.getLastChar()).thenReturn((char) 10);
 when(mockReader.read()).thenReturn((int) '\r');
 assertTrue(lexer.isStartOfLine((int) '\r'));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 when(mockReader.getLastChar()).thenReturn((char) Constants.END_OF_STREAM);
 assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 when(mockReader.read()).thenReturn((int) Lexer.this.delimiter);
 assertTrue(lexer.isDelimiter((int) Lexer.this.delimiter));
 }

 @Test
 public void testIsEscape() throws IOException {
 when(mockReader.read()).thenReturn((int) Lexer.this.escape);
 assertTrue(lexer.isEscape((int) Lexer.this.escape));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 when(mockReader.read()).thenReturn((int) Lexer.this.quoteChar);
 assertTrue(lexer.isQuoteChar((int) Lexer.this.quoteChar));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 when(mockReader.read()).thenReturn((int) Lexer.this.commmentStart);
 assertTrue(lexer.isCommentStart((int) Lexer.this.commmentStart));
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 when(mockReader.lookAhead()).thenReturn('r');
 when(mockReader.read()).thenReturn(13);
 when(mockReader.read()).thenReturn(10);
 assertTrue(lexer.readEndOfLine((int) 13));
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder("abc def");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());

 buffer.setLength(buffer.length() + 5);
 buffer.append("  ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc def", buffer.toString());
}
}