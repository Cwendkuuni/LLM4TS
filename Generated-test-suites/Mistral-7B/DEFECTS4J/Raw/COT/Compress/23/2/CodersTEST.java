import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.IvParameterSpec;
import java.security.spec.SecretKeySpec;

public class CodersTest {

 @Test
 public void testAddDecoder_UnknownMethodId() {
 InputStream inputStream = mock(InputStream.class);
 Coder coder = mock(Coder.class);
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04 };
 when(coder.decompressionMethodId).thenReturn(new byte[] { 0x5a, 0x5a, 0x5a });

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
 }

 @Test
 public void testAddDecoder_KnownMethodId() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
 SevenZMethod method = SevenZMethod.COPY;
 Coder coder = new CopyDecoder();
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04 };

 InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);
 assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, actualOutput.readAllBytes());
 assertNull(actualOutput);
 }

 @Test
 public void testAddEncoder_UnknownMethod() throws IOException {
 OutputStream outputStream = mock(OutputStream.class);
 SevenZMethod method = new SevenZMethod("unknown");
 Coders.addEncoder(outputStream, method, new byte[0]);

 assertThrows(IOException.class, () -> Coders.addEncoder(outputStream, method, new byte[0]));
 }

 @Test
 public void testAddEncoder_KnownMethod() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 SevenZMethod method = SevenZMethod.DEFLATE;
 Coder coder = new DeflateDecoder();
 byte[] password = new byte[0];

 OutputStream actualOutput = Coders.addEncoder(outputStream, method, password);
 assertNotNull(actualOutput);
 assertEquals(outputStream.size(), actualOutput.size());
 }

 @Test
 public void testLZMADecoder_InvalidDictSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
 }

 @Test
 public void testLZMADecoder_ValidDictSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21 });
 Coder coder = new LZMADecoder();
 byte[] password = new byte[0];

 InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);
 assertNotNull(actualOutput);
 }

 @Test
 public void testAES256SHA256Decoder_InvalidNumCyclesPower() throws IOException, NoSuchAlgorithmException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, (byte) 0x7f, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21 });
 Coder coder = new AES256SHA256Decoder();
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04 };

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
 }

 @Test
 public void testAES256SHA256Decoder_ValidNumCyclesPower() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x3f, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21 });
 Coder coder = new AES256SHA256Decoder();
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04 };

 InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);
 assertNotNull(actualOutput);
 }

 @Test
 public void testAES256SHA256Decoder_InvalidPasswordLength() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x3f, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22 });
 Coder coder = new AES256SHA256Decoder();
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04 };

 assertThrows(IOException.class, () -> Coders.addDecoder(inputStream, coder, password));
 }

 @Test
 public void testAES256SHA256Decoder_ValidPasswordLength() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x3f, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23 });
 Coder coder = new AES256SHA256Decoder();
 byte[] password = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f, 0x20, 0x21, 0x22, 0x23 };

 InputStream actualOutput = Coders.addDecoder(inputStream, coder, password);
 assertNotNull(actualOutput);
 }
}