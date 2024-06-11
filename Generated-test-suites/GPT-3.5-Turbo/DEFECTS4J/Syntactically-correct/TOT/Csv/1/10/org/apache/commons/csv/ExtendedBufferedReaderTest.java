import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
  
  private ExtendedBufferedReader reader;

  @Before
  public void setUp() {
    reader = new ExtendedBufferedReader(new StringReader("abc\r\ndef\nghi"));
  }

  @Test
  public void testRead() throws IOException {
    assertEquals('a', reader.read());
    assertEquals('b', reader.read());
    assertEquals('c', reader.read());
    assertEquals('\r', reader.read());
    assertEquals('\n', reader.read());
    assertEquals('d', reader.read());
    assertEquals('e', reader.read());
    assertEquals('f', reader.read());
    assertEquals('\n', reader.read());
    assertEquals('g', reader.read());
    assertEquals('h', reader.read());
    assertEquals('i', reader.read());
    assertEquals(-1, reader.read());
  }

  @Test
  public void testReadAgain() throws IOException {
    reader.read();
    assertEquals('a', reader.readAgain());
    reader.read();
    assertEquals('b', reader.readAgain());
    reader.read();
    assertEquals('c', reader.readAgain());
    reader.read();
    assertEquals('\r', reader.readAgain());
    reader.read();
    assertEquals('\n', reader.readAgain());
    reader.read();
    assertEquals('d', reader.readAgain());
  }

  @Test
  public void testReadCharArray() throws IOException {
    char[] buffer = new char[4];
    assertEquals(4, reader.read(buffer, 0, 4));
    assertEquals('a', buffer[0]);
    assertEquals('b', buffer[1]);
    assertEquals('c', buffer[2]);
    assertEquals('\r', buffer[3]);

    assertEquals(2, reader.read(buffer, 0, 4));
    assertEquals('\n', buffer[0]);
    assertEquals('d', buffer[1]);
    assertEquals('e', buffer[2]);
    assertEquals('f', buffer[3]);

    assertEquals(3, reader.read(buffer, 0, 4));
    assertEquals('\n', buffer[0]);
    assertEquals('g', buffer[1]);
    assertEquals('h', buffer[2]);
    assertEquals('i', buffer[3]);

    assertEquals(-1, reader.read(buffer, 0, 4));
  }

  @Test
  public void testReadLine() throws IOException {
    assertEquals("abc", reader.readLine());
    assertEquals("def", reader.readLine());
    assertEquals("ghi", reader.readLine());
  }

  @Test
  public void testLookAhead() throws IOException {
    assertEquals('a', reader.lookAhead());
    assertEquals('a', reader.lookAhead());
    reader.read();
    assertEquals('b', reader.lookAhead());
  }

  @Test
  public void testGetLineNumber() throws IOException {
    assertEquals(0, reader.getLineNumber());
    reader.readLine();
    assertEquals(1, reader.getLineNumber());
    reader.read();
    assertEquals(1, reader.getLineNumber());
    reader.readLine();
    assertEquals(2, reader.getLineNumber());
    reader.readLine();
    assertEquals(3, reader.getLineNumber());
  }

  @Test
  public void testReadEmpty() throws IOException {
    reader = new ExtendedBufferedReader(new StringReader(""));
    assertEquals(-1, reader.read());
    assertEquals(-1, reader.readAgain());
    char[] buffer = new char[4];
    assertEquals(0, reader.read(buffer, 0, 4));
    assertEquals(-1, reader.lookAhead());
    assertEquals(0, reader.getLineNumber());
    assertNull(reader.readLine());
  }
}