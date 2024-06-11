import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {
   
   private Base64InputStream inputStream;
   private Base64 base64;

   @Before
   public void setUp() {
      base64 = new Base64();
   }

   @Test
   public void testRead() {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      try {
         int result = inputStream.read();
         assertEquals('H', result);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
   }

   @Test
   public void testReadByteArray() {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[5];
      try {
         int result = inputStream.read(buffer, 0, 5);
         assertEquals(5, result);
         assertArrayEquals("Hello".getBytes(), buffer);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
   }

   @Test
   public void testReadByteArrayWithOffset() {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[10];
      try {
         int result = inputStream.read(buffer, 2, 5);
         assertEquals(5, result);
         assertArrayEquals(new byte[] {0, 0, 'H', 'e', 'l', 'l', 'o', 0, 0, 0}, buffer);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
   }

   @Test
   public void testReadByteArrayWithInvalidOffsetAndLength() {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[10];
      try {
         // Invalid offset
         int result = inputStream.read(buffer, -1, 5);
         assertEquals(-1, result);
         // Invalid length
         result = inputStream.read(buffer, 0, 12);
         assertEquals(-1, result);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
   }

   @Test
   public void testReadByteArrayEmptyBuffer() {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[0];
      try {
         int result = inputStream.read(buffer, 0, 0);
         assertEquals(0, result);
      } catch (IOException e) {
         fail("IOException should not be thrown");
      }
   }

   @Test(expected = NullPointerException.class)
   public void testReadByteArrayNullBuffer() throws IOException {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      inputStream.read(null, 0, 5);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testReadByteArrayInvalidOffset() throws IOException {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[10];
      inputStream.read(buffer, -1, 5);
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testReadByteArrayInvalidLength() throws IOException {
      byte[] testData = base64.encode("Hello World".getBytes());
      ByteArrayInputStream in = new ByteArrayInputStream(testData);
      inputStream = new Base64InputStream(in, false);
      byte[] buffer = new byte[10];
      inputStream.read(buffer, 0, 12);
   }

   @Test
   public void testMarkSupported() {
      ByteArrayInputStream in = new ByteArrayInputStream("Hello World".getBytes());
      inputStream = new Base64InputStream(in, false);
      assertFalse(inputStream.markSupported());
   }

}