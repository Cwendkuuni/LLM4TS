import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ext.ExtendedBufferedReader;
import org.apache.commons.csv.format.CSVFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LexerTest {

 private Lexer lexer;
 @Mock
 private ExtendedBufferedReader mockReader;
 @Mock
 private CSVFormat mockCSVFormat;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 lexer = new Lexer(mockCSVFormat, mockReader);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 assertEquals(1, lexer.getLineNumber());
 }

 @Test
 public void testReadEscape() throws IOException {
 // Test with valid escape sequences
 assertEquals(CR, lexer.readEscape());
 assertEquals(LF, lexer.readEscape());
 assertEquals(TAB, lexer.readEscape());
 assertEquals(BACKSPACE, lexer.readEscape());
 assertEquals(FF, lexer.readEscape());

 // Test with other characters
 assertEquals(Constants.BACKSPACE, lexer.readEscape());
 // Test with end of stream
 assertThrows(IOException.class, () -> lexer.readEscape());
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder buffer = new StringBuilder(" some text \t\t\r\n trailing spaces");
 lexer.trimTrailingSpaces(buffer);
 assertEquals(" some text", buffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 // Test with CR
 assertTrue(lexer.readEndOfLine((int) Constants.CR));

 // Test with LF
 assertTrue(lexer.readEndOfLine((int) Constants.LF));

 // Test with CR followed by LF
 assertTrue(lexer.readEndOfLine((int) Constants.CR));
 assertTrue(lexer.readEndOfLine((int) Constants.LF));
 }

 @Test(expected = IOException.class)
 public void testReadEndOfLineWithUnexpectedChar() throws IOException {
 assertFalse(lexer.readEndOfLine((int) 'X'));
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 // Test with CR
 assertTrue(lexer.isStartOfLine((int) Constants.CR));

 // Test with LF
 assertTrue(lexer.isStartOfLine((int) Constants.LF));

 // Test with start of file
 assertTrue(lexer.isStartOfLine((int) Constants.UNDEFINED));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 // Test with CR
 assertFalse(lexer.isEndOfFile((int) Constants.CR));

 // Test with LF
 assertFalse(lexer.isEndOfFile((int) Constants.LF));

 // Test with end of stream
 assertTrue(lexer.isEndOfFile((int) Constants.END_OF_STREAM));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 // Test with delimiter
 assertTrue(lexer.isDelimiter((int) lexer.delimiter));

 // Test with other character
 assertFalse(lexer.isDelimiter((int) 'A'));
 }

 @Test
 public void testIsEscape() throws IOException {
 // Test with escape character
 assertTrue(lexer.isEscape((int) lexer.escape));

 // Test with other character
 assertFalse(lexer.isEscape((int) 'A'));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 // Test with quote character
 assertTrue(lexer.isQuoteChar((int) lexer.quoteChar));

 // Test with other character
 assertFalse(lexer.isQuoteChar((int) 'A'));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 // Test with comment start character
 assertTrue(lexer.isCommentStart((int) lexer.commmentStart));

 // Test with other character
 assertFalse(lexer.isCommentStart((int) 'A'));
 }

 @Test
 public void testNextToken() throws IOException {
 // Initialize test dataset
 String input = "\"Field1\",field2,\"field3\"\n"+
         "comment1#field4\tfield5\tfield6\n"+
         "  field7  \n";
 ExtendedBufferedReader inputStream = new ExtendedBufferedReader(new StringReader(input));
 CSVFormat csvFormat = new CSVFormatBuilder()
         .setDelimiter(',')
         .setEscapeCharacter('\\')
         .setQuoteChar('\"')
         .setCommentStartKey("#")
         .setTrimLeadingWhiteSpace(true)
         .build();
 Lexer testLexer = new Lexer(csvFormat, inputStream);
 Token token = new Token();

 // Test first token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.START_ARRAY, token.getTokenType());
 assertEquals("Field1", token.getValue());

 // Test second token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.STRING, token.getTokenType());
 assertEquals("field2", token.getValue());

 // Test third token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.STRING, token.getTokenType());
 assertEquals("field3", token.getValue());

 // Test comment token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.COMMENT, token.getTokenType());
 assertEquals("comment1", token.getValue());

 // Test fourth token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.STRING, token.getTokenType());
 assertEquals("field4", token.getValue());

 // Test fifth token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.STRING, token.getTokenType());
 assertEquals("field5", token.getValue());

 // Test sixth token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.STRING, token.getTokenType());
 assertEquals("field6", token.getValue());

 // Test empty line
 token = testLexer.nextToken(token);
 assertEquals(TokenType.END_OF_RECORD, token.getTokenType());

 // Test whitespace token
 token = testLexer.nextToken(token);
 assertEquals(TokenType.WHITESPACE, token.getTokenType());
 assertEquals("", token.getValue());

 // Test end of file
 token = testLexer.nextToken(token);
 assertEquals(TokenType.END_OF_STREAM, token.getTokenType());
 }
}