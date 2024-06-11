import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for Base64InputStream class.
 */
public class Base64InputStreamTest {

 private Base64InputStream base64InputStream;
 private ByteArrayOutputStream byteArrayOutputStream;
 private ByteArrayInputStream byteArrayInputStream;
 private Base64 base64;

 @Before
 public void setUp() {
 base64 = new Base64();
 }

 /**
 * Test constructor when doEncode is false.
 */
 @Test
 public void testBase64InputStreamConstructorWithDoEncodeFalse() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
 Assert.assertFalse(base64InputStream.doEncode);
 }

 /**
 * Test constructor when doEncode is true.
 */
 @Test
 public void testBase64InputStreamConstructorWithDoEncodeTrue() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true);
 Assert.assertTrue(base64InputStream.doEncode);
 }

 /**
 * Test read method when InputStream has data.
 * @throws IOException
 */
 @Test
 public void testReadMethodWithDataInInputStream() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("TestData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 base64InputStream = new Base64InputStream(byteArrayInputStream);

 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 Assert.assertEquals(byteArrayOutputStream.size(), read);
 }

 /**
 * Test read method when InputStream is empty.
 * @throws IOException
 */
 @Test
 public void testReadMethodWithEmptyInputStream() throws IOException {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));
 byte[] result = new byte[10];
 int read = base64InputStream.read(result);
 Assert.assertEquals(-1, read);
 }

 /**
 * Test read method when InputStream has invalid data.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testReadMethodWithInvalidDataInInputStream() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("InvalidData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 }

 /**
 * Test read method when InputStream has non base64 data.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testReadMethodWithNonBase64DataInInputStream() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write(new byte[]{'A', 'B', 0x80, 'C', 'D'});
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 }

 /**
 * Test read method when InputStream has no data and lineLength is not set.
 */
 @Test
 public void testReadMethodWhenInputStreamHasNoDataAndLineLengthIsNotSet() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true);
 byte[] result = new byte[10];
 int read = base64InputStream.read(result);
 Assert.assertEquals(-1, read);
 }

 /**
 * Test read method when InputStream has no data and lineLength is set.
 */
 @Test
 public void testReadMethodWhenInputStreamHasNoDataAndLineLengthIsSet() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true, 10, new byte[]{'\r', '\n'});
 byte[] result = new byte[10];
 int read = base64InputStream.read(result);
 Assert.assertEquals(-1, read);
 }

 /**
 * Test read method when InputStream has data and lineLength is not set.
 */
 @Test
 public void testReadMethodWhenInputStreamHasDataAndLineLengthIsNotSet() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("TestData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 base64InputStream = new Base64InputStream(byteArrayInputStream);
 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 Assert.assertEquals(byteArrayOutputStream.size(), read);
 }

 /**
 * Test read method when InputStream has data and lineLength is set.
 */
 @Test
 public void testReadMethodWhenInputStreamHasDataAndLineLengthIsSet() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("TestData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 base64 = new Base64(76, new byte[]{'\\r', '\\n'});
 Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, true, 76, new byte[]{'\\r', '\\n'});
 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 Assert.assertEquals(byteArrayOutputStream.size(), read);
 }

 /**
 * Test read method when InputStream has data and lineLength is set but data length is less than lineLength.
 */
 @Test
 public void testReadMethodWhenInputStreamHasDataAndLineLengthIsMoreThanDataLength() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("TestData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 base64 = new Base64(76, new byte[]{'\\r', '\\n'});
 Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, true, 76, new byte[]{'\\r', '\\n'});
 byte[] result = new byte[byteArrayOutputStream.size()];
 int read = base64InputStream.read(result);
 Assert.assertEquals(byteArrayOutputStream.size(), read);
 }

 /**
 * Test close method.
 */
 @Test
 public void testCloseMethod() throws IOException {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), false);
 base64InputStream.close();
 }

 /**
 * Test close method when InputStream has data.
 */
 @Test
 public void testCloseMethodWhenInputStreamHasData() throws IOException {
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayOutputStream.write("TestData".getBytes());
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 base64InputStream = new Base64InputStream(byteArrayInputStream);
 base64InputStream.close();
 }

 /**
 * Test close method when InputStream has no data and lineLength is not set.
 */
 @Test
 public void testCloseMethodWhenInputStreamHasNoDataAndLineLengthIsNotSet() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true);
 base64InputStream.close();
 }

 /**
 * Test close method when InputStream has no data and lineLength is set.
 */
 @Test
 public void testCloseMethodWhenInputStreamHasNoDataAndLineLengthIsSet() {
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true, 10, new byte[]{'\r', '\n'});
 base64InputStream.close();
 }
}