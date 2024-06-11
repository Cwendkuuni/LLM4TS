import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private CSVFormat format;

 @Before
 public void setup() {
 format = new CSVFormat();
 mockReader = mock(ExtendedBufferedReader.class);
 lexer = new Lexer(format, mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 when(mockReader.getLineNumber()).thenReturn(1);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeWithInvalidChar() throws IOException {
 when(mockReader.read()).thenReturn('X');
 lexer.readEscape();
 }

 @Test
 public void testReadEscapeCR() throws IOException {
 when(mockReader.read()).thenReturn(Constants.CR);
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test
 public void testReadEscapeLF() throws IOException {
 when(mockReader.read()).thenReturn(Constants.LF);
 assertEquals(Constants.LF, lexer.readEscape());
 }

 @Test
 public void testReadEscapeTab() throws IOException {
 when(mockReader.read()).thenReturn(Constants.TAB);
 assertEquals(Constants.TAB, lexer.readEscape());
 }

 @Test
 public void testReadEscapeBackspace() throws IOException {
 when(mockReader.read()).thenReturn(Constants.BACKSPACE);
 assertEquals(Constants.BACKSPACE, lexer.readEscape());
 }

 @Test
 public void testReadEscapeFF() throws IOException {
 when(mockReader.read()).thenReturn(Constants.FF);
 assertEquals(Constants.FF, lexer.readEscape());
 }

 @Test
 public void testReadEscapeUnexpected() throws IOException {
 when(mockReader.read()).thenReturn('X');
 assertNull(lexer.readEscape());
 }

 @Test
 public void testReadEscapeEndOfStream() throws IOException {
 when(mockReader.read()).thenThrow(new IOException());
 assertSame(mockReader.getLastChar(), lexer.readEscape());
 }

 @Test
 public void testTrimTrailingSpaces() {
 StringBuilder buffer = new StringBuilder("  abc  ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("abc", buffer.toString());
 }

 @Test
 public void testIsStartOfLine() {
 assertEquals(true, lexer.isStartOfLine(Constants.LF));
 assertEquals(true, lexer.isStartOfLine(Constants.CR));
 assertEquals(true, lexer.isStartOfLine(Constants.UNDEFINED));
 assertEquals(false, lexer.isStartOfLine('A'));
 }

 @Test
 public void testIsEndOfFile() {
 assertEquals(false, lexer.isEndOfFile(Constants.LF));
 assertEquals(false, lexer.isEndOfFile(Constants.CR));
 assertEquals(false, lexer.isEndOfFile('A'));
 assertEquals(true, lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() {
 assertEquals(false, lexer.isDelimiter('A'));
 assertEquals(true, lexer.isDelimiter(lexer.delimiter));
 }

 @Test
 public void testIsEscape() {
 assertEquals(false, lexer.isEscape('A'));
 assertEquals(true, lexer.isEscape(lexer.escape));
 }

 @Test
 public void testIsQuoteChar() {
 assertEquals(false, lexer.isQuoteChar('A'));
 assertEquals(true, lexer.isQuoteChar(lexer.quoteChar));
 }

 @Test
 public void testIsCommentStart() {
 assertEquals(false, lexer.isCommentStart('A'));
 assertEquals(true, lexer.isCommentStart(lexer.commmentStart));
 }

 @Test
 public void testIsWhitespace() {
 assertEquals(false, lexer.isWhitespace('A'));
 assertEquals(true, lexer.isWhitespace(Constants.TAB));
 assertEquals(true, lexer.isWhitespace(Constants.LF));
 }

 @Test
 public void testReadEndOfLineCR() throws IOException {
 assertTrue(lexer.readEndOfLine(Constants.CR));
 }

 @Test
 public void testReadEndOfLineLF() throws IOException {
 assertTrue(lexer.readEndOfLine(Constants.LF));
 }

 @Test
 public void testReadEndOfLineCRandLF() throws IOException {
 assertTrue(lexer.readEndOfLine((int) Constants.CR + (int) Constants.LF));
 }

 @Test
 public void testReadEndOfLineUnexpected() throws IOException {
 assertFalse(lexer.readEndOfLine('A'));
 }

 @Test(expected = IOException.class)
 public void testReadEndOfLineEndOfStreamCR() throws IOException {
 when(mockReader.read()).thenThrow(new IOException());
 lexer.readEndOfLine('A');
 }

 @Test(expected = IOException.class)
 public void testReadEndOfLineEndOfStreamLF() throws IOException {
 when(mockReader.read()).thenThrow(new IOException());
 lexer.readEndOfLine('A');
 }
}