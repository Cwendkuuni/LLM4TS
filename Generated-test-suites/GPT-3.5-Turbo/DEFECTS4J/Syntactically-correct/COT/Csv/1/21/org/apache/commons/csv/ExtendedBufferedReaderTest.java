import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

  ExtendedBufferedReader reader;

  @Test
  public void testRead() {
    // Verify that the read method returns the correct character
    reader = new ExtendedBufferedReader(reader);
    char expected = 'i';
    int actual = reader.read();
    assertEquals(expected , actual);
  }

  @Test
  public void testReadAgain() {
    // Verify that the readAgain method returns the last character read
    reader = new ExtendedBufferedReader(reader);
    reader.read();
    int expected = 'i';
    int actual = reader.readAgain();
    assertEquals(expected , actual);
  }

  @Test
  public void testReadCharArray() {
    // Verify that the read(char[] buf, int offset, int length) method reads the correct characters
    reader = new ExtendedBufferedReader(reader);
    char[] buf = new char[5];
    int offset = 0;
    int length = 5;
    int expectedLen = 5;
    int actualLen = reader.read(buf, offset, length);
    assertEquals(expectedLen , actualLen);
  }

  @Test
  public void testReadLine() {
    // Verify that the readLine method returns the correct line
    reader = new ExtendedBufferedReader(reader);
    String expected = "hello";
    String actual = reader.readLine();
    assertEquals(expected , actual);
  }

  @Test
  public void testLookAhead() {
    // Verify that the lookAhead method returns the correct character
    reader = new ExtendedBufferedReader(reader);
    int expected = 'h';
    int actual = reader.lookAhead();
    assertEquals(expected , actual);
  }

  @Test
  public void testGetLineNumber() {
    // Verify that the getLineNumber method returns the correct line number
    reader = new ExtendedBufferedReader(reader);
    int expected = 1;
    int actual = reader.getLineNumber();
    assertEquals(expected , actual);
  }

}