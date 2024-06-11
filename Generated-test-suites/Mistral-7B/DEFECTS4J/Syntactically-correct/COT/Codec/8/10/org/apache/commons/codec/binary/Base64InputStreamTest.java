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
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

 private Base64InputStream inputStream;
 private ByteArrayInputStream inputStreamSource;
 private ByteArrayOutputStream outputStream;
 private Base64 base64;

 @Before
 public void setUp() {
 inputStreamSource = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
 inputStream = new Base64InputStream(inputStreamSource, false);
 outputStream = new ByteArrayOutputStream();
 base64 = new Base64();
 }

 /**
 * Test constructor Base64InputStream(InputStream in)
 */
 @Test
 public void testBase64InputStreamConstructor1() throws IOException {
 int read = inputStream.read();
 Assert.assertThat(read, not(-1));
 Assert.assertThat(read, is(oneOf(Base64.BASE64_ENCODED_BYTE_ARRAY[0], Base64.BASE64_ENCODED_BYTE_ARRAY[1])));
 }

 /**
 * Test constructor Base64InputStream(InputStream in, boolean doEncode)
 */
 @Test
 public void testBase64InputStreamConstructor2() throws IOException {
 int read = inputStream.read();
 Assert.assertThat(read, is(oneOf(inputStreamSource.read(), inputStreamSource.read(), inputStreamSource.read())));

 inputStream = new Base64InputStream(inputStreamSource, true);
 int encodedByte = inputStream.read();
 Assert.assertThat(encodedByte, is(oneOf(Base64.BASE64_ENCODED_BYTE_ARRAY[0], Base64.BASE64_ENCODED_BYTE_ARRAY[1])));
 }

 /**
 * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
 */
 @Test
 public void testBase64InputStreamConstructor3() throws IOException {
 inputStream = new Base64InputStream(inputStreamSource, false, 76, new byte[]{'\r', '\n'});

 ByteArrayOutputStream outputStreamLineEnding = new ByteArrayOutputStream();
 ByteArrayOutputStream outputStreamNoLineEnding = new ByteArrayOutputStream();

 OutputStream outputStreamLineEndingWrapper = new Base64OutputStream(outputStreamLineEnding, 76, '\r', '\n');
 OutputStream outputStreamNoLineEndingWrapper = new Base64OutputStream(outputStreamNoLineEnding);

 byte[] bytesToWrite = new byte[]{"Test".getBytes(), '\r', '\n'};
 int bytesWritten = 0;
 while (bytesWritten < bytesToWrite.length) {
 inputStream.writeTo(outputStreamLineEndingWrapper);
 outputStreamLineEndingWrapper.flush();
 bytesWritten += outputStreamLineEnding.size();

 inputStream.writeTo(outputStreamNoLineEndingWrapper);
 outputStreamNoLineEndingWrapper.flush();
 bytesWritten += outputStreamNoLineEnding.size();
 }

 byte[] lineEndingEncodedBytes = new byte[]{"=".getBytes()};
 byte[] noLineEndingEncodedBytes = new byte[bytesToWrite.length + bytesToWrite.length / 3];

 Assert.assertArrayEquals(base64.decode(outputStreamLineEnding.toByteArray()), base64.decode(bytesToWrite));
 Assert.assertArrayEquals(base64.decode(outputStreamNoLineEnding.toByteArray()), base64.decode(bytesToWrite));

 Assert.assertArrayEquals(outputStreamLineEnding.toByteArray(), base64.decode(outputStreamLineEnding.toByteArray()));
 Assert.assertArrayEquals(Arrays.copyOfRange(base64.decode(outputStreamLineEnding.toByteArray()), bytesToWrite.length, bytesToWrite.length + lineEndingEncodedBytes.length), lineEndingEncodedBytes);

 Assert.assertArrayEquals(outputStreamNoLineEnding.toByteArray(), base64.decode(outputStreamNoLineEnding.toByteArray()));
 Assert.assertArrayEquals(Arrays.copyOfRange(base64.decode(outputStreamNoLineEnding.toByteArray()), bytesToWrite.length, bytesToWrite.length + noLineEndingEncodedBytes.length), noLineEndingEncodedBytes);
 }

 /**
 * Test method read()
 */
 @Test
 public void testRead() throws IOException {
 Assert.assertThat(inputStream.read(), is(not(-1)));
 }

 /**
 * Test method read(byte[], int, int)
 */
 @Test
 public void testReadByteArray() throws IOException {
 byte[] expected = new byte[]{"Test".getBytes()};
 byte[] actual = new byte[expected.length];
 int read = inputStream.read(actual);

 Assert.assertThat(read, is(expected.length));
 Assert.assertArrayEquals(expected, actual);
 }

 /**
 * Test method markSupported()
 */
 @Test
 public void testMarkSupported() {
 Assert.assertThat(inputStream.markSupported(), is(false));
 }

 /**
 * Test method writeTo(OutputStream)
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testWriteTo() throws IOException {
 outputStream.write(Base64.BASE64_ENCODED_BYTE_ARRAY[0]);
 inputStream.writeTo(outputStream);
 }
}