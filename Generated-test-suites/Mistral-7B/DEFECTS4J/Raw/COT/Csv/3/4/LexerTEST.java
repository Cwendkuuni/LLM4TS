import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Constants;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

 private ExtendedBufferedReader mockReader;
 private CSVFormat mockFormat;

 @Before
 public void setUp() {
 mockFormat = mock(CSVFormat.class);
 mockReader = mock(ExtendedBufferedReader.class);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.getLineNumber()).thenReturn(1);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape_CR() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn('r');
 assertEquals(Constants.CR, lexer.readEscape());
 }

 @Test
 public void testReadEscape_LF() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn('n');
 assertEquals(Constants.LF, lexer.readEscape());
 }

 @Test(expected = IOException.class)
 public void testReadEscape_EndOfStream() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
 lexer.readEscape();
 }

 @Test
 public void testIsStartOfLine_CR() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isStartOfLine(Constants.CR));
 }

 @Test
 public void testIsStartOfLine_LF() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isStartOfLine(Constants.LF));
 }

 @Test
 public void testIsStartOfLine_Undefined() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
 }

 @Test
 public void testIsEndOfFile_EndOfStream() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 @Test
 public void testIsWhitespace_Space() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isWhitespace(' '));
 }

 @Test
 public void testIsWhitespace_Tab() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isWhitespace('\t'));
 }

 @Test
 public void testIsWhitespace_Newline() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isWhitespace('\n'));
 }

 @Test
 public void testIsWhitespace_Delimiter() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertFalse(lexer.isWhitespace(mockFormat.getDelimiter()));
 }

 @Test
 public void testIsDelimiter_Delimiter() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isDelimiter(mockFormat.getDelimiter()));
 }

 @Test
 public void testIsDelimiter_OtherChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertFalse(lexer.isDelimiter('x'));
 }

 @Test
 public void testIsEscape_EscapeChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isEscape(mockFormat.getEscape()));
 }

 @Test
 public void testIsEscape_OtherChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertFalse(lexer.isEscape('x'));
 }

 @Test
 public void testIsQuoteChar_QuoteChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isQuoteChar(mockFormat.getQuoteChar()));
 }

 @Test
 public void testIsQuoteChar_OtherChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertFalse(lexer.isQuoteChar('x'));
 }

 @Test
 public void testIsCommentStart_CommentStart() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertTrue(lexer.isCommentStart(mockFormat.getCommentStart()));
 }

 @Test
 public void testIsCommentStart_OtherChar() {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 assertFalse(lexer.isCommentStart('x'));
 }

 @Test
 public void testReadEndOfLine_CR() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn(Constants.CR);
 assertTrue(lexer.readEndOfLine(Constants.CR));
 }

 @Test
 public void testReadEndOfLine_LF() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn(Constants.LF);
 assertTrue(lexer.readEndOfLine(Constants.LF));
 }

 @Test
 public void testReadEndOfLine_CR_LF() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn(Constants.CR).thenReturn(Constants.LF);
 assertTrue(lexer.readEndOfLine(Constants.CR));
 }

 @Test(expected = IOException.class)
 public void testReadEndOfLine_EndOfStream() throws IOException {
 final Lexer lexer = new Lexer(mockFormat, mockReader);
 when(mockReader.read()).thenReturn(Constants.END_OF_STREAM);
 lexer.readEndOfLine(Constants.END_OF_STREAM);
 }
}