
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

  @Test
  public void testRead() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    int result = base64Input.read();
    assertEquals('H', result);
  }

  @Test
  public void testReadWithOffsetAndLength() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    byte[] buffer = new byte[5];
    int result = base64Input.read(buffer, 1, 4);
    assertEquals(4, result);
    assertArrayEquals(new byte[] {0, 'H', 'e', 'l', 'l'}, buffer);
  }

  @Test(expected = NullPointerException.class)
  public void testReadWithNullBuffer() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    base64Input.read(null, 0, 5);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithNegativeOffset() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    byte[] buffer = new byte[5];
    base64Input.read(buffer, -1, 4);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testReadWithInvalidLength() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    byte[] buffer = new byte[5];
    base64Input.read(buffer, 0, 6);
  }

  @Test
  public void testMarkSupported() throws Exception {
    InputStream input = new ByteArrayInputStream("Hello World".getBytes());
    Base64InputStream base64Input = new Base64InputStream(input);
    assertFalse(base64Input.markSupported());
  }
}
