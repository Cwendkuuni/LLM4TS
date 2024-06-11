import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

   private Base64InputStream base64InputStream;

   @Before
   public void setUp() {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Hello, World!".getBytes());
      base64InputStream = new Base64InputStream(byteArrayInputStream);
   }

   @Test
   public void testRead() throws IOException {
      int result = base64InputStream.read();
      assertEquals(72, result);
   }

   @Test
   public void testReadWithNegativeResult() throws IOException {
      int result = base64InputStream.read();
      assertFalse(result < 0);
   }

   @Test
   public void testReadWithEmptyByteArray() throws IOException {
      byte[] byteArray = new byte[0];
      int result = base64InputStream.read(byteArray, 0, 0);
      assertEquals(0, result);
   }

   @Test(expected = NullPointerException.class)
   public void testReadWithNullByteArray() throws IOException {
      base64InputStream.read(null, 0, 1);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testReadWithNegativeOffset() throws IOException {
      byte[] byteArray = new byte[1];
      base64InputStream.read(byteArray, -1, 1);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testReadWithInvalidOffset() throws IOException {
      byte[] byteArray = new byte[1];
      base64InputStream.read(byteArray, 2, 1);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testReadWithInvalidBufferSize() throws IOException {
      byte[] byteArray = new byte[1];
      base64InputStream.read(byteArray, 0, 2);
   }

   @Test
   public void testReadWithBase64Decoding() throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("SGVsbG8sIFdvcmxkIQ==".getBytes());
      Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, false);
      byte[] byteArray = new byte[13];
      int result = base64InputStream.read(byteArray, 0, 13);
      assertEquals(12, result);
      assertEquals("Hello, World!", new String(byteArray));
   }

   @Test
   public void testReadWithBase64Encoding() throws IOException {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Hello, World!".getBytes());
      Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, true);
      byte[] byteArray = new byte[30];
      int result = base64InputStream.read(byteArray, 0, 30);
      assertEquals(17, result);
      assertEquals("SGVsbG8sIFdvcmxkIQ==", new String(byteArray));
   }
}