package org.apache.commons.codec.binary.test;

 import org.apache.commons.codec.binary.Base64;
 import org.apache.commons.codec.binary.Base64InputStream;
 import org.apache.commons.codec.binary.Base64OutputStream;
 import org.junit.Assert;
 import org.junit.Test;

 import java.io.ByteArrayInputStream;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.nio.charset.StandardCharsets;

 /**
 * Test class for Base64InputStream
 */
 public class Base64InputStreamTest {

 private static final String TEST_DATA = "Test data for Base64InputStream test";

 @Test
 public void testBase64InputStreamConstructorWithDefaults() {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 Assert.assertFalse(base64InputStream.markSupported());
 }

 @Test
 public void testBase64InputStreamConstructorWithDecode() {
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()), false);
 Assert.assertFalse(base64InputStream.markSupported());

 byte[] buffer = new byte[TEST_DATA.length()];
 int length = base64InputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(TEST_DATA.length(), length);
 String decodedString = new String(buffer, StandardCharsets.UTF_8);
 Assert.assertEquals(TEST_DATA, decodedString);
 }

 @Test
 public void testBase64InputStreamConstructorWithEncode() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);
 base64OutputStream.write(TEST_DATA.getBytes());
 base64OutputStream.finish();
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), true);
 Assert.assertFalse(base64InputStream.markSupported());

 byte[] buffer = new byte[TEST_DATA.length()];
 int length = base64InputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(TEST_DATA.length(), length);
 String encodedString = new String(buffer, StandardCharsets.UTF_8);
 Assert.assertEquals(base64OutputStream.toString(), encodedString);
 }

 @Test
 public void testBase64InputStreamConstructorWithEncodeLineLengthAndLineSeparator() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, '\r', '\n', 76);
 base64OutputStream.write(TEST_DATA.getBytes());
 base64OutputStream.finish();
 Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), true, 76, new byte[]{'\\r', '\\n'});
 Assert.assertFalse(base64InputStream.markSupported());

 byte[] buffer = new byte[TEST_DATA.length()];
 int length = base64InputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(TEST_DATA.length(), length);
 String encodedString = new String(buffer, StandardCharsets.UTF_8);
 Assert.assertEquals(base64OutputStream.toString(), encodedString);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithInvalidInput() throws IOException {
 Base64InputStream base64InputStream = new Base64InputStream(null);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithInvalidInputAndDecode() throws IOException {
 Base64InputStream base64InputStream = new Base64InputStream(null, false);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamWithInvalidInputAndEncode() throws IOException {
 Base64InputStream base64InputStream = new Base64InputStream(null, true);
 }
}