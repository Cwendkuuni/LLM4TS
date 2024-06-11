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
 format = new CSVFormatBuilder() // Create a CSVFormat object for testing
 .setDelimiter(';') // Set the delimiter to semicolon for testing
 .build();

 in = mock(ExtendedBufferedReader.class); // Create a mock ExtendedBufferedReader
 lexer = new Lexer(format, in); // Create a Lexer instance
 }

 @Test
 public void testGetLineNumber() throws IOException {
 when(in.getLineNumber()).thenReturn(1);
 assertEquals(1, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeWithInvalidFollowingChar() throws IOException {
 when(in.read()).thenReturn('Z'); // Set next char as 'Z' (invalid character)
 lexer.readEscape();
 }

 @Test
 public void testTrimTrailingSpaces() {
 StringBuilder buffer = new StringBuilder(" a b\t c\t d ");
 lexer.trimTrailingSpaces(buffer);
 assertEquals(" a b c ", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 when(in.lookAhead()).thenReturn('R'); // Set next char as 'R'
 when(in.read()).thenReturn('N'); // Set second char as 'N'
 assertTrue(lexer.readEndOfLine(0));
 }

 @Test
 public void testIsStartOfLine() {
 assertTrue(lexer.isStartOfLine(UNDEFINED));
 assertTrue(lexer.isStartOfLine(LF));
 assertTrue(lexer.isStartOfLine(CR));
 }

 @Test
 public void testIsEndOfFile() {
 when(in.read()).thenReturn(END_OF_STREAM);
 assertTrue(lexer.isEndOfFile(END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() {
 assertTrue(lexer.isDelimiter(';'));
 assertFalse(lexer.isDelimiter(','));
 }

 @Test
 public void testIsEscape() {
 assertFalse(lexer.isEscape(';'));
 assertFalse(lexer.isEscape('\\'));
 assertTrue(lexer.isEscape('\uufffe')); // Check DISABLED character
 }

 @Test
 public void testIsQuoteChar() {
 assertFalse(lexer.isQuoteChar(';'));
 assertFalse(lexer.isQuoteChar('"'));
 assertTrue(lexer.isQuoteChar('\u0002')); // Check a non-standard quote char
 }

 @Test
 public void testIsCommentStart() {
 assertFalse(lexer.isCommentStart(';'));
 assertFalse(lexer.isCommentStart('%'));
 assertTrue(lexer.isCommentStart('!'));
 }
}