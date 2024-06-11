package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

 private ChecksumCalculatingInputStream checksumInputStream;
 private Checksum checksum;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setUp() throws IOException, NoSuchAlgorithmException {
 checksum = MessageDigest.getInstance("MD5");
 checksumInputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(new byte[0]));
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(new byte[]{'A', 'B', 'C'});
 }

 /**
 * Test for read method
 */
 @Test
 public void testReadMethod() throws IOException {
 final byte testByte = 'D';
 final int expected = inputStream.read();
 final int actual = checksumInputStream.read();

 // Check that the expected byte is read from the input stream
 Assert.assertEquals("Expected 'D' but read '-1' from input stream", expected, actual);

 // Check that the read byte is added to the checksum
 checksum.update(testByte);
 Assert.assertArrayEquals("Checksums do not match", checksum.digest(), checksumInputStream.getChecksum().digest());
 }

 /**
 * Test for read method with EOF
 */
 @Test(expected = IOException.class)
 public void testReadMethodWithEOF() throws IOException {
 final int expected = inputStream.read();
 checksumInputStream.read();

 // Check that the expected EOF (-1) is thrown when reading from the input stream after EOF
 Assert.assertEquals("Expected EOF (-1) but read '0' from checksumInputStream", -1, inputStream.read());
 Assert.assertEquals("Checksum verification failed", checksum.getValue(), checksumInputStream.getValue());
 }

 /**
 * Test for read method with buffer
 */
 @Test
 public void testReadMethodWithBuffer() throws IOException {
 final byte testByte = 'D';
 final int expectedBufferSize = 3;
 final byte[] buffer = new byte[expectedBufferSize];
 final int expected = inputStream.read(buffer);
 final int actual = checksumInputStream.read(buffer);

 // Check that the expected number of bytes is read from the input stream
 Assert.assertEquals("Expected " + expected + " bytes but read " + actual + " from checksumInputStream", expected, actual);

 // Check that the read bytes are added to the checksum
 checksum.update(buffer, 0, expected);
 Assert.assertArrayEquals("Checksums do not match", checksum.digest(), checksumInputStream.getChecksum().digest());
 }

 /**
 * Test for skip method
 */
 @Test(expected = IOException.class)
 public void testSkipMethod() throws IOException {
 final long expectedNumberOfBytesToSkip = 3L;
 final long expectedNumberOfBytesRead = 1L;
 checksumInputStream.skip(expectedNumberOfBytesToSkip);
 final long actualNumberOfBytesRead = checksumInputStream.read();

 // Check that the expected number of bytes is skipped
 Assert.assertEquals("Expected to skip " + expectedNumberOfBytesToSkip + " bytes but skipped " + actualNumberOfBytesRead, expectedNumberOfBytesToSkip, checksumInputStream.getChecksum().getValue() - checksumInputStream.getChecksum().getValue() + expectedNumberOfBytesRead);

 // Check that the expected byte is read from the input stream
 Assert.assertEquals("Expected 'D' but read '0'", 'D', (char) actualNumberOfBytesRead);

 // Check that the read byte is added to the checksum
 checksum.update((byte) actualNumberOfBytesRead);
 Assert.assertArrayEquals("Checksums do not match", checksum.digest(), checksumInputStream.getChecksum().digest());
 }

 /**
 * Test for getValue method
 */
 @Test
 public void testgetValueMethod() throws IOException {
 final byte testByte = 'D';
 final int expectedBufferSize = 3;
 final byte[] buffer = new byte[expectedBufferSize];
 final int expectedNumberOfBytesToRead = 3;
 final long expectedChecksumValue = 0xABCDEF12L; // Arbitrary value

 // Read expectedNumberOfBytesToRead bytes from input stream
 for (int i = 0; i < expectedNumberOfBytesToRead; i++) {
 inputStream.read(buffer, 0, 1);
 checksum.update(buffer[0]);
 }

 // Check that the calculated checksum value matches the expected value
 Assert.assertEquals("Checksums do not match", expectedChecksumValue, checksumInputStream.getValue());
 }

 // Add more test cases for other edge cases and exception handling scenarios as necessary
}