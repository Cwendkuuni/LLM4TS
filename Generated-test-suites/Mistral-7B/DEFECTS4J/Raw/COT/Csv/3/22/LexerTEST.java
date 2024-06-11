import org.apache.commons.csv.*;
import org.apache.commons.csv.Constants.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private Lexer lexer;
 private ExtendedBufferedReader mockInput;
 private StringBuilder buffer;

 @Before
 public void setUp() {
 mockInput = new ExtendedBufferedReader(new StringReader(""));
 buffer = new StringBuilder();
 lexer = new Lexer(new CSVFormat(new CSVParserDefaultConfig.Builder().build()), mockInput);
 lexer.in = mockInput;
 }

 @Test
 public void testGetLineNumber() throws IOException {
 // Test initial line number
 assertEquals(1, lexer.getLineNumber());

 // Test reading a line
 mockInput.readLine();
 mockInput.readLine();
 assertEquals(3, lexer.getLineNumber());
 }

 @Test(expected = IOException.class)
 public void testReadEscapeWithInvalidChar() throws IOException {
 lexer.readEscape();
 fail("Expected IOException when reading invalid escape character");
 }

 @Test
 public void testReadEscapeWithValidChars() throws IOException {
 int c;
 for (char escapeChar : new char[]{'r', 'n', 't', 'b', 'f'}) {
 c = lexer.readEscape();
 assertEquals(c, readEscapeCharacter(escapeChar));
 }
 }

 @Test
 public void testTrimTrailingSpaces() throws IOException {
 StringBuilder initialBuffer = new StringBuilder(" a b c \t d \t e f g\t ");
 lexer.trimTrailingSpaces(initialBuffer);
 assertEquals(" a b c d e f g", initialBuffer.toString());
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 int c;
 // Test CR
 c = CR;
 assertTrue(lexer.readEndOfLine(c));
 // Test LF
 c = LF;
 assertTrue(lexer.readEndOfLine(c));
 // Test CR followed by LF
 mockInput.read();
 assertTrue(lexer.readEndOfLine(CR));
 assertTrue(lexer.readEndOfLine(LF));
 // Test invalid character
 c = '\u0001';
 assertFalse(lexer.readEndOfLine(c));
 }

 @Test
 public void testNextToken() throws IOException {
 Token token = new Token();
 nextTokenTestHelper(token, "abcd", "a", TokenType.STRING);
 nextTokenTestHelper(token, "abcd\t", "a", TokenType.STRING);
 nextTokenTestHelper(token, "abcd\r", "a", TokenType.STRING);
 nextTokenTestHelper(token, "abcd\n", "a", TokenType.STRING);
 nextTokenTestHelper(token, "ab\tcd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\rcd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\ncd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "\tabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "\rabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "\nabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\r\ncd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\t\r cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\t\n cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\r\t cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\n\t cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "abcd\r\n", "abcd", TokenType.STRING);
 nextTokenTestHelper(token, "\nabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "\rabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "\nabcd", "a", TokenType.STRING, "bcd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\r\ncd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\t\r cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\t\n cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\r\t cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "ab\n\t cd", "ab", TokenType.STRING, "cd", TokenType.STRING);
 nextTokenTestHelper(token, "abcd\r\n", "abcd", TokenType.STRING);
 nextTokenTestHelper(token, "\nabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "\rabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "\nabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\r\ncd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\t\r cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\t\n cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\r\t cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\n\t cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "abcd\r\n", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "\nabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "\rabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "\nabcd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\r\ncd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\t\r cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\t\n cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\r\t cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "ab\n\t cd", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "abcd\r\n", null, TokenType.COMMENT);
 nextTokenTestHelper(token, "", null, TokenType.EMPTY_FIELD);
 nextTokenTestHelper(token, " ", null, TokenType.EMPTY_FIELD);
 nextTokenTestHelper(token, "\t", null, TokenType.EMPTY_FIELD);
 nextTokenTestHelper(token, "\r", null, TokenType.EMPTY_FIELD);
 nextTokenTestHelper(token, "\n", null, TokenType.EMPTY_FIELD);
 nextTokenTestHelper(token, "\n", null, TokenType.EOF);
 }

 private void nextTokenTestHelper(Token token, String input, String expectedTokenValue, TokenType... expectedTokenTypes)
 throws IOException {
 lexer.in = new ExtendedBufferedReader(new StringReader(input));
 lexer.nextToken(token);
 assertEquals(expectedTokenValue, token.getValue());
 assertEquals(expectedTokenTypes.length, token.getTypes().length);
 for (int i = 0; i < expectedTokenTypes.length; i++) {
 assertEquals(expectedTokenTypes[i], token.getTypes()[i]);
 }
 }

 private int readEscapeCharacter(char escapeChar) {
 switch (escapeChar) {
 case 'r':
 return CR;
 case 'n':
 return LF;
 case 't':
 return TAB;
 case 'b':
 return BACKSPACE;
 case 'f':
 return FF;
 default:
 throw new IllegalArgumentException("Invalid escape character: " + escapeChar);
 }
 }
}