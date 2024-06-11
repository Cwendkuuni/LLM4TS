import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.tukaani.xz.LZMAInputStream;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecretKey;
import java.security.spec.SecretKeySpec;

public class CodersTest {

 @org.junit.Test
 public void testAddDecoder_CopyDecoder() throws IOException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final Coder copyDecoder = new CopyDecoder();
 final byte[] copyDecoderPassword = new byte[0];
 final InputStream inputStream = new ByteArrayInputStream(new byte[10]);

 final InputStream result = Coders.addDecoder(inputStream, copyDecoder, copyDecoderPassword);
 Assert.assertEquals(inputStream, result);
 }

 @org.junit.Test
 public void testAddDecoder_LZMADecoder() throws IOException, NoSuchAlgorithmException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final Coder lzmaDecoder = new LZMADecoder();
 final byte[] lzmaDecoderPassword = new byte[0];
 final InputStream inputStream = new ByteArrayInputStream(new byte[10]);

 final InputStream result = Coders.addDecoder(inputStream, lzmaDecoder, lzmaDecoderPassword);
 Assert.assertTrue(result instanceof LZMAInputStream);
 }

 @org.junit.Test(expected = IOException.class)
 public void testAddDecoder_UnsupportedMethod() throws IOException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final Coder unsupportedMethod = new Object() {}; // Unsupported Coder
 final byte[] unsupportedMethodPassword = new byte[0];
 final InputStream inputStream = new ByteArrayInputStream(new byte[10]);

 Coders.addDecoder(inputStream, unsupportedMethod, unsupportedMethodPassword);
 }

 @org.junit.Test
 public void testAddEncoder_CopyEncoder() throws IOException {
 final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[10]);
 final OutputStream outputStream = new ByteArrayOutputStream();
 final Coder copyEncoder = new CopyDecoder();
 final byte[] copyEncoderPassword = new byte[0];

 final OutputStream result = Coders.addEncoder(outputStream, SevenZMethod.COPY, copyEncoderPassword);
 Assert.assertEquals(outputStream, result);
 }

 @org.junit.Test(expected = IOException.class)
 public void testAddEncoder_UnsupportedMethod() throws IOException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final SevenZMethod unsupportedMethod = new Object() {}; // Unsupported SevenZMethod
 final byte[] unsupportedMethodPassword = new byte[0];

 Coders.addEncoder(outputStream, unsupportedMethod, unsupportedMethodPassword);
 }

 @org.junit.Test
 public void testDecode_CopyDecoder() throws IOException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final byte[] source = "CopyDecoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder copyDecoder = new CopyDecoder();
 final byte[] copyDecoderPassword = new byte[0];

 final InputStream inputStream = new ByteArrayInputStream(source);
 final InputStream decodedStream = Coders.addDecoder(inputStream, copyDecoder, copyDecoderPassword);
 final byte[] decodedBytes = new byte[source.length];
 final int length = decodedStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertArrayEquals(source, decodedBytes);
 }

 @org.junit.Test
 public void testDecode_LZMADecoder() throws IOException, GeneralSecurityException, NoSuchAlgorithmException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final byte[] source = "LZMADecoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder lzmaDecoder = new LZMADecoder();
 final byte[] lzmaDecoderPassword = new byte[0];

 final InputStream inputStream = new ByteArrayInputStream(source);
 final InputStream decodedStream = Coders.addDecoder(inputStream, lzmaDecoder, lzmaDecoderPassword);
 final byte[] decodedBytes = new byte[source.length];
 final int length = decodedStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertArrayEquals(source, decodedBytes);
 }

 @org.junit.Test
 public void testDecode_BZIP2Decoder() throws IOException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final ByteArrayOutputStream bzip2OutputStream = new ByteArrayOutputStream();
 final byte[] source = "BZIP2DecoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder bzip2Decoder = new BZIP2Decoder();
 final byte[] bzip2DecoderPassword = new byte[0];

 final OutputStream bzip2EncoderStream = new BZip2CompressorOutputStream(outputStream);
 final OutputStream decoderStream = Coders.addEncoder(bzip2EncoderStream, SevenZMethod.BZIP2, bzip2DecoderPassword);
 final byte[] buffer = new byte[1024];
 final int length = -1;

 while (length > 0) {
 length = bzip2EncoderStream.write(source, 0, source.length);
 length = decoderStream.write(buffer, 0, buffer.length);
 }

 final InputStream decodedStream = new ByteArrayInputStream(outputStream.toByteArray());
 final byte[] decodedBytes = new byte[source.length];
 final int decodedLength = decodedStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertArrayEquals(source, decodedBytes);
 }

 @org.junit.Test
 public void testDecode_AES256SHA256Decoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 final byte[] input = "AES256SHA256DecoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder aes256sha256Decoder = new AES256SHA256Decoder();
 final byte[] aes256sha256DecoderPassword = new byte[16];

 final InputStream decodedStream = Coders.addDecoder(inputStream, aes256sha256Decoder, aes256sha256DecoderPassword);
 final byte[] decodedBytes = new byte[input.length];
 final int length = decodedStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertArrayEquals(input, decodedBytes);

 final MessageDigest digest = MessageDigest.getInstance("SHA-256");
 final byte[] key = "AES256SHA256DecoderTestKey".getBytes(StandardCharsets.UTF_8);
 digest.update(key);
 final byte[] password = new byte[16];
 System.arraycopy(digest.digest(), 0, password, 0, 16);

 final byte[] salt = new byte[16];
 final byte[] iv = new byte[16];
 final byte[] encryptedData = new byte[input.length + 2 * (salt.length + iv.length)];
 System.arraycopy(input, 0, encryptedData, 0, input.length);
 final byte extraByte = (byte) 0x01;
 System.arraycopy(salt, 0, encryptedData, input.length, salt.length);
 System.arraycopy(iv, 0, encryptedData, input.length + salt.length, iv.length);
 System.arraycopy(new byte[] {extraByte}, 0, encryptedData, input.length + salt.length + iv.length, 1);

 final InputStream encryptedStream = new ByteArrayInputStream(encryptedData);
 final InputStream decryptedStream = Coders.addDecoder(encryptedStream, aes256sha256Decoder, aes256sha256DecoderPassword);
 final byte[] decryptedBytes = new byte[input.length];
 final int decryptedLength = decryptedStream.read(decryptedBytes, 0, decryptedBytes.length);
 Assert.assertArrayEquals(input, decryptedBytes);
 }

 @org.junit.Test
 public void testEncode_CopyEncoder() throws IOException {
 final ByteArrayInputStream inputStream = new ByteArrayInputStream("CopyEncoderTest".getBytes(StandardCharsets.UTF_8));
 final OutputStream outputStream = new ByteArrayOutputStream();
 final Coder copyEncoder = new CopyDecoder();
 final byte[] copyEncoderPassword = new byte[0];

 final OutputStream encodedStream = Coders.addEncoder(outputStream, SevenZMethod.COPY, copyEncoderPassword);
 final byte[] buffer = new byte[1024];
 final int length = -1;

 while ((length = inputStream.read(buffer)) > 0) {
 encodedStream.write(buffer, 0, length);
 }

 final InputStream encodedStreamResult = new ByteArrayInputStream(outputStream.toByteArray());
 final byte[] encodedBytes = new byte[inputStream.available()];
 final int encodedLength = encodedStreamResult.read(encodedBytes, 0, encodedBytes.length);
 Assert.assertArrayEquals(inputStream.readAllBytes(), encodedBytes);
 }

 @org.junit.Test
 public void testEncode_BZIP2Encoder() throws IOException {
 final ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
 final ByteArrayOutputStream bzip2OutputStream = new ByteArrayOutputStream();
 final byte[] source = "BZIP2EncoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder bzip2Encoder = new BZIP2Decoder();
 final byte[] bzip2EncoderPassword = new byte[0];

 final OutputStream inputStreamWriter = Coders.addEncoder(inputStream, SevenZMethod.BZIP2, bzip2EncoderPassword);
 final OutputStream bzip2EncoderStream = Coders.addEncoder(inputStreamWriter, SevenZMethod.BZIP2, bzip2EncoderPassword);
 final OutputStream encodedStream = Coders.addEncoder(bzip2EncoderStream, SevenZMethod.BZIP2, bzip2EncoderPassword);
 final byte[] buffer = new byte[1024];
 final int length = -1;

 while (length > 0) {
 length = inputStream.write(source, 0, source.length);
 length = encodedStream.write(buffer, 0, buffer.length);
 }

 final InputStream encodedStreamResult = new ByteArrayInputStream(bzip2OutputStream.toByteArray());
 final byte[] encodedBytes = new byte[source.length];
 final int encodedLength = encodedStreamResult.read(encodedBytes, 0, encodedBytes.length);
 Assert.assertArrayEquals(source, encodedBytes);
 }

 @org.junit.Test
 public void testEncode_AES256SHA256Encoder() throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
 final ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
 final ByteArrayOutputStream aes256sha256OutputStream = new ByteArrayOutputStream();
 final byte[] source = "AES256SHA256EncoderTest".getBytes(StandardCharsets.UTF_8);
 final Coder aes256sha256Encoder = new AES256SHA256Decoder();
 final byte[] aes256sha256EncoderPassword = new byte[16];

 final OutputStream inputStreamWriter = Coders.addEncoder(inputStream, SevenZMethod.AES256SHA256, aes256sha256EncoderPassword);
 final OutputStream aes256sha256EncoderStream = Coders.addEncoder(inputStreamWriter, SevenZMethod.AES256SHA256, aes256sha256EncoderPassword);
 final OutputStream encodedStream = Coders.addEncoder(aes256sha256EncoderStream, SevenZMethod.AES256SHA256, aes256sha256EncoderPassword);
 final byte[] buffer = new byte[1024];
 final int length = -1;

 while (length > 0) {
 length = inputStream.write(source, 0, source.length);
 length = encodedStream.write(buffer, 0, buffer.length);
 }

 final InputStream encodedStreamResult = new ByteArrayInputStream(aes256sha256OutputStream.toByteArray());
 final byte[] encodedBytes = new byte[source.length];
 final int encodedLength = encodedStreamResult.read(encodedBytes, 0, encodedBytes.length);
 Assert.assertArrayEquals(source, encodedBytes);
 }
}