import org.junit.Assert;
import org.junit.Test;

public class Base64InputStreamTest {

   // Test public methods in Base64InputStream

   // Test read() method
   @Test
   public void testRead() {
       // Create a ByteArrayInputStream for testing
       byte[] bytes = {65, 66, 67, 68, 69};
       ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

       // Create a Base64InputStream with the ByteArrayInputStream
       Base64InputStream base64InputStream = new Base64InputStream(inputStream);

       try {
           // Test reading individual bytes from the stream
           Assert.assertEquals(65, base64InputStream.read());
           Assert.assertEquals(66, base64InputStream.read());
           Assert.assertEquals(67, base64InputStream.read());
           Assert.assertEquals(68, base64InputStream.read());
           Assert.assertEquals(69, base64InputStream.read());

           // Test EOF
           Assert.assertEquals(-1, base64InputStream.read());

       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   // Test read(byte[] b, int offset, int len) method
   @Test
   public void testReadArrayOffsetLen() {
       // Create a ByteArrayInputStream for testing
       byte[] bytes = {65, 66, 67, 68, 69};
       ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

       // Create a Base64InputStream with the ByteArrayInputStream
       Base64InputStream base64InputStream = new Base64InputStream(inputStream);

       try {
           // Create a byte array for reading from the stream
           byte[] buffer = new byte[5];

           // Test reading bytes into the buffer
           int numBytesRead = base64InputStream.read(buffer, 0, 5);

           // Verify the number of bytes read
           Assert.assertEquals(5, numBytesRead);

           // Verify the contents of the buffer
           Assert.assertArrayEquals(bytes, buffer);

           // Test EOF
           Assert.assertEquals(-1, base64InputStream.read(buffer, 0, 5));

       } catch (IOException e) {
           e.printStackTrace();
       }
   }

   // Test markSupported() method
   @Test
   public void testMarkSupported() {
       // Create a ByteArrayInputStream for testing
       byte[] bytes = {65, 66, 67, 68, 69};
       ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

       // Create a Base64InputStream with the ByteArrayInputStream
       Base64InputStream base64InputStream = new Base64InputStream(inputStream);

       // Verify that mark is not supported
       Assert.assertFalse(base64InputStream.markSupported());
   }

}