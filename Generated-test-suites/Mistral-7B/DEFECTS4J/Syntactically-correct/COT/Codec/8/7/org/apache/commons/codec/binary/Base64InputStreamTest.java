import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.IOExceptionAdapter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

 private Base64InputStream inputStream;
 private ByteArrayInputStream input;
 private ByteArrayOutputStream output;
 private Base64 base64;

 @Before
 public void setup() {
 input = new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43, 0x44, 0x45});
 output = new ByteArrayOutputStream();
 base64 = new Base64(false);
 }

 /**
 * Test constructor Base64InputStream(InputStream in)
 */
 @Test
 public void testBase64InputStream_InputStream() throws IOException {
 inputStream = new Base64InputStream(input);
 byte[] buffer = new byte[4];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG8gZXhhbXBsZS4=", output.toString());
 }

 /**
 * Test constructor Base64InputStream(InputStream in, boolean doEncode)
 */
 @Test
 public void testBase64InputStream_InputStream_boolean() throws IOException {
 inputStream = new Base64InputStream(input, true);
 byte[] buffer = new byte[4];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG8gZXhhbXBsZS4=\r\n", output.toString());
 }

 /**
 * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
 */
 @Test
 public void testBase64InputStream_InputStream_boolean_int_byteArray() throws IOException {
 inputStream = new Base64InputStream(input, true, 76, new byte[]{0x0D, 0x0A});
 byte[] buffer = new byte[12];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG8gZXhhbXBsZS4=\r\nVG\r\n", output.toString());
 }

 /**
 * Test method read()
 */
 @Test
 public void testRead() throws IOException {
 inputStream = new Base64InputStream(input);
 int length = 0;
 byte testByte = 0;
 while ((length = inputStream.read()) != -1) {
 testByte = (byte) length;
 System.out.print(testByte);
 }
 Assert.assertEquals("VG8gZXhhbXBsZS4=", new String(input.getBytes(), 0, input.available()));
 }

 /**
 * Test method read(byte[], int, int)
 */
 @Test
 public void testRead_byteArray_int_int() throws IOException {
 inputStream = new Base64InputStream(input);
 byte[] buffer = new byte[10];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG8gZXhhbXBsZS4=", output.toString());
 }

 /**
 * Test method markSupported()
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testMarkSupported() throws IOException {
 inputStream = new Base64InputStream(input);
 inputStream.markSupported();
 }

 /**
 * Test exception handling for IOException
 */
 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 input = new ByteArrayInputStream(new byte[0]);
 inputStream = new Base64InputStream(input);
 Assert.assertThrows(IOException.class, () -> inputStream.read(new byte[1], 0, 1));
 }

 /**
 * Test exception handling for NullPointerException
 */
 @Test(expected = NullPointerException.class)
 public void testNullPointerException() throws IOException {
 inputStream = new Base64InputStream(input);
 Assert.assertThrows(NullPointerException.class, () -> inputStream.read(null, 0, 1));
 }

 /**
 * Test exception handling for IndexOutOfBoundsException
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsException() throws IOException {
 inputStream = new Base64InputStream(input);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(new byte[1], -1, 1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(new byte[1], 1, -1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> inputStream.read(new byte[1], 1, 100));
 }

 /**
 * Test edge case for read(byte[], int, int) when buffer is not big enough
 */
 @Test
 public void testReadByteArrayNotBigEnough() throws IOException {
 inputStream = new Base64InputStream(input);
 byte[] buffer = new byte[2];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG8g", output.toString());
 }

 /**
 * Test edge case for read(byte[], int, int) when InputStream has less data than buffer can hold
 */
 @Test
 public void testReadByteArrayLessData() throws IOException {
 input = new ByteArrayInputStream(new byte[2]);
 inputStream = new Base64InputStream(input);
 byte[] buffer = new byte[10];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("VG", output.toString());
 }

 /**
 * Test edge case for read(byte[], int, int) when InputStream has no data left
 */
 @Test
 public void testReadByteArrayNoDataLeft() throws IOException {
 input = new ByteArrayInputStream(new byte[0]);
 inputStream = new Base64InputStream(input);
 byte[] buffer = new byte[10];
 int len;
 while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
 output.write(buffer, 0, len);
 }
 Assert.assertEquals("", output.toString());
 }
}