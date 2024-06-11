import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Assert;
import org.junit.Test;

public class CodersTest {

 private static final byte[] TEST_PASSWORD = "testPassword".getBytes();

 @Test
 public void testAddDecoder_copy() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/copy.7z");
 Coder coder = new CopyDecoder();
 InputStream result = Coders.addDecoder(inputStream, coder, null);
 byte[] buffer = new byte[1024];
 int length = result.read(buffer);
 Assert.assertEquals(length, 1024);
 result.close();
 inputStream.close();
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_unsupportedMethod() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/copy.7z");
 Coder coder = new Object() {}.getClass().getDeclaredConstructor().newInstance();
 InputStream result = Coders.addDecoder(inputStream, coder, null);
 result.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoder_lzma() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/lzma.7z");
 Coder coder = new LZMADecoder();
 InputStream result = Coders.addDecoder(inputStream, coder, null);
 byte[] buffer = new byte[1024];
 int length = result.read(buffer);
 Assert.assertEquals(length, 1024);
 result.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoder_deflate() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/deflate.7z");
 Coder coder = new DeflateDecoder();
 InputStream result = Coders.addDecoder(inputStream, coder, null);
 byte[] buffer = new byte[1024];
 int length = result.read(buffer);
 Assert.assertEquals(length, 1024);
 result.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoder_bzip2() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/bzip2.7z");
 Coder coder = new BZIP2Decoder();
 InputStream result = Coders.addDecoder(inputStream, coder, null);
 byte[] buffer = new byte[1024];
 int length = result.read(buffer);
 Assert.assertEquals(length, 1024);
 result.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoder_aes256sha256() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/aes256sha256.7z");
 Coder coder = new AES256SHA256Decoder();
 InputStream result = Coders.addDecoder(inputStream, coder, TEST_PASSWORD);
 byte[] buffer = new byte[1024];
 int length = result.read(buffer);
 Assert.assertEquals(length, 1024);
 result.close();
 inputStream.close();
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_aes256sha256_wrongPassword() throws IOException {
 InputStream inputStream = Coders.class.getResourceAsStream("/testData/aes256sha256.7z");
 Coder coder = new AES256SHA256Decoder();
 InputStream result = Coders.addDecoder(inputStream, coder, new byte[1]);
 result.close();
 inputStream.close();
 }

 @Test
 public void testAddEncoder_copy() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/copy.7z");
 Coder coder = new CopyEncoder();
 Coders.addEncoder(outputStream, SevenZMethod.COPY, null);
 outputStream.write(1024 * 1024); // write 1MB data to test writing
 outputStream.close();
 }

 @Test(expected = IOException.class)
 public void testAddEncoder_unsupportedMethod() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/copy.7z");
 Coder coder = new Object() {}.getClass().getDeclaredConstructor().newInstance();
 Coders.addEncoder(outputStream, SevenZMethod.LZMA, null);
 }

 @Test
 public void testAddEncoder_lzma() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/lzma.7z");Coder coder = new LZMAEncoder();
 Coders.addEncoder(outputStream, SevenZMethod.LZMA, null);
 outputStream.write(1024 * 1024); // write 1MB data to test writing
 outputStream.close();
 }

 @Test
 public void testAddEncoder_deflate() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/deflate.7z");
 Coder coder = new DeflateEncoder();
 Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, null);
 outputStream.write(1024 * 1024); // write 1MB data to test writing
 outputStream.close();
 }

 @Test
 public void testAddEncoder_bzip2() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/bzip2.7z");
 Coder coder = new BZIP2Encoder();
 Coders.addEncoder(outputStream, SevenZMethod.BZIP2, null);
 outputStream.write(1024 * 1024); // write 1MB data to test writing
 outputStream.close();
 }

 @Test
 public void testAddEncoder_aes256sha256() throws IOException {
 OutputStream outputStream = Coders.class.getOutputStream("/testOutput/aes256sha256.7z");
 Coder coder = new AES256SHA256Encoder();
 Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, TEST_PASSWORD);
 outputStream.write(1024 * 1024); // write 1MB data to test writing
 outputStream.close();
 }
}