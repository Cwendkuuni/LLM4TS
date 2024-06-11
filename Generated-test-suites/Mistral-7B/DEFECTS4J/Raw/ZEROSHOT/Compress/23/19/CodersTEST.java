import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CodersTest {

 @Test
 public void testAddDecoder() throws Exception {
 Coder copyCoder = mock(CopyDecoder.class);
 Coder lzmaCoder = mock(LZMADecoder.class);
 Coder deflateCoder = mock(DeflateDecoder.class);
 Coder bzip2Coder = mock(BZIP2Decoder.class);
 Coder aes256SHA256Coder = mock(AES256SHA256Decoder.class);
 SevenZMethod copyMethod = SevenZMethod.COPY;
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 byte[] password = "password".getBytes();
 InputStream inputStream = mock(InputStream.class);
 byte[] expectedDecodedStream = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};

 when(copyCoder.decode(inputStream, copyMethod, password)).thenReturn(new ByteArrayInputStream(expectedDecodedStream));
 when(lzmaCoder.decode(inputStream, lzmaMethod, password)).thenCallRealMethod();
 when(deflateCoder.decode(inputStream, deflateMethod, password)).thenCallRealMethod();
 when(bzip2Coder.decode(inputStream, bzip2Method, password)).thenCallRealMethod();
 when(aes256SHA256Coder.decode(inputStream, aes256SHA256Method, password)).thenCallRealMethod();

 CoderId copyCoderId = new CoderId(copyMethod, copyCoder);
 CoderId lzmaCoderId = new CoderId(lzmaMethod, lzmaCoder);
 CoderId deflateCoderId = new CoderId(deflateMethod, deflateCoder);
 CoderId bzip2CoderId = new CoderId(bzip2Method, bzip2Coder);
 CoderId aes256SHA256CoderId = new CoderId(aes256SHA256Method, aes256SHA256Coder);
 CoderId[] coderIds = {copyCoderId, lzmaCoderId, deflateCoderId, bzip2CoderId, aes256SHA256CoderId};
 Coders coders = new Coders();

 InputStream result = coders.addDecoder(inputStream, copyMethod, password);
 assertSame(expectedDecodedStream, result);

 result = coders.addDecoder(inputStream, lzmaMethod, password);
 assertSame(inputStream, result);

 result = coders.addDecoder(inputStream, deflateMethod, password);
 assertSame(inputStream, result);

 result = coders.addDecoder(inputStream, bzip2Method, password);
 assertSame(inputStream, result);

 result = coders.addDecoder(inputStream, aes256SHA256Method, password);
 assertSame(inputStream, result);

 result = coders.addDecoder(inputStream, SevenZMethod.UNKNOWN, password);
 assertSame(inputStream, result);
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod copyMethod = SevenZMethod.COPY;
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;
 Coder copyCoder = mock(CopyDecoder.class);
 Coder lzmaCoder = mock(LZMADecoder.class);
 Coder deflateCoder = mock(DeflateDecoder.class);
 Coder bzip2Coder = mock(BZIP2Decoder.class);
 Coder aes256SHA256Coder = mock(AES256SHA256Decoder.class);
 Coder copyEncoder = mock(CopyEncoder.class);
 Coder lzmaEncoder = mock(LZMAEncoder.class);
 Coder deflateEncoder = mock(DeflateEncoder.class);
 Coder bzip2Encoder = mock(BZIP2Encoder.class);
 Coder aes256SHA256Encoder = mock(AES256SHA256Encoder.class);
 byte[] password = "password".getBytes();

 when(copyEncoder.encode(outputStream, password)).thenCallRealMethod();
 when(lzmaEncoder.encode(outputStream, password)).thenCallRealMethod();
 when(deflateEncoder.encode(outputStream, password)).thenCallRealMethod();
 when(bzip2Encoder.encode(outputStream, password)).thenCallRealMethod();
 when(aes256SHA256Encoder.encode(outputStream, password)).thenCallRealMethod();

 CoderId copyCoderId = new CoderId(copyMethod, copyEncoder);
 CoderId lzmaCoderId = new CoderId(lzmaMethod, lzmaEncoder);
 CoderId deflateCoderId = new CoderId(deflateMethod, deflateEncoder);
 CoderId bzip2CoderId = new CoderId(bzip2Method, bzip2Encoder);
 CoderId aes256SHA256CoderId = new CoderId(aes256SHA256Method, aes256SHA256Encoder);
 CoderId[] coderIds = {copyCoderId, lzmaCoderId, deflateCoderId, bzip2CoderId, aes256SHA256CoderId};
 Coders coders = new Coders();

 OutputStream result = coders.addEncoder(outputStream, copyMethod, password);
 assertSame(outputStream, result);

 result = coders.addEncoder(outputStream, lzmaMethod, password);
 assertSame(outputStream, result);

 result = coders.addEncoder(outputStream, deflateMethod, password);
 assertSame(outputStream, result);

 result = coders.addEncoder(outputStream, bzip2Method, password);
 assertSame(outputStream, result);

 result = coders.addEncoder(outputStream, aes256SHA256Method, password);
 assertSame(outputStream, result);

 result = coders.addEncoder(outputStream, SevenZMethod.UNKNOWN, password);
 assertSame(outputStream, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDecoderWithNullInputStream() throws Exception {
 Coders coders = new Coders();
 SevenZMethod method = SevenZMethod.LZMA;
 byte[] password = "password".getBytes();
 InputStream inputStream = null;
 assertThrows(IllegalArgumentException.class, () -> coders.addDecoder(inputStream, method, password));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDecoderWithNullMethod() throws Exception {
 Coders coders = new Coders();
 InputStream inputStream = mock(InputStream.class);
 byte[] password = "password".getBytes();
 SevenZMethod method = null;
 assertThrows(IllegalArgumentException.class, () -> coders.addDecoder(inputStream, method, password));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDecoderWithNullPassword() throws Exception {
 Coders coders = new Coders();
 InputStream inputStream = mock(InputStream.class);
 SevenZMethod method = SevenZMethod.LZMA;
 byte[] password = null;
 assertThrows(IllegalArgumentException.class, () -> coders.addDecoder(inputStream, method, password));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEncoderWithNullOutputStream() throws Exception {
 Coders coders = new Coders();
 SevenZMethod method = SevenZMethod.LZMA;
 byte[] password = "password".getBytes();
 OutputStream outputStream = null;
 assertThrows(IllegalArgumentException.class, () -> coders.addEncoder(outputStream, method, password));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEncoderWithNullMethod() throws Exception {
 Coders coders = new Coders();
 OutputStream outputStream = mock(OutputStream.class);
 byte[] password = "password".getBytes();
 SevenZMethod method = null;
 assertThrows(IllegalArgumentException.class, () -> coders.addEncoder(outputStream, method, password));
 }
}