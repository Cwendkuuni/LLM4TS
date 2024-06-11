import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

 private Base64InputStream base64InputStream;
 private ByteArrayOutputStream byteArrayOutputStream;
 private ByteArrayInputStream byteArrayInputStream;
 private Base64 base64;

 @Before
 public void setUp() {
 base64 = new Base64();
 byteArrayOutputStream = new ByteArrayOutputStream();
 byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 }

 @Test
 public void testBase64InputStreamDecoding() throws IOException {
 // Test decoding with default constructor
 base64InputStream = new Base64InputStream(byteArrayInputStream);
 byte[] decodedBytes = new byte[1024];
 int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertEquals("Decoded data length should be equal to the input data length.", byteArrayOutputStream.size(), length);
 // Test decoding with custom constructor
 base64InputStream = new Base64InputStream(byteArrayInputStream, false, 76, "\r\n".getBytes());
 length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertEquals("Decoded data length should be equal to the input data length.", byteArrayOutputStream.size(), length);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithNullInputStream() {
 base64InputStream = new Base64InputStream(null);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithNegativeLineLength() {
 base64InputStream = new Base64InputStream(byteArrayInputStream, false, -1, null);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamDecodingWithNullLineSeparator() {
 base64InputStream = new Base64InputStream(byteArrayInputStream, false, 76, (byte[]) null);
 }

 @Test
 public void testBase64InputStreamEncoding() throws IOException {
 byte[] inputBytes = "Apache Commons Codec".getBytes();
 byteArrayOutputStream = new ByteArrayOutputStream();
 base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true);
 byteArrayInputStream = new ByteArrayInputStream(base64InputStream.toByteArray());
 int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
 Assert.assertEquals("Encoded data length should be greater than the input data length.", length, inputBytes.length * 4 / 3);
 String encodedString = new String(byteArrayOutputStream.toByteArray());
 String expectedString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLQ==";
 Assert.assertEquals("Encoded data should be equal to the expected data.", expectedString, encodedString);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamEncodingWithNullInputStream() {
 base64InputStream = new Base64InputStream(null, true);
 }
}