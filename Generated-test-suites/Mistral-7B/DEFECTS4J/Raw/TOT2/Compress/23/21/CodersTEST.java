import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CodersTest {

 private InputStream inputStream;
 private OutputStream outputStream;

 @Test
 public void testAddDecoderWithSupportedMethod() throws Exception {
 inputStream = createFileWithData("testData.7z");
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = "password".getBytes();
 InputStream decoder = Coders.addDecoder(inputStream, new Coder(method), password);
 assertEquals(decoder.getClass(), BZip2CompressorInputStream.class);
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoderWithUnsupportedMethod() throws Exception {
 inputStream = createFileWithData("testData.7z");
 SevenZMethod unsupportedMethod = new SevenZMethod(-1, "UnsupportedMethod");
 byte[] password = "password".getBytes();
 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, new Coder(unsupportedMethod), password));
 inputStream.close();
 }

 @Test
 public void testAddEncoderWithSupportedMethod() throws Exception {
 outputStream = createFileOutputStream("testData.7z");
 SevenZMethod method = SevenZMethod.DEFLATE;
 byte[] password = "password".getBytes();
 OutputStream encoder = Coders.addEncoder(outputStream, method, password);
 assertEquals(encoder.getClass(), DeflaterOutputStream.class);
 encoder.close();
 outputStream.close();
 }

 @Test
 public void testAddEncoderWithUnsupportedMethod() throws Exception {
 outputStream = createFileOutputStream("testData.7z");
 SevenZMethod unsupportedMethod = new SevenZMethod(-1, "UnsupportedMethod");
 byte[] password = "password".getBytes();
 assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, unsupportedMethod, password));
 outputStream.close();
 }

 @Test
 public void testCopyDecoder() throws Exception {
 inputStream = createFileWithData("testData.7z");
 Coder copier = new Coder(SevenZMethod.COPY);
 InputStream decoder = Coders.addDecoder(inputStream, copier, null);
 byte[] buffer = new byte[10];
 int bytesRead = decoder.read(buffer, 0, buffer.length);
 assertEquals(bytesRead, buffer.length);
 assertEquals(buffer, decoder.readAllBytes());
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testLzmaDecoder() throws Exception {
 inputStream = createFileWithData("testData.lzma");
 Coder lzmaDecoder = new LZMADecoder();
 InputStream decoder = Coders.addDecoder(inputStream, lzmaDecoder, null);
 byte[] buffer = new byte[10];
 int bytesRead = decoder.read(buffer, 0, buffer.length);
 assertEquals(bytesRead, buffer.length);
 assertEquals(buffer, decoder.readAllBytes());
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testBzip2Decoder() throws Exception {
 inputStream = createFileWithData("testData.bz2");
 Coder bzip2Decoder = new BZIP2Decoder();
 InputStream decoder = Coders.addDecoder(inputStream, bzip2Decoder, null);
 byte[] buffer = new byte[10];
 int bytesRead = decoder.read(buffer, 0, buffer.length);
 assertEquals(bytesRead, buffer.length);
 assertEquals(buffer, decoder.readAllBytes());
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testAes256Sha256Decoder() throws Exception {
 inputStream = createFileWithData("testData.aes256.7z");
 byte[] password = "password".getBytes();
 Coder aes256Sha256Decoder = new AES256SHA256Decoder();
 InputStream decoder = Coders.addDecoder(inputStream, aes256Sha256Decoder, password);
 byte[] buffer = new byte[10];
 int bytesRead = decoder.read(buffer, 0, buffer.length);
 assertEquals(bytesRead, buffer.length);
 assertEquals(buffer, decoder.readAllBytes());
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testLzmaDecoderThrowsExceptionWithUnsupportedDictionarySize() throws Exception {
 inputStream = createFileWithData("testData.lzma");
 Coder lzmaDecoder = new LZMADecoder();
 byte[] password = new byte[LZMAInputStream.DICT_SIZE_MAX + 1];
 InputStream decoder = Coders.addDecoder(inputStream, lzmaDecoder, password);
 assertThrows(IOException.class, () -> decoder.read(new byte[1], 0, 1));
 decoder.close();
 inputStream.close();
 }

 @Test
 public void testAddDecoderThrowsExceptionWithUnsupportedCompressionMethod() throws Exception {
 inputStream = createFileWithData("testData.7z");
 SevenZMethod unsupportedMethod = new SevenZMethod(-1, "UnsupportedMethod");
 byte[] password = "password".getBytes();
 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, new Coder(unsupportedMethod), password));
 inputStream.close();
 }

 @Test
 public void testAddEncoderThrowsExceptionWithUnsupportedCompressionMethod() throws Exception {
 outputStream = createFileOutputStream("testData.7z");
 SevenZMethod unsupportedMethod = new SevenZMethod(-1, "UnsupportedMethod");
 byte[] password = "password".getBytes();
 assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, unsupportedMethod, password));
 outputStream.close();
 }

 @Test
 public void testDecoderThrowsExceptionWithInvalidPassword() throws Exception {
 inputStream = createFileWithData("testData.aes256.7z");
 byte[] wrongPassword = "wrongpassword".getBytes();
 Coder aes256Sha256Decoder = new AES256SHA256Decoder();
 InputStream decoder = Coders.addDecoder(inputStream, aes256Sha256Decoder, wrongPassword);
 assertThrows(IOException.class, () -> decoder.readAllBytes());
 decoder.close();
 inputStream.close();
 }

 private InputStream createFileWithData(String fileName) throws Exception {
 Class<?> clazz = Coders.class;
 InputStream inputStream = clazz.getResourceAsStream(fileName);
 if (inputStream == null) {
 throw new IOException("File not found: " + fileName);
 }
 return new ByteArrayInputStream(clazz.getResource(fileName).getBytes("UTF-8"));
 }

 private OutputStream createFileOutputStream(String fileName) throws Exception {
 return new ByteArrayOutputStream();
 }

 private static class Coder extends Coders.CoderBase {
 Coder(SevenZMethod method) {
 super(method);
 }
 }

 private static class LZMADecoder extends Coders.CoderBase {
 @Override
 InputStream decode(final InputStream in, final Coder coder, final byte[] password)
 throws IOException {
 return new LZMAInputStream(in, -1, 0, LZMAInputStream.DEFAULT_PROPS);
 }
 }

}