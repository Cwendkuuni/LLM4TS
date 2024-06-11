import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CodersTest {

 @Test
 public void testAddDecoderWithSupportedMethod() throws Exception {
 InputStream is = new FilterInputStream(new ByteArrayInputStream(new byte[0])) {
 @Override
 public void close() throws IOException {
 super.close();
 }
 };
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];
 InputStream decoder = Coders.addDecoder(is, coder, password);
 assertNotNull(decoder);
 }

 @Test(expected = IOException.class)
 public void testAddDecoderWithUnsupportedMethod() throws Exception {
 InputStream is = new ByteArrayInputStream(new byte[0]);
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];
 Coders.addDecoder(is, coder, password);
 }

 @Test(expected = IOException.class)
 public void testAddDecoderWithInvalidPassword() throws Exception {
 InputStream is = new ByteArrayInputStream(new byte[0]) {
 @Override
 public void close() throws IOException {
 super.close();
 }
 };
 Coder coder = new AES256SHA256Decoder();
 byte[] password = new byte[1];
 Coders.addDecoder(is, coder, password);
 }

 @Test
 public void testAddEncoderWithSupportedMethod() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateEncoder();
 byte[] password = new byte[0];
 OutputStream encoder = Coders.addEncoder(out, method, password);
 assertNotNull(encoder);
 }

 @Test(expected = IOException.class)
 public void testAddEncoderWithUnsupportedMethod() throws Exception {
 OutputStream out = new ByteArrayOutputStream();
 SevenZMethod method = new SevenZMethod() {
 @Override
 public byte[] getId() {
 throw new UnsupportedOperationException();
 }
 };
 Coder coder = new DeflateEncoder();
 byte[] password = new byte[0];
 Coders.addEncoder(out, method, password);
 }

 @Test
 public void testCoderTableSize() {
 assertArrayEquals(new SevenZMethod[] {SevenZMethod.COPY, SevenZMethod.LZMA,
 SevenZMethod.LZMA2, SevenZMethod.DEFLATE, SevenZMethod.BZIP2, SevenZMethod.AES256SHA256},
 Coders.coderTable[0].method);
 }

}