import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.Coder;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.CoderId;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CodersTest {

 // Test addDecoder method
 @Test
 public void testAddDecoder_SupportedFormat_ReturnsExpectedDecoder() throws Exception {
 byte[] password = new byte[16];
 Coder coder = new BZIP2Decoder();
 InputStream expected = new BZip2CompressorInputStream(new ByteArrayInputStream(new byte[1]));
 InputStream actual = Coders.addDecoder(new ByteArrayInputStream(new byte[1]), coder, password);
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_UnsupportedFormat_ThrowsIOException() throws Exception {
 byte[] password = new byte[16];
 Coder coder = new Object() {}.getClass().newInstance(); // Unsupported Coder
 InputStream inputStream = new ByteArrayInputStream(new byte[1]);
 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
 }

 // Test addEncoder method
 @Test(expected = IOException.class)
 public void testAddEncoder_UnsupportedFormat_ThrowsIOException() throws Exception {
 byte[] password = new byte[16];
 OutputStream outputStream = new ByteArrayOutputStream();
 Coder coder = new Object() {}.getClass().newInstance(); // Unsupported Coder
 assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, new SevenZMethod("UnsupportedFormat"), password));
 }

 @Test
 public void testAddEncoder_SupportedFormat_ReturnsExpectedEncoder() throws Exception {
 byte[] password = new byte[16];
 Coder coder = new DeflateDecoder();
 OutputStream outputStream = new ByteArrayOutputStream();
 Coders.addEncoder(outputStream, SevenZMethod.DEFLATE, password);
 Deflater deflater = new Deflater(9, true);
 deflater.setOutputStream(outputStream);
 deflater.finish();
 byte[] expected = new byte[1];
 assertEquals(expected, outputStream.toByteArray());
 }

 // Test coderTable
 @Test
 public void testCoderTable_ContainsSupportedCoders() {
 for (CoderId coderId : Coders.coderTable) {
 System.out.println(coderId.method);
 }
 }
}