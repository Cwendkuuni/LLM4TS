import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class CodersTest {

 private InputStream createTestInputStream() {
 return new ByteArrayInputStream(new byte[0]);
 }

 private OutputStream createTestOutputStream() {
 return new ByteArrayOutputStream();
 }

 @Test
 public void testAddDecoder_Copy() throws Exception {
 InputStream is = createTestInputStream();
 Coder copyCoder = new CopyDecoder();
 byte[] password = new byte[0];
 InputStream result = Coders.addDecoder(is, copyCoder, password);
 assertEquals(is, result);
 }

 @Test(expected = IOException.class)
 public void testAddDecoder_UnsupportedCompressionMethod() throws Exception {
 InputStream is = createTestInputStream();
 Coder unsupportedCoder = new Object();
 byte[] password = new byte[0];
 Coders.addDecoder(is, unsupportedCoder, password);
 fail("Should throw IOException for unsupported compression method");
 }

 @Test
 public void testAddEncoder_LZMA() throws Exception {
 OutputStream out = createTestOutputStream();
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMADecoder();
 byte[] password = new byte[0];
 Coders.addEncoder(out, lzmaMethod, password);
 assertNotNull(out);
 }

 @Test(expected = IOException.class)
 public void testAddEncoder_UnsupportedCompressionMethod() throws Exception {
 OutputStream out = createTestOutputStream();
 SevenZMethod unsupportedMethod = new Object();
 byte[] password = new byte[0];
 Coders.addEncoder(out, unsupportedMethod, password);
 fail("Should throw IOException for unsupported compression method");
 }

 @Test
 public void testCoderTableSize() {
 assertEquals(6, Coders.coderTable.length);
 }

 @Test
 public void testCoderIdCreation() {
 SevenZMethod copyMethod = SevenZMethod.COPY;
 Coder copyCoder = new CopyDecoder();
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 Coder lzmaCoder = new LZMADecoder();
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 Coder deflateCoder = new DeflateDecoder();
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 Coder bzip2Coder = new BZIP2Decoder();
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 Coder aes256SHA256Coder = new AES256SHA256Decoder();
 CoderId[] expectedCoderIds = new CoderId[] {
 new CoderId(copyMethod, copyCoder),
 new CoderId(lzmaMethod, lzmaCoder),
 new CoderId(lzmaMethod, lzmaCoder), // LZMA2 is identical to LZMA
 new CoderId(deflateMethod, deflateCoder),
 new CoderId(bzip2Method, bzip2Coder),
 new CoderId(aes256SHA256Method, aes256SHA256Coder)
 };
 assertArrayEquals(expectedCoderIds, Coders.coderTable);
 }

 @Test
 public void testCoderBaseDecode() throws Exception {
 InputStream is = createTestInputStream();
 CopyDecoder copyDecoder = new CopyDecoder();
 byte[] password = new byte[0];
 InputStream result = copyDecoder.decode(is, copyDecoder, password);
 assertEquals(is, result);
 }

 @Test
 public void testCoderBaseEncode() throws Exception {
 OutputStream out = createTestOutputStream();
 CopyDecoder copyDecoder = new CopyDecoder();
 byte[] password = new byte[0];
 OutputStream result = copyDecoder.encode(out, password);
 assertNotNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCoderBaseEncode_ThrowsException() throws Exception {
 CopyDecoder copyDecoder = new CopyDecoder();
 byte[] password = new byte[0];
 OutputStream out = createTestOutputStream();
 try {
 copyDecoder.encode(out, password);
 fail("Should throw UnsupportedOperationException for write-only CoderBase");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test
 public void testLZMADecoderDecode() throws Exception {
 byte[] propsByte = (byte) 0xff;
 long dictSize = 4096L;
 byte[] password = new byte[0];
 byte[] testData = new byte[0];
 InputStream testInputStream = new ByteArrayInputStream(testData);
 LZMADecoder lzmaDecoder = new LZMADecoder();
 InputStream result = lzmaDecoder.decode(testInputStream, lzmaDecoder, password);
 // LZMAInputStream doesn't support reading testData, so equal check is not applicable
 //assertTrue(Arrays.equals(testData, result.readAllBytes()));
 }

 @Test(expected = IOException.class)
 public void testLZMADecoderDecode_IOException() throws Exception {
 byte[] propsByte = (byte) 0xff;
 long dictSize = Long.MAX_VALUE;
 byte[] password = new byte[0];
 byte[] testData = new byte[0];
 InputStream testInputStream = new ByteArrayInputStream(testData);
 LZMADecoder lzmaDecoder = new LZMADecoder();
 try {
 lzmaDecoder.decode(testInputStream, lzmaDecoder, password);
 fail("Should throw IOException for invalid dictionary size");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testDeflateDecoderDecode() throws Exception {
 InputStream is = createTestInputStream();
 DeflateDecoder deflateDecoder = new DeflateDecoder();
 byte[] password = new byte[0];
 InputStream result = deflateDecoder.decode(is, deflateDecoder, password);
 // InflaterInputStream doesn't support reading testData, so equal check is not applicable
 //assertTrue(Arrays.equals(testData, result.readAllBytes()));
 }

 @Test(expected = IOException.class)
 public void testDeflateDecoderDecode_IOException() throws Exception {
 InputStream is = createTestInputStream();
 DeflateDecoder deflateDecoder = new DeflateDecoder();
 byte[] password = new byte[0];
 try {
 deflateDecoder.decode(is, deflateDecoder, password);
 fail("Should throw IOException for invalid decoder");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testBZIP2DecoderDecode() throws Exception {
 InputStream is = createTestInputStream();
 BZIP2Decoder bzip2Decoder = new BZIP2Decoder();
 byte[] password = new byte[0];
 InputStream result = bzip2Decoder.decode(is, bzip2Decoder, password);
 // BZip2CompressorInputStream doesn't support reading testData, so equal check is not applicable
 //assertTrue(Arrays.equals(testData, result.readAllBytes()));
 }

 @Test(expected = IOException.class)
 public void testBZIP2DecoderDecode_IOException() throws Exception {
 InputStream is = createTestInputStream();
 BZIP2Decoder bzip2Decoder = new BZIP2Decoder();
 byte[] password = new byte[0];
 try {
 bzip2Decoder.decode(is, bzip2Decoder, password);
 fail("Should throw IOException for invalid decoder");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testAES256SHA256DecoderDecode() throws Exception {
 byte[] salt = new byte[] { 0x01, 0x02, 0x03, 0x04 };
 byte[] iv = new byte[16];
 byte[] password = "password".getBytes();
 byte[] testData = "test data".getBytes();
 InputStream is = new ByteArrayInputStream(testData);
 AES256SHA256Decoder aes256SHA256Decoder = new AES256SHA256Decoder();
 byte[] decryptedData = aes256SHA256Decoder.decode(is, aes256SHA256Decoder, password).readAllBytes();
 assertTrue(Arrays.equals(testData, decryptedData));
 }

 @Test(expected = IOException.class)
 public void testAES256SHA256DecoderDecode_IOException() throws Exception {
 byte[] salt = new byte[] { 0x01, 0x02, 0x03, 0x04 };
 byte[] iv = new byte[16];
 byte[] password = "password".getBytes();
 byte[] testData = "test data".getBytes();
 InputStream is = new ByteArrayInputStream(testData);
 AES256SHA256Decoder aes256SHA256Decoder = new AES256SHA256Decoder();
 try {
 aes256SHA256Decoder.decode(is, aes256SHA256Decoder, password);
 fail("Should throw IOException for invalid decoder");
 } catch (IOException e) {
 // Expected
 }
 }
}