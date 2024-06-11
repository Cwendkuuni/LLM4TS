import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
  
  private BaseNCodecInputStream stream;
  
  @Before
  public void setUp() {
    stream = new BaseNCodecInputStream(null, null, true);
  }
  
  @Test
  public void testRead() {
    try {
      int result = stream.read();
      assertEquals("Result", -1, result);
    } catch (IOException e) {
      fail("IOException caught");
    }
  }
  
  @Test
  public void testReadByteArrayIntInt() {
    try {
      byte[] b = new byte[10];
      int result = stream.read(b, 0, 10);
      assertEquals("Result", 0, result);
    } catch (IOException e) {
      fail("IOException caught");
    }
  }
  
  @Test
  public void testMarkSupported() {
    assertFalse("Mark supported", stream.markSupported());
  }
  
  @Test
  public void testAvailable() {
    int result = stream.available();
    assertEquals("Result", 1, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testSkipNegativeLength() {
    stream.skip(-1);
  }
  
  @Test
  public void testSkip() {
    try {
      long result = stream.skip(100);
      assertEquals("Result", 0L, result);
    } catch (IOException e) {
      fail("IOException caught");
    }
  }

}