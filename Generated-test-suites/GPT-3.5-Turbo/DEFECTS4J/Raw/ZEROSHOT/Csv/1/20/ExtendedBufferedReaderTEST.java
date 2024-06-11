import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

  private ExtendedBufferedReader reader;

  @Before
  public void setUp() {
    reader = new ExtendedBufferedReader(new FileReader("test.txt"));
  }

  @Test
  public void testRead() throws IOException {
    int c = reader.read();
    assertEquals('t', (char) c);
  }

  @Test
  public void testReadAgain() throws IOException {
    reader.read();
    int c = reader.readAgain();
    assertEquals('t', (char) c);
  }

  @Test
  public void testReadWithBuffer() throws IOException {
    char[] buf = new char[10];
    int len = reader.read(buf, 0, buf.length);
    assertEquals(10, len);
  }

  @Test
  public void testReadWithBufferPartial() throws IOException {
    char[] buf = new char[10];
    int len = reader.read(buf, 0, buf.length);
    assertEquals(10, len);
    String str = new String(buf);
    assertEquals("test file ", str);
  }

  @Test
  public void testReadLine() throws IOException {
    String line = reader.readLine();
    assertEquals("test file line 1", line);
  }

  @Test
  public void testLookAhead() throws IOException {
    int c = reader.lookAhead();
    assertEquals('t', (char) c);
  }

  @Test
  public void testGetLineNumber() throws IOException {
    int line = reader.getLineNumber();
    assertEquals(0, line);
  }
}