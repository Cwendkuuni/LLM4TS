import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

 private CSVFormat format;
 private ExtendedBufferedReader reader;
 private Lexer lexer;

 @Before
 public void setUp() throws Exception {
 String testData = "Field1,\"Field2\",Field3\n" +
 "Field1,\"Field2 with quotes\",Field3\n" +
 "Field1,\"Field2 with quotes and escape \\ in it\",Field3\n" +
 "Field1,Field2,Field3\n"; // no quotes or escapes

 String testFilePath = "src/test/resources/LexerTestData.csv";
 format = new CSVFormat(',', '\u001F'); // use a non-standard delimiter for testing
 reader = new ExtendedBufferedReader(new StringReader(testData));
 lexer = new Lexer(format, reader);
 }

 @After
 public void tearDown() throws Exception {
 reader.close();
 }

 @Test
 public void testIsStartOfLine() throws IOException {
 // Testing isStartOfLine method
 int c = reader.read();
 // First character in the file is at the start of a line
 assertTrue(lexer.isStartOfLine(c));
 }

 @Test
 public void testIsEndOfFile() throws IOException {
 // Testing isEndOfFile method
 int c = reader.read();
 while (c != Constants.END_OF_STREAM) {
 c = reader.read();
 }
 // Last character is the end of file
 assertTrue(lexer.isEndOfFile(c));
 }

 @Test
 public void testIsDelimiter() throws IOException {
 // Testing isDelimiter method
 int c = reader.read();
 while (!lexer.isEndOfFile(c) && c != format.getDelimiter()) {
 c = reader.read();
 }
 // The first comma is a delimiter
 assertTrue(lexer.isDelimiter(c));
 }

 @Test
 public void testIsEscape() throws IOException {
 // Testing isEscape method
 int c = reader.read();
 while (!lexer.isEndOfFile(c) && !lexer.isDelimiter(c) && !lexer.isQuoteChar(c) && c != Constants.CR && c != Constants.LF) {
 c = reader.read();
 }
 // The first backslash is an escape character
 assertTrue(lexer.isEscape(c));
 }

 @Test
 public void testIsQuoteChar() throws IOException {
 // Testing isQuoteChar method
 int c = reader.read();
 while (!lexer.isEndOfFile(c) && !lexer.isDelimiter(c) && !lexer.isQuoteChar(c) && !lexer.isEndOfLine(c)) {
 c = reader.read();
 }
 // The first double quote is a quote character
 assertTrue(lexer.isQuoteChar(c));
 }

 @Test
 public void testIsCommentStart() throws IOException {
 // Testing isCommentStart method
 int c = reader.read();
 while (!lexer.isEndOfFile(c) && !lexer.isDelimiter(c) && !lexer.isQuoteChar(c) && !lexer.isCommentStart(c) && !lexer.isEndOfLine(c)) {
 c = reader.read();
 }
 // The first comment start character is a comment start
 assertTrue(lexer.isCommentStart(c));
 }

 @Test
 public void testNextToken() throws IOException {
 // Testing nextToken method
 StringBuilder buffer = new StringBuilder();
 Token token = new Token();
 int c;

 // Test empty line
 c = reader.read();
 assertTrue(lexer.isEndOfLine(c));
 token = lexer.nextToken(token);
 assertEquals("", token.getString());
 assertEquals(Constants.EMPTY_STRING, token.getRawValue());
 assertEquals(Constants.TOKEN_TYPE_EMPTY, token.getType());

 // Test empty string
 c = reader.read();
 assertEquals(format.getDelimiter(), c); // Delimiter separates empty strings
 token = lexer.nextToken(token);
 assertEquals("", token.getString());
 assertEquals("", token.getRawValue());
 assertEquals(Constants.TOKEN_TYPE_STRING, token.getType());

 // Test string with no quote characters
 c = reader.read();
 assertEquals(format.getDelimiter(), c); // Delimiter separates string
 buffer.setLength(0);
 while (!lexer.isEndOfFile(c) && !lexer.isDelimiter(c) && !lexer.isQuoteChar(c) && !lexer.isEndOfLine(c)) {
 buffer.append((char) c);
 c = reader.read();
 }
 token = lexer.nextToken(token);
 assertEquals("Field1", token.getString());
 assertEquals(buffer.toString(), token.getRawValue());
 assertEquals(Constants.TOKEN_TYPE_STRING, token.getType());

 // Test string with escaped quote character
 buffer.setLength(0);
 c = reader.read();
 assertEquals(format.getDelimiter(), c); // Delimiter separates string
 c = reader.read(); // Read the backslash
 assertTrue(lexer.isEscape(c));
 c = reader.read(); // Read the quote character
 assertTrue(lexer.isQuoteChar(c));
 token = lexer.nextToken(token);
 assertEquals("Field2 with quotes", token.getString());
 assertEquals(buffer.append(c).toString(), token.getRawValue());
 assertEquals(Constants.TOKEN_TYPE_STRING, token.getType());

 // Test string with escaped escape character
 buffer.setLength(0);
 c = reader.read();
 assertEquals(format.getDelimiter(), c); // Delimiter separates string
 c = reader.read(); // Read the backslash
 assertTrue(lexer.isEscape(c));
 c = reader.read(); // Read the backslash
 assertTrue(lexer.isEscape(c));
 token = lexer.nextToken(token);
 assertEquals("Field2 with quotes and escape \\ in it", token.getString());
 assertEquals(buffer.append(c).toString(), token.getRawValue());
 assertEquals(Constants.TOKEN_TYPE_STRING, token.getType());
 }
}