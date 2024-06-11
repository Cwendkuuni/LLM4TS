import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {
  
  private ExtendedBufferedReader reader;
  
  @Before
  public void setup() {
    reader = new ExtendedBufferedReader(mockReader);
  }
  
  @Test
  public void testRead() {
    try {
      int result = reader.read();
      assertEquals(-2, result);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testReadAgain() {
    try {
      int result = reader.read();
      result = reader.readAgain();
      assertEquals(-2, result);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testReadChar() {
    try {
      char[] buf = new char[10];
      int result = reader.read(buf, 0, 5);
      assertEquals(5, result);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testReadLine() {
    try {
      String result = reader.readLine();
      assertNull(result);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testLookAhead() {
    try {
      int result = reader.lookAhead();
      assertEquals(0, result);
    } catch (IOException e) {
      fail("Unexpected IOException");
    }
  }
  
  @Test
  public void testGetLineNumber() {
    int result = reader.getLineNumber();
    assertEquals(0, result);
  }
}