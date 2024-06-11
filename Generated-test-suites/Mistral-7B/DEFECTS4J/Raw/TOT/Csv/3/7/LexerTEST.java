import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class LexerTest {

 @Mock
 private ExtendedBufferedReader mockReader;

 @Captor
 private ArgumentCaptor<Token> tokenCaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testNextToken_delimiter() throws IOException {
 final String testData = "col1,col2,col3";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader) {
 @Override
 public int readEscape() throws IOException {
 throw new UnsupportedOperationException("This method should not be called in tests.");
 }
 };

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 assertEquals(TokenType.DELIMITER, token.getType());
 assertEquals(',', token.getValue()[0]);
 }

 @Test
 public void testNextToken_delimiter_ignoringTrailingSpaces() throws IOException {
 final String testData = "col1\t ,col2\t ,col3\t";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader) {
 @Override
 public void trimTrailingSpaces(StringBuilder buffer) {
 // do nothing
 }
 };

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 assertEquals(TokenType.DELIMITER, token.getType());
 assertEquals(',', token.getValue()[0]);
 }

 @Test
 public void testNextToken_newline() throws IOException {
 final String testData = "col1\ncol2";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader) {
 @Override
 public boolean readEndOfLine(int c) throws IOException {
 return true;
 }
 };

 mockReader.when(mockReader::readLine).thenReturn(null);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the newline character
 assertNull(token);
 }

 @Test
 public void testNextToken_comment() throws IOException {
 final String testData = "col1#this is a comment\ncol2";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 assertFalse(token.isValueSet());
 assertNull(token.getValue());
 }

 @Test(expected = IOException.class)
 public void testNextToken_error() throws IOException {
 final String testData = "col1,col2,col3";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).thenThrow(new IOException());
 mockReader.when(mockReader::getLastChar()).thenReturn(-1);

 Token token = lexer.nextToken(null);
 verify(mockReader).read(); // read the first character
 verify(mockReader).read(); // read the second character (throws an exception)
 assertNull(token);
 }

 @Test
 public void testNextToken_escape() throws IOException {
 final String testData = "col1,\"col2,col3\"";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 verify(mockReader).read(); // read the second character
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(2, token.getValue().length);
 assertEquals("\",", token.getValue()[0]);
 assertEquals("col2,col3", token.getValue()[1]);
 }

 @Test
 public void testNextToken_quotedValue() throws IOException {
 final String testData = "col1,\"col2,\"col3\"";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 verify(mockReader).read(); // read the second character
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(3, token.getValue().length);
 assertEquals("col2,\"col3\"", token.getValue()[0]);
 assertNull(token.getValue()[1]);
 }

 @Test
 public void testNextToken_emptyString() throws IOException {
 final String testData = "col1,\"\",col3";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 verify(mockReader).read(); // read the second character
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(1, token.getValue().length);
 assertEquals("", token.getValue()[0]);
 }

 @Test
 public void testNextToken_emptyLine() throws IOException {
 final String testData = "\ncol1,col2\n";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader) {
 @Override
 public boolean ignoreEmptyLines() {
 return true;
 }
 };

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader, never()).read(); // don't read the empty line
 assertNull(token);
 }

 @Test
 public void testNextToken_multipleTokens() throws IOException {
 final String testData = "col1,\"col2,col3\",col4";
 final byte[] testDataBytes = testData.getBytes();
 final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testDataBytes);
 final CSVFormat format = new CSVFormat(',', null);
 final Lexer lexer = new Lexer(format, mockReader);

 mockReader.when(mockReader::readLine).thenReturn(testData);
 mockReader.when(mockReader::read).then(i -> testInputStream.read());

 Token token = lexer.nextToken(null);

 verify(mockReader).read(); // read the first character
 assertEquals(TokenType.STRING, token.getType());
 assertEquals(1, token.getValue().length);
 assertEquals("col2,col3", token.getValue()[0]);

 token = lexer.nextToken(token);

 verify(mockReader, never()).read(); // don't read any more characters
 assertEquals(TokenType.DELIMITER, token.getType());
 assertEquals(',', token.getValue()[0]);
 }
}