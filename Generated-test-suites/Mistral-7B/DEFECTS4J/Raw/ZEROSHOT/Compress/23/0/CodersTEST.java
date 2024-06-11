import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyDecoder = new Coders.CopyDecoder();
 SevenZMethod copyMethod = SevenZMethod.COPY;
 byte[] copyPassword = new byte[0];
 InputStream expectedInput = new java.io.ByteArrayInputStream(new byte[]{});
 InputStream result = Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), copyDecoder, copyPassword);
 assertArrayEquals(expectedInput.readAllBytes(), result.readAllBytes());
 assertNull(result);

 Coder lzmaDecoder = new Coders.LZMADecoder();
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 byte[] lzmaPassword = new byte[1];
 InputStream expectedLzmaInput = new LZMAInputStream(new java.io.ByteArrayInputStream(new byte[]{}), -1, (int) 0x1, 0);
 InputStream resultLzma = Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), lzmaDecoder, lzmaPassword);
 assertEquals(expectedLzmaInput, resultLzma);

 Coder deflateDecoder = new Coders.DeflateDecoder();
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 byte[] deflatePassword = new byte[1];
 InputStream expectedDeflateInput = new InflaterInputStream(new java.io.ByteArrayInputStream(new byte[]{}), new Inflater(true));
 InputStream resultDeflate = Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), deflateDecoder, deflatePassword);
 assertEquals(expectedDeflateInput, resultDeflate);

 Coder bzip2Decoder = new Coders.BZIP2Decoder();
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 byte[] bzip2Password = new byte[1];
 InputStream expectedBzip2Input = new BZip2CompressorInputStream(new java.io.ByteArrayInputStream(new byte[]{}));
 InputStream resultBzip2 = Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), bzip2Decoder, bzip2Password);
 assertEquals(expectedBzip2Input, resultBzip2);

 Coder aes256sha256Decoder = new Coders.AES256SHA256Decoder();
 SevenZMethod aes256sha256Method = SevenZMethod.AES256SHA256;
 byte[] aes256sha256Password = new byte[16];
 InputStream expectedAes256sha256Input = new java.io.ByteArrayInputStream(new byte[]{});
 InputStream resultAes256sha256 = Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), aes256sha256Decoder, aes256sha256Password);
 assertNull(resultAes256sha256);
 }

 @Test(expected = IOException.class)
 public void testUnsupportedDecoder() throws Exception {
 Coder unsupportedDecoder = new Object();
 SevenZMethod unsupportedMethod = new SevenZMethod(-1);
 byte[] unsupportedPassword = new byte[0];
 Coders.addDecoder(new java.io.ByteArrayInputStream(new byte[]{}), unsupportedDecoder, unsupportedPassword);
 }
}