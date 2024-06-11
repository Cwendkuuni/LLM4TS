import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyCoder = new CopyDecoder();
 Coder lzmaCoder = new LZMADecoder();
 Coder deflateCoder = new DeflateDecoder();
 Coder bzip2Coder = new BZIP2Decoder();
 Coder aes256sha256Coder = new AES256SHA256Decoder();
 SevenZMethod[] methods = new SevenZMethod[] { SevenZMethod.COPY, SevenZMethod.LZMA,
 SevenZMethod.LZMA2, SevenZMethod.DEFLATE, SevenZMethod.BZIP2, SevenZMethod.AES256SHA256 };
 byte[] password = new byte[] { 1, 2, 3, 4 };

 for (SevenZMethod method : methods) {
 Coder coder = null;
 switch (method) {
 case COPY:
 coder = copyCoder;
 break;
 case LZMA:
 coder = lzmaCoder;
 break;
 case LZMA2:
 coder = lzmaCoder;
 break;
 case DEFLATE:
 coder = deflateCoder;
 break;
 case BZIP2:
 coder = bzip2Coder;
 break;
 case AES256SHA256:
 coder = aes256sha256Coder;
 break;
 }
 InputStream expectedInputStream = null;
 InputStream actualInputStream = null;
 try {
 actualInputStream = Coders.addDecoder(new ByteArrayInputStream(new byte[0]),
 new Coder(method.getId(), coder), password);
 expectedInputStream = new ByteArrayInputStream(new byte[0]);
 } catch (IOException e) {
 if (expectedInputStream != null) {
 throw e;
 }
 continue;
 }
 assertNull(expectedInputStream);
 assertNull(coder.encode(new ByteArrayOutputStream(), password));
 assertArrayEquals(new byte[0], new byte[1024], actualInputStream.readAllBytes());
 assertArrayEquals(new byte[0], new byte[1024], expectedInputStream.readAllBytes());
 actualInputStream.close();
 expectedInputStream.close();
 }

 Coder unsupportedCoder = new UnsupportedCoder();
 try {
 Coders.addDecoder(new ByteArrayInputStream(new byte[0]),
 new Coder(123, unsupportedCoder), password);
 assertFalse("Exception should be thrown", true);
 } catch (IOException e) {
 assertTrue("Expected exception: " + e.getMessage(), e.getMessage().contains(
 "Unsupported compression method "));
 }
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder copier = new CopyEncoder();
 Coder lzmaEncoder = new LZMAEncoder();
 Coder deflateEncoder = new DeflateEncoder();
 Coder bzip2Encoder = new BZip2Encoder();
 Coder aes256sha256Encoder = new AES256SHA256Encoder();

 for (Coder coder : new Coder[] { copier, lzmaEncoder, deflateEncoder, bzip2Encoder,
 aes256sha256Encoder }) {
 if (coder.getClass() == method.getClass()) {
 outputStream = Coders.addEncoder(outputStream, method, password);
 break;
 }
 }
 assertNull(outputStream);
 }

 @Test
 public void testAddEncoderUnsupportedMethod() throws Exception {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod unsupportedMethod = new SevenZMethod(123);
 Coder copier = new CopyEncoder();
 Coder lzmaEncoder = new LZMAEncoder();
 Coder deflateEncoder = new DeflateEncoder();
 Coder bzip2Encoder = new BZip2Encoder();
 Coder aes256sha256Encoder = new AES256SHA256Encoder();

 for (Coder coder : new Coder[] { copier, lzmaEncoder, deflateEncoder, bzip2Encoder,
 aes256sha256Encoder }) {
 if (coder.getClass() == unsupportedMethod.getClass()) {
 outputStream = Coders.addEncoder(outputStream, unsupportedMethod, password);
 break;
 }
 }
 assertNull(outputStream);
 }
}

class UnsupportedCoder implements Coder {
 @Override
 public InputStream decode(InputStream in, Coder coder, byte[] password)
 throws IOException {
 throw new UnsupportedOperationException();
 }

 @Override
 public OutputStream encode(OutputStream out, byte[] password) throws IOException {
 throw new UnsupportedOperationException();
 }
}