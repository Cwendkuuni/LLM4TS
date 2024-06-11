import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
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

 Coder[] coders = {copyCoder, lzmaCoder, deflateCoder, bzip2Coder, aes256SHA256Coder};
 CoderId[] coderIds = Coders.coderTable;

 for (CoderId coderId : coderIds) {
 SevenZMethod method = coderId.method;
 Coder expectedCoder = null;
 for (Coder coder : coders) {
 if (coder.getClass().equals(coderId.coder.getClass())) {
 expectedCoder = coder;
 break;
 }
 }
 InputStream inputStream = Coders.addDecoder(mock(InputStream.class), method, new byte[0]);
 assertNull(inputStream);
 if (expectedCoder == null) continue;
 when(expectedCoder.decode(mock(InputStream.class), mock(Coder.class), anyByteArray())).thenReturn(mock(InputStream.class));
 when(expectedCoder.encode(mock(OutputStream.class), anyByteArray())).thenThrow(new UnsupportedOperationException());

 byte[] password = new byte[0];
 InputStream returnedInputStream = Coders.addDecoder(mock(InputStream.class), method, password);
 assertNotNull(returnedInputStream);
 assertSame(expectedCoder.getClass(), returnedInputStream.getClass().getSuperclass());
 }
 }

 @Test
 public void testAddEncoder() throws Exception {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod copyMethod = SevenZMethod.COPY;
 SevenZMethod lzmaMethod = SevenZMethod.LZMA;
 SevenZMethod deflateMethod = SevenZMethod.DEFLATE;
 SevenZMethod bzip2Method = SevenZMethod.BZIP2;
 SevenZMethod aes256SHA256Method = SevenZMethod.AES256SHA256;

 Coder copyDecoder = mock(CopyDecoder.class);
 Coder lzmaDecoder = mock(LZMADecoder.class);
 Coder deflateDecoder = mock(DeflateDecoder.class);
 Coder bzip2Decoder = mock(BZIP2Decoder.class);
 Coder aes256SHA256Decoder = mock(AES256SHA256Decoder.class);
 Coder copyEncoder = mock(CopyDecoder.class);
 Coder lzmaEncoder = mock(LZMAEncoder.class);
 Coder deflateEncoder = mock(DeflateEncoder.class);
 Coder bzip2Encoder = mock(BZIP2Encoder.class);
 Coder aes256SHA256Encoder = mock(AES256SHA256Encoder.class);

 Coder[] coders = {copyDecoder, lzmaDecoder, deflateDecoder, bzip2Decoder, aes256SHA256Decoder};
 CoderId[] coderIds = Coders.coderTable;

 for (CoderId coderId : coderIds) {
 SevenZMethod method = coderId.method;
 Coder expectedCoder = null;
 for (Coder coder : coders) {
 if (coder.getClass().equals(coderId.coder.getClass())) {
 expectedCoder = coder;
 break;
 }
 }
 OutputStream outputStreamMock = mock(OutputStream.class);
 byte[] password = new byte[0];
 OutputStream returnedOutputStream = Coders.addEncoder(outputStream, method, password);
 assertNull(returnedOutputStream);
 if (expectedCoder == null) continue;
 when(expectedCoder.encode(outputStreamMock, anyByteArray())).thenReturn(outputStreamMock);

 assertSame(expectedCoder.getClass(), returnedOutputStream.getClass());
 }
 }

 @Test
 public void testLZMADecoder() throws Exception {
 byte[] testData = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
 InputStream inputStream = new ByteArrayInputStream(testData);
 Coder lzmaDecoder = new LZMADecoder();
 byte[] decodedData = new byte[testData.length];
 int decodedDataLength = lzmaDecoder.decode(inputStream, null, new byte[0])
 .read(decodedData, 0, decodedData.length);
 assertArrayEquals(testData, decodedData);
 }

 @Test(expected = IOException.class)
 public void testLZMADecoderWithWrongMethod() throws Exception {
 byte[] testData = new byte[] {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};
 InputStream inputStream = new ByteArrayInputStream(testData);
 Coder deflateDecoder = new DeflateDecoder();
 int decodedDataLength = deflateDecoder.decode(inputStream, null, new byte[0])
 .read(new byte[testData.length], 0, testData.length);
 }
}