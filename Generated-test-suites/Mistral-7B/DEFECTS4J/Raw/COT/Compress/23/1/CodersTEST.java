import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMAInputStream;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.SecretKeySpec;

public class CodersTest {

 @Test
 public void testAddDecoderUnsupportedMethod() {
 InputStream inputStream = mock(InputStream.class);
 Coder coder = mock(Coder.class);
 when(coder.decompressionMethodId).thenReturn(new byte[] { 0x01, 0x02 });
 when(coderTable[0].method.getId()).thenReturn(new byte[] { 0x01, 0x03 });

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, new byte[0]));
 }

 @Test
 public void testAddDecoderSupportedMethod() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x04, 0x00, 0x01, 0x01, 0x00, 0x05, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x0A, 0x00, 0x0B, 0x00, 0x0C, 0x00, 0x0D, 0x00 });
 Coder coder = new CopyDecoder();
 CoderId coderId = coderTable[0];

 InputStream outputStream = Coders.addDecoder(inputStream, coder, new byte[0]);
 assertNotNull(outputStream);
 assertArrayEquals(new byte[0], outputStream.readAllBytes());
 }

 @Test
 public void testAddDecoderWithPassword() throws IOException {
 InputStream inputStream = mock(InputStream.class);
 Coder coder = mock(Coder.class);
 when(coder.decompressionMethodId).thenReturn(new byte[] { 0x01, 0x04 });
 when(coderTable[4].method.getId()).thenReturn(new byte[] { 0x01, 0x04 });
 when(coderTable[4].coder.decode(inputStream, coder, new byte[0]))
 .thenReturn(new ByteArrayInputStream(new byte[] { 0x01, 0x05, 0x02, 0x05 }));

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, new byte[0]);
 InputStream decoderOutput = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), coder, new byte[0]);
 assertNotNull(decoderOutput);
 assertArrayEquals(new byte[] { 0x01, 0x05, 0x02, 0x05 }, decoderOutput.readAllBytes());
 }

 @Test
 public void testAddEncoderUnsupportedMethod() throws IOException {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod method = SevenZMethod.BZIP2;
 when(method.equals(SevenZMethod.BZIP2)).thenReturn(true);
 when(outputStream.flush()).thenReturn(true);

 assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, method, new byte[0]));
 }

 @Test
 public void testAddEncoderSupportedMethod() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateDecoder();
 CoderId coderId = coderTable[2];
 when(coderId.method.equals(method)).thenReturn(true);
 when(coder.encode(outputStream, new byte[0])).thenReturn(outputStream);

 Coders.addEncoder(outputStream, method, new byte[0]);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 InputStream decoderOutput = Coders.addDecoder(inputStream, coder, new byte[0]);
 assertNotNull(decoderOutput);
 assertArrayEquals(new byte[0], decoderOutput.readAllBytes());
 }

 @Test
 public void testLZMADecoderWithInvalidDictSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x04, 0x01, 0x01, 0x00, 0x05, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x0A, 0x00, 0x0B, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF });
 Coder coder = new LZMADecoder();
 CoderId coderId = coderTable[1];

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, new byte[0]));
 }

 @Test
 public void testAES256SHA256DecoderWithInvalidSaltSizeAndIVSize() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x04, 0x01, 0x01, 0x00, 0x05, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x0A, 0x00, 0x0B, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x0E, 0x00, 0x0F, 0x00 });
 Coder coder = new AES256SHA256Decoder();
 CoderId coderId = coderTable[5];
 byte[] passwordBytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, passwordBytes));
 }

 @Test
 public void testAES256SHA256DecoderWithValidSaltSizeAndIVSize() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x04, 0x01, 0x01, 0x00, 0x05, 0x00, 0x02, 0x00, 0x03, 0x00, 0x04, 0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x08, 0x00, 0x09, 0x00, 0x0A, 0x00, 0x0B, 0x00, 0x0C, 0x00, 0x0D, 0x00, 0x0E, 0x00, 0x0F, 0x00, 0x10, 0x00, 0x11, 0x00 });
 Coder coder = new AES256SHA256Decoder();
 CoderId coderId = coderTable[5];
 byte[] passwordBytes = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 Coders.addEncoder(outputStream, SevenZMethod.AES256SHA256, passwordBytes);
 InputStream decoderOutput = Coders.addDecoder(new ByteArrayInputStream(outputStream.toByteArray()), coder, passwordBytes);
 assertNotNull(decoderOutput);
 assertArrayEquals(new byte[] { 0x01, 0x05, 0x02, 0x05 }, decoderOutput.readAllBytes());
 }
}