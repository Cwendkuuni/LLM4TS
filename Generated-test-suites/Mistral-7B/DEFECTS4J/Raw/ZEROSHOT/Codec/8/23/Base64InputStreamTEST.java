package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test file for Base64InputStream class
 */
public class Base64InputStreamTest {

 private Base64InputStream inputStream;
 private ByteArrayInputStream inputStreamSource;
 private ByteArrayOutputStream outputStream;
 private Base64 base64;

 @Before
 public void setUp() {
 base64 = new Base64();
 }

 /**
 * Test constructor with default decoding behavior
 */
 @Test
 public void testDefaultDecoder() throws IOException {
 inputStreamSource = new ByteArrayInputStream("SGVsbG8sIFdvcmxkIQ==".getBytes());
 inputStream = new Base64InputStream(inputStreamSource);
 Assert.assertEquals("Vm9sbG93bWQs", inputStream.readLine());
 inputStream.close();
 }

 /**
 * Test constructor with custom decoding behavior and line length
 */
 @Test
 public void testCustomDecoderWithLineLength() throws IOException {
 byte[] inputData = "VG8gZXhhbXBsZSBoYXZlIGlzIG1hbnQgYWxsIG9mIHBhc3Npb24=".getBytes();
 inputStreamSource = new ByteArrayInputStream(inputData);
 inputStream = new Base64InputStream(inputStreamSource, false, 76);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] outputData = new byte[inputData.length];
 int readBytes = 0;
 while ((readBytes = inputStream.read(outputData, 0, outputData.length)) > 0) {
 outputStream.write(outputData, 0, readBytes);
 outputStream.flush();
 }
 inputStream.close();
 outputStream.close();
 Assert.assertEquals(new String(inputData), new String(outputStream.toByteArray()));
 }

 /**
 * Test constructor with encoding behavior
 */
 @Test
 public void testEncoder() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String inputData = "Hello, World!";
 byte[] inputBytes = inputData.getBytes();
 inputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true);
 int readBytes = 0;
 while ((readBytes = inputStream.read(singleByte, 0, 1)) > 0) {
 outputStream.write(singleByte, 0, 1);
 }
 inputStream.close();
 outputStream.flush();
 Assert.assertEquals("SGVsbG8sIHdvcmxkIQ==", new String(outputStream.toByteArray()));
 }

 /**
 * Test custom encoding behavior and line length
 */
 @Test
 public void testCustomEncoderWithLineLength() throws IOException {
 String inputData = "Hello, World!";
 byte[] inputBytes = inputData.getBytes();
 inputStreamSource = new ByteArrayInputStream(inputBytes);
 inputStream = new Base64InputStream(inputStreamSource, true, 76);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 int readBytes = 0;
 while ((readBytes = inputStream.read(singleByte, 0, 1)) > 0) {
 outputStream.write(singleByte, 0, 1);
 }
 inputStream.close();
 outputStream.flush();
 Assert.assertEquals("VG8gZXhhbXBsZSBoYXZlIGlzIG1hbnQgYWxsIG9mIHBhc3Npb24=\r\n", new String(outputStream.toByteArray()));
 }
}