import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CodersTest {

 // Test for addDecoder method with supported compression methods
 @Test
 public void testAddDecoderWithSupportedCompressionMethods() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, 0x02, 0x03});
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];
 CoderId expectedCoderId = new CoderId(SevenZMethod.DEFLATE, new DeflateDecoder());
 InputStream result = Coders.addDecoder(inputStream, coder, password);
 assertArrayEquals(expectedCoderId.coder.getClass().getName(), result.getClass().getName());
 }

 // Test for addDecoder method with unsupported compression method
 @Test(expected = IOException.class)
 public void testAddDecoderWithUnsupportedCompressionMethod() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x04, 0x05, 0x06});
 Coder coder = new copyDecoder(); // Unsupported Coder
 byte[] password = new byte[0];
 Coders.addDecoder(inputStream, coder, password);
 }

 // Test for addEncoder method with supported compression methods
 @Test
 public void testAddEncoderWithSupportedCompressionMethods() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = new byte[0];
 Coder expectedCoder = new DeflateDecoder();
 CoderId expectedCoderId = new CoderId(SevenZMethod.DEFLATE, new DeflateDecoder());
 Coder result = Coders.addEncoder(outputStream, method, password);
 assertArrayEquals(expectedCoderId.coder.getClass().getName(), result.getClass().getName());
 }

 // Test for addEncoder method with unsupported compression method
 @Test(expected = IOException.class)
 public void testAddEncoderWithUnsupportedCompressionMethod() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.AES256SHA256; // Unsupported compression method
 byte[] password = new byte[0];
 Coders.addEncoder(outputStream, method, password);
 }
}