import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.BufferiedReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

 private static final char LF = '\n';
 private static final char CR = '\r';
 private static final char EOS = ExtendedBufferedReader.END_OF_STREAM;
 private static final int UNDEFINED = ExtendedBufferedReader.UNDEFINED;

 @Test
 public void testConstructor() {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new BufferiedReader(new StringReader("")));
 assertTrue(reader instanceof ExtendedBufferedReader);
 }

 @Test
 public void testRead() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
 int c;

 for (int i = 0; i < 3; i++) {
 c = reader.read();
 assertEquals(Character.toString(i + 'a').charAt(0), String.valueOf((char) c));
 }

 c = reader.read();
 assertEquals(LF, (char) c);
 }

 @Test
 public void testReadAgain() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
 int c;

 for (int i = 0; i < 3; i++) {
 c = reader.read();
 assertEquals(Character.toString(i + 'a').charAt(0), String.valueOf((char) reader.readAgain()));
 }

 c = reader.read();
 assertEquals(LF, (char) reader.readAgain());
 }

 @Test
 public void testReadLine() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\n"));
 String line;

 line = reader.readLine();
 assertEquals("abc", line);
 line = reader.readLine();
 assertNull(line);
 }

 @Test
 public void testLookAhead() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
 int c;

 c = reader.lookAhead();
 assertEquals(LF, c);
 c = reader.read();
 assertEquals(LF, (char) c);
 }

 @Test(expected = IOException.class)
 public void testLookAheadWithNoMark() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
 int c = reader.lookAhead();
 }

 @Test
 public void testGetLineNumber() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));
 int lineNumber = reader.getLineNumber();
 assertEquals(3, lineNumber);
 }

 @Test
 public void testReadWithEmptyBuffer() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
 int c = reader.read();
 assertEquals(EOS, c);
 }

 @Test
 public void testReadWithTrailingCR() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\rhello"));
 String line = reader.readLine();
 assertEquals("hello", line);
 }

 @Test
 public void testReadWithTrailingLF() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\n"));
 String line = reader.readLine();
 assertEquals("hello", line);
 }

 @Test
 public void testReadWithBothCRandLF() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r\nhello"));
 String line = reader.readLine();
 assertEquals("hello", line);
 }

 @Test
 public void testReadWithOnlyCR() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r"));
 assertEquals(CR, reader.read());
 assertEquals(EOS, reader.read());
 }

 @Test
 public void testReadWithOnlyLF() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
 assertEquals(LF, reader.read());
 assertEquals(LF, reader.read());
 assertEquals(EOS, reader.read());
 }

 @Test
 public void testReadWithEmptyLine() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r\n"));
 String line = reader.readLine();
 assertEquals("", line);
 }

 @Test
 public void testReadWithEmptyLineEndedByCR() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\r"));
 String line = reader.readLine();
 assertEquals("", line);
 assertEquals(CR, reader.readAgain());
 }

 @Test
 public void testReadWithComment() throws IOException {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("//comment\n"));
 String line = reader.readLine();
 assertNull(line);
 }
}