import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockReader;
 private CSVFormat format;

 @Before
 public void setup() {
 format = new CSVFormat(new CSVParserDefaultImplementation(), '\t');
 mockReader = mock(ExtendedBufferedReader.class);
 lexer = new Lexer(format, mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 when(mockReader.getLineNumber()).thenReturn(1);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape_CR() throws IOException {
 when(mockReader.read()).thenReturn('r');
 assertEquals(CR, lexer.readEscape());
 }

 @Test
 public void testReadEscape_LF() throws IOException {
 when(mockReader.read()).thenReturn('n');
 assertEquals(LF, lexer.readEscape());
 }

 @Test(expected = IOException.class)
 public void testReadEscape_EndOfStream() throws IOException {
 when(mockReader.read()).thenReturn(ExtendedBufferedReader.EOF);
 lexer.readEscape();
 }

 @Test
 public void testIsWhitespace() {
 assertTrue(lexer.isWhitespace(' '));
 assertFalse(lexer.isWhitespace('\t'));
 assertFalse(lexer.isWhitespace('\r'));
 assertFalse(lexer.isWhitespace('\n'));
 }

 @Test
 public void testIsStartOfLine() {
 assertTrue(lexer.isStartOfLine(UNDEFINED));
 assertTrue(lexer.isStartOfLine(CR));
 assertTrue(lexer.isStartOfLine(LF));
 assertFalse(lexer.isStartOfLine('A'));
 }

 @Test
 public void testIsEndOfFile() {
 assertFalse(lexer.isEndOfFile(UNDEFINED));
 assertFalse(lexer.isEndOfFile(CR));
 assertFalse(lexer.isEndOfFile(LF));
 assertTrue(lexer.isEndOfFile(ExtendedBufferedReader.EOF));
 }

 @Test
 public void testIsDelimiter() {
 assertTrue(lexer.isDelimiter('\t'));
 assertFalse(lexer.isDelimiter('A'));
 }

 @Test
 public void testIsEscape() {
 assertFalse(lexer.isEscape(UNDEFINED));
 assertFalse(lexer.isEscape('\t'));
 assertFalse(lexer.isEscape('\\'));
 assertTrue(lexer.isEscape('\ufffe'));
 }

 @Test
 public void testIsQuoteChar() {
 assertFalse(lexer.isQuoteChar(UNDEFINED));
 assertFalse(lexer.isQuoteChar('\t'));
 assertFalse(lexer.isQuoteChar('\\'));
 assertFalse(lexer.isQuoteChar('"'));
 assertTrue(lexer.isQuoteChar('\ufffe'));
 }

 @Test
 public void testIsCommentStart() {
 assertFalse(lexer.isCommentStart(UNDEFINED));
 assertFalse(lexer.isCommentStart('\t'));
 assertFalse(lexer.isCommentStart('%'));
 assertFalse(lexer.isCommentStart('\ufffe'));
 assertTrue(lexer.isCommentStart('\u0023'));
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder(" abc\t def ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals(" abc\ndef ", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 when(mockReader.read()).thenReturn(CR);
 assertTrue(lexer.readEndOfLine(CR));
 when(mockReader.read()).thenReturn(LF);
 assertTrue(lexer.readEndOfLine(LF));
 assertTrue(lexer.readEndOfLine(UNDEFINED));
 assertFalse(lexer.readEndOfLine('A'));
 }
}