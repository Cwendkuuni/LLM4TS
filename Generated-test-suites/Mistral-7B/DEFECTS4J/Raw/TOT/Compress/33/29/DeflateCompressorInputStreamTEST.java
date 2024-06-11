import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {

 @Test
 public void testDecompressEmptyFile() throws IOException {
 InputStream inputStream = IOUtils.toInputStream("", "UTF-8"); // Empty file
 DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
 byte[] buffer = new byte[512];
 int bytesRead = decompressor.read(buffer, 0, buffer.length);
 assertEquals(-1, bytesRead); // End of stream reached
 decompressor.close();
 }

 @Test
 public void testDecompressValidFile() throws IOException {
 InputStream inputStream = IOUtils.toInputStream("MjkyZW5ndGggS2V5", "BASE64"); // Lorem Ipsum
 DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
 byte[] buffer = new byte[512];
 int bytesRead = decompressor.read(buffer, 0, buffer.length);
 String content = new String(buffer, 0, bytesRead, "UTF-8");
 assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", content);
 decompressor.close();
 }

 @Test(expected = IOException.class)
 public void testDecompressInvalidFile() throws IOException {
 InputStream inputStream = IOUtils.toInputStream("MjkyZW5ndGg=", "BASE64"); // Base64 decoding error
 DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(inputStream);
 byte[] buffer = new byte[512];
 int bytesRead = decompressor.read(buffer, 0, buffer.length);
 fail("Should throw an IOException if the input stream is not a valid Deflate-compressed stream.");
 }

 @Test(expected = NullPointerException.class)
 public void testDecompressNullStream() {
 try {
 DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(null);
 fail("Should throw a NullPointerException if the input stream is null.");
 } catch (NullPointerException e) {
 // Expected
 }
 }
}