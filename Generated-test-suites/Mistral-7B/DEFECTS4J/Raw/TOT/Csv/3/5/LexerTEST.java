import org.apache.commons.csv.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LexerTest {

 private static final String TEST_FILE = "test.csv";
 private CSVFormat format;
 private ExtendedBufferedReader reader;
 private Lexer lexer;

 @BeforeAll
 public void setUp() throws Exception {
 format = new CSVFormatBuilder()
 .setIgnoreSurroundingSpaces(true)
 .setIgnoreEmptyLines(true)
 .build();
 reader = new FileReader(TEST_FILE);
 lexer = new Lexer(format, reader);
 }

 @AfterAll
 public void tearDown() throws Exception {
 reader.close();
 }

 @Test
 public void testNextToken_ValidCharacters() throws IOException {
 Token reusableToken = new Token();
 for (int i = 0; i < 256; i++) {
 int nextChar = lexer.read();
 int tokenType = lexer.nextToken(reusableToken).getType();
 assertEquals(tokenType, (isWhitespace(i) ? TokenType.WHITESPACE : isStartOfLine(i) ? TokenType.START_LINE : isDelimiter(i) ? TokenType.DELIMITER : isQuoteChar(i) ? TokenType.QUOTE : isCommentStart(i) ? TokenType.COMMENT : isEscape(i) ? TokenType.ESCAPE : (char) i == lexer.delimiter ? TokenType.DELIMITER : TokenType.TEXT));
 }
 }

 @Test
 public void testNextToken_EndOfFile() throws IOException {
 Token reusableToken = new Token();
 int nextChar = lexer.read();
 assertFalse(lexer.isStartOfLine(nextChar));
 assertFalse(lexer.isDelimiter(nextChar));
 assertFalse(lexer.isQuoteChar(nextChar));
 assertFalse(lexer.isCommentStart(nextChar));
 assertFalse(lexer.isEscape(nextChar));
 assertFalse(lexer.isWhitespace(nextChar));
 assertEquals(TokenType.END_OF_STREAM, lexer.nextToken(reusableToken).getType());
 }

 @Test
 public void testNextToken_InvalidEscapeSequence() throws IOException {
 Token reusableToken = new Token();
 int nextChar = lexer.read();
 int escapeChar = lexer.escape;
 int invalidEscape = (char) (escapeChar + 1);
 lexer.read(); // consume escape character
 assertNotEquals(TokenType.ESCAPE, lexer.nextToken(reusableToken).getType());
 lexer.in.reset(); // reset the reader for next test
 lexer.read(); // consume the character before the invalid escape sequence
 assertFalse(lexer.isEndOfFile());
 assertFalse(lexer.isStartOfLine());
 assertFalse(lexer.isDelimiter());
 assertFalse(lexer.isQuoteChar());
 assertFalse(lexer.isCommentStart());
 assertFalse(lexer.isEscape());
 assertFalse(lexer.isWhitespace((char) lexer.read()));
 assertNotEquals(TokenType.ESCAPE, lexer.nextToken(reusableToken).getType());
 }

 @Test
 public void testNextToken_Commment() throws IOException {
 Token reusableToken = new Token();
 int nextChar = lexer.read();
 int commentStart = lexer.commmentStart;
 int commentEnd = lexer.delimiter;
 lexer.read(); // consume comment start
 assertFalse(lexer.isEndOfFile());
 assertFalse(lexer.isStartOfLine());
 assertFalse(lexer.isDelimiter());
 assertFalse(lexer.isQuoteChar());
 assertFalse(lexer.isCommentStart());
 assertFalse(lexer.isEscape());
 assertFalse(lexer.isWhitespace((char) lexer.read()));
 int commentLength = 0;
 while (!isEndOfFile(nextChar) && nextChar != commentEnd) {
 reusableToken.setValue(reusableToken.getValue() + (char) nextChar);
 nextChar = lexer.read();
 commentLength++;
 }
 assertEquals(TokenType.COMMENT, lexer.nextToken(reusableToken).getType());
 assertEquals(commentLength, reusableToken.getLength());
 }
}