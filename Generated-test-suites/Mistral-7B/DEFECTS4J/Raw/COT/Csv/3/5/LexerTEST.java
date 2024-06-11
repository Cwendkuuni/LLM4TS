import org.apache.commons.csv.Constants;
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
 mockReader = mock(ExtendedBufferedReader.class);
 format = new CSVFormat(new String[]{}, Constants.DEFAULT_LINE_SEPARATOR, Constants.DEFAULT_CHARSET);
 lexer = new Lexer(format, mockReader);
 }

 /**
 * Test getLineNumber method
 */
 @Test
 public void testGetLineNumber() {
 int initialLineNumber = lexer.getLineNumber();
 when(mockReader.getLineNumber()).thenReturn(1);
 int expectedLineNumber = 1;
 int actualLineNumber = lexer.getLineNumber();
 assertEquals(expectedLineNumber, actualLineNumber);
 }

 /**
 * Test isStartOfLine method with CR character
 */
 @Test
 public void testIsStartOfLineCR() throws IOException {
 int cr = Constants.CR;
 assertTrue(lexer.isStartOfLine(cr));
 }

 /**
 * Test isStartOfLine method with LF character
 */
 @Test
 public void testIsStartOfLineLF() throws IOException {
 int lf = Constants.LF;
 assertTrue(lexer.isStartOfLine(lf));
 }

 /**
 * Test isStartOfLine method with undefined character
 */
 @Test
 public void testIsStartOfLineUndefined() throws IOException {
 assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
 }

 /**
 * Test isStartOfLine method with non-line-terminator character
 */
 @Test
 public void testIsStartOfLineNonLineTerminator() throws IOException {
 int nonLineTerminator = 45; // Assuming ASCII value 45 for character 'E'
 assertFalse(lexer.isStartOfLine(nonLineTerminator));
 }

 /**
 * Test isEndOfFile method with EOF character
 */
 @Test
 public void testIsEndOfFile() throws IOException {
 assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
 }

 /**
 * Test isEndOfFile method with non-EOF character
 */
 @Test
 public void testIsEndOfFileNonEOF() throws IOException {
 int nonEOF = 'A';
 assertFalse(lexer.isEndOfFile(nonEOF));
 }

 /**
 * Test isWhitespace method with whitespace character
 */
 @Test
 public void testIsWhitespace() throws IOException {
 int space = ' ';
 assertTrue(lexer.isWhitespace(space));
 }

 /**
 * Test isWhitespace method with non-whitespace character
 */
 @Test
 public void testIsWhitespaceNonWhitespace() throws IOException {
 int nonWhitespace = 'A';
 assertFalse(lexer.isWhitespace(nonWhitespace));
 }

 /**
 * Test isDelimiter method with delimiter character
 */
 @Test
 public void testIsDelimiter() throws IOException {
 int delimiter = lexer.delimiter;
 assertTrue(lexer.isDelimiter(delimiter));
 }

 /**
 * Test isDelimiter method with non-delimiter character
 */
 @Test
 public void testIsDelimiterNonDelimiter() throws IOException {
 int nonDelimiter = 'A';
 assertFalse(lexer.isDelimiter(nonDelimiter));
 }

 /**
 * Test isEscape method with escape character
 */
 @Test
 public void testIsEscape() throws IOException {
 int escape = lexer.escape;
 assertTrue(lexer.isEscape(escape));
 }

 /**
 * Test isEscape method with non-escape character
 */
 @Test
 public void testIsEscapeNonEscape() throws IOException {
 int nonEscape = 'A';
 assertFalse(lexer.isEscape(nonEscape));
 }

 /**
 * Test isQuoteChar method with quoteChar character
 */
 @Test public void testIsQuoteChar() throws IOException {
 int quoteChar = lexer.quoteChar;
 assertTrue(lexer.isQuoteChar(quoteChar));
 }

 /**
 * Test isQuoteChar method with non-quoteChar character
 */
 @Test
 public void testIsQuoteCharNonQuoteChar() throws IOException {
 int nonQuoteChar = 'A';
 assertFalse(lexer.isQuoteChar(nonQuoteChar));
 }

 /**
 * Test isCommentStart method with commentStart character
 */
 @Test
 public void testIsCommentStart() throws IOException {
 int commentStart = lexer.commmentStart;
 assertTrue(lexer.isCommentStart(commentStart));
 }

 /**
 * Test isCommentStart method with non-commentStart character
 */
 @Test
 public void testIsCommentStartNonCommentStart() throws IOException {
 int nonCommentStart = 'A';
 assertFalse(lexer.isCommentStart(nonCommentStart));
 }

 /**
 * Test nextToken method
 */
 @Test
 public void testNextToken() throws IOException {
 Token reusableToken = new Token();
 Token token = lexer.nextToken(reusableToken);
 assertNull(token);
 }

 /**
 * Test readEscape method with CR character
 */
 @Test(expected = IOException.class)
 public void testReadEscapeCR() throws IOException {
 lexer.readEscape();
 }

 /**
 * Test readEscape method with LF character
 */
 @Test(expected = IOException.class)
 public void testReadEscapeLF() throws IOException {
 lexer.readEscape();
 }

 /**
 * Test readEscape method with TAB character
 */
 @Test(expected = IOException.class)
 public void testReadEscapeTab() throws IOException {
 lexer.readEscape();
 }

 /**
 * Test readEscape method with non-line-terminator character
 */
 @Test(expected = IOException.class)
 public void testReadEscapeNonLineTerminator() throws IOException {
 int nonLineTerminator = 45; // Assuming ASCII value 45 for character 'E'
 lexer.readEscape();
 }

 /**
 * Test readEscape method with valid escape character followed by CR character
 */
 @Test
 public void testReadEscapeCRafterEscape() throws IOException {
 int escape = lexer.escape;
 int cr = Constants.CR;
 int expectedCR = cr;
 when(mockReader.read()).thenReturn(escape).thenReturn(cr);
 int actualCR = lexer.readEscape();
 assertEquals(expectedCR, actualCR);
 }

 /**
 * Test readEscape method with valid escape character followed by LF character
 */
 @Test
 public void testReadEscapeLFafterEscape() throws IOException {
 int escape = lexer.escape;
 int lf = Constants.LF;
 int expectedLF = lf;
 when(mockReader.read()).thenReturn(escape).thenReturn(lf);
 int actualLF = lexer.readEscape();
 assertEquals(expectedLF, actualLF);
 }

 /**
 * Test readEscape method with valid escape character followed by non-line-terminator character
 */
 @Test(expected = IOException.class)
 public void testReadEscapeNonLineTerminatorafterEscape() throws IOException {
 int escape = lexer.escape;
 int nonLineTerminator = 45; // Assuming ASCII value 45 for character 'E'
 when(mockReader.read()).thenReturn(escape).thenReturn(nonLineTerminator);
 }

 /**
 * Test trimTrailingSpaces method
 */
 @Test
 public void testTrimTrailingSpaces() {
 StringBuilder buffer = new StringBuilder(" test string   ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals("test string", buffer.toString());
 }
}