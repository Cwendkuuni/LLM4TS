
import static org.junit.Assert.*;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

  @Test
  public void testConstructor() {
    InputStream inputStream = null;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);
    assertNotNull(dci);
  }

  @Test
  public void testRead() {
    InputStream inputStream = null;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);
    assertEquals(0, dci.read());
  }

  @Test
  public void testReadWithByteArray() {
    InputStream inputStream = null;
    byte[] buf = new byte[10];
    int off = 0;
    int len = 5;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);
    assertEquals(0, dci.read(buf, off, len));
  }

  @Test
  public void testSkip() {
    InputStream inputStream = null;
    long n = 5;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);
    assertEquals(0, dci.skip(n));
  }

  @Test
  public void testAvailable() {
    InputStream inputStream = null;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);;
    assertEquals(0, dci.available());
  }

  @Test
  public void testClose() {
    InputStream inputStream = null;
    DeflateCompressorInputStream dci = new DeflateCompressorInputStream(inputStream);
    dci.close();
  }
}
