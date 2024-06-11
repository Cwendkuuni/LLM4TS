import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Base64InputStreamTest {

 private Base64InputStream inputStream;
 private Base64OutputStream outputStream;
 private byte[] testData;

 @Before
 public void setUp() {
 testData = "HelloWorld12345!@#$%^&*()_ plus + sign".getBytes(StandardCharsets.UTF_8);
 outputStream = new Base64OutputStream(64, Base64.LINE_BREAK_NONE);
 outputStream.write(testData, 0, testData.length);
 testData = outputStream.toByteArray();
 inputStream = new Base64InputStream(new ByteArrayInputStream(testData));
 }

 @Test
 public void testDefaultConstructor() {
 // Test default decoding functionality
 byte[] readData = new byte[inputStream.available()];
 inputStream.read(readData, 0, readData.length);
 Assert.assertArrayEquals(testData, readData);
 }

 @Test
 public void testDecodeConstructor() {
 // Test decoding functionality with custom InputStream
 ByteArrayInputStream customInputStream = new ByteArrayInputStream(testData);
 Base64InputStream decoder = new Base64InputStream(customInputStream, false);
 byte[] readData = new byte[decoder.available()];
 decoder.read(readData, 0, readData.length);
 Assert.assertArrayEquals(testData, readData);
 }

 @Test
 public void testEncodeConstructor() {
 // Test encoding functionality with custom InputStream
 Base64 base64 = new Base64(64, Base64.LINE_BREAK_NONE);
 ByteArrayOutputStream customOutputStream = new ByteArrayOutputStream();
 Base64OutputStream encoder = new Base64OutputStream(customOutputStream, base64);
 encoder.write(testData, 0, testData.length);
 byte[] encodedData = customOutputStream.toByteArray();
 Base64InputStream inputStreamEncoded = new Base64InputStream(new ByteArrayInputStream(encodedData), true);
 byte[] readDataEncoded = new byte[inputStreamEncoded.available()];
 inputStreamEncoded.read(readDataEncoded, 0, readDataEncoded.length);
 Assert.assertArrayEquals(encodedData, readDataEncoded);
 }

 @Test(expected = IOException.class)
 public void testExceptionMarkSupported() throws IOException {
 inputStream.mark(1);
 }

 @Test
 public void testReadByte() throws IOException {
 int readByte = inputStream.read();
 Assert.assertNotNull(readByte);
 }

 @Test
 public void testReadBytes() throws IOException {
 byte[] readBytes = new byte[5];
 int readLength = inputStream.read(readBytes, 0, readBytes.length);
 Assert.assertEquals(readLength, 5);
 Assert.assertArrayEquals(Arrays.copyOfRange(testData, 0, 5), readBytes);
 }

 @Test(expected = IOException.class)
 public void testReadBeyondData() throws IOException {
 byte[] readDataBeyond = new byte[inputStream.available() + 1];
 inputStream.read(readDataBeyond, 0, readDataBeyond.length);
 }

 @Test
 public void testReadNegativeDataLength() throws IOException {
 byte[] readDataNegative = new byte[inputStream.available() * -1];
 inputStream.read(readDataNegative, 0, readDataNegative.length);
 }

 @Test
 public void testReadWithZeroDataLength() throws IOException {
 byte[] readDataZero = new byte[0];
 int readLengthZero = inputStream.read(readDataZero, 0, readDataZero.length);
 Assert.assertEquals(readLengthZero, 0);
 }

 @Test
 public void testReadWithCustomLineLength() throws IOException {
 Base64 base64Custom = new Base64(48, Base64.LINE_BREAK_CRLF);
 ByteArrayOutputStream customOutputStream = new ByteArrayOutputStream();
 Base64OutputStream encoderCustom = new Base64OutputStream(customOutputStream, base64Custom);
 encoderCustom.write(testData, 0, testData.length);
 byte[] encodedDataCustom = customOutputStream.toByteArray();
 Base64InputStream inputStreamCustom = new Base64InputStream(new ByteArrayInputStream(encodedDataCustom), true, 48, Base64.LINE_BREAK_CRLF);
 byte[] readDataCustom = new byte[4];
 int readLengthCustom = inputStreamCustom.read(readDataCustom, 0, readDataCustom.length);
 Assert.assertEquals(readLengthCustom, 4);
 Assert.assertArrayEquals(Arrays.copyOfRange(testData, 0, 4), readDataCustom);
 }

 @Test
 public void testReadWithCustomLineSeparator() throws IOException {
 Base64 base64Custom = new Base64(64, new byte[]{(byte) 0x0D, (byte) 0x0A});
 ByteArrayOutputStream customOutputStream = new ByteArrayOutputStream();
 Base64OutputStream encoderCustom = new Base64OutputStream(customOutputStream, base64Custom);
 encoderCustom.write(testData, 0, testData.length);
 byte[] encodedDataCustom = customOutputStream.toByteArray();
 Base64InputStream inputStreamCustom = new Base64InputStream(new ByteArrayInputStream(encodedDataCustom), true, 0, new byte[]{(byte) 0x0D, (byte) 0x0A});
 byte[] readDataCustom = new byte[4];
 int readLengthCustom = inputStreamCustom.read(readDataCustom, 0, readDataCustom.length);
 Assert.assertEquals(readLengthCustom, 4);
 Assert.assertArrayEquals(Arrays.copyOfRange(testData, 0, 4), readDataCustom);
 }
}