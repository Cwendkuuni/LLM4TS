import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader mockReader;
 private Lexer lexer;

 @Before
 public void setup() throws IOException {
 format = new CSVFormatBuilder().setDelimiter(',').build();
 mockReader = new ExtendedBufferedReader(new MockInputStreamReader("Test Input"));
 lexer = new Lexer(format, mockReader);
 }

 @Test
 public void testNextToken_simple_token() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.STRING, token.getType());
 assertEquals("Test", token.getValue());
 }

 @Test
 public void testNextToken_empty_line() throws IOException {
 Token token = lexer.nextToken(null);
 assertNull(token.getValue());
 assertTrue(lexer.isEndOfFile(token.getPosition()));
 }

 @Test
 public void testNextToken_comment() throws IOException {
 Token token = lexer.nextToken(null);
 assertEquals(TokenType.COMMENT, token.getType());
 assertEquals("# This is a comment", token.getValue());
 }

 @Test
 public void testNextToken_escaped_char() throws IOException {
 // Given
 mockReader.setExpectedChar('\n', 1);
 mockReader.setExpectedChar('\\', 2);
 mockReader.setExpectedChar('r', 3);
 mockReader.setExpectedChar('\n', 4);
 // When
 Token token = lexer.nextToken(null);
 // Then
 assertEquals(TokenType.STRING, token.getType());
 assertEquals("\r\n", token.getValue());
 }

 @Test(expected = IOException.class)
 public void testNextToken_end_of_stream() throws IOException {
 Token token = lexer.nextToken(null);
 fail("Expected IOException when reaching end of stream.");
 }

 @Test
 public void testNextToken_quoted_token() throws IOException {
 // Given
 mockReader.setExpectedChars("\"Hello, world!\"", 0);
 // When
 Token token = lexer.nextToken(null);
 // Then
 assertEquals(TokenType.STRING, token.getType());
 assertEquals("Hello, world!", token.getValue());
 }

 @Test
 public void testNextToken_quoted_token_with_escapes() throws IOException {
 // Given
 mockReader.setExpectedChars("\"Hello,\\ world!\"", 0);
 // When
 Token token = lexer.nextToken(null);
 // Then
 assertEquals(TokenType.STRING, token.getType());
 assertEquals("Hello,\\ world!", token.getValue());
 }

 @Test
 public void testNextToken_quoted_token_trailing_escapes() throws IOException {
 // Given
 mockReader.setExpectedChars("\"Hello,\\ world\\\"", 0);
 // When
 Token token = lexer.nextToken(null);
 // Then
 assertEquals(TokenType.STRING, token.getType());
 assertEquals("Hello,\\ world\"", token.getValue());
 }
}