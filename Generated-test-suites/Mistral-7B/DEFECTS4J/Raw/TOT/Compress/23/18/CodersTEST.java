import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.Assert.*;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x07, 0x01, 0x01, 0x01, 0x00, 0x07, 0x00, 0x14, 0x00, 0x04, 0x00, 0x02, 0x00, 0x16, 0x04, 0x00, 0x01}); // LZMA compression method ID
 InputStream expectedResult = new LZMAInputStream(new ByteArrayInputStream(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07})); // Sample LZMA compressed data

 InputStream result = Coders.addDecoder(inputStream, new SevenZMethod(SevenZMethod.LZMA), null);

 Assert.assertSame(expectedResult, result);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedDecoder() throws Exception {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x07, 0x01, 0x01, 0x01, 0x00, 0x08, 0x00, 0x14, 0x00, 0x04, 0x00, 0x02, 0x00, 0x16, 0x04, 0x00, 0x01}); // Unsupported compression method ID
 InputStream expectedException = null;

 InputStream result = Coders.addDecoder(inputStream, new SevenZMethod(SevenZMethod.LZMA), null);

 Assert.assertNull(result);
 Assert.assertSame(expectedException, result);
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.BZIP2; // BZIP2 compression method

 OutputStream expectedResult = new BZip2CompressorOutputStream(new ByteArrayOutputStream());

 OutputStream result = Coders.addEncoder(outputStream, method, null);

 Assert.assertSame(expectedResult, result);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedEncoder() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.AES256SHA256; // Unsupported compression method

 OutputStream expectedException = null;

 OutputStream result = Coders.addEncoder(outputStream, method, null);

 Assert.assertNull(result);
 Assert.assertSame(expectedException, result);
 }
}