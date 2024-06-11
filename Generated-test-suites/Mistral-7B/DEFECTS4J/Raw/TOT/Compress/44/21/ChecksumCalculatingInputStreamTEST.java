package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.io.output.StringBuilderOutputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

 private InputStream nullInputStream;
 private InputStream checksumStream;
 private ByteArrayOutputStream outputStream;
 private ChecksumCalculatingInputStream testStream;
 private Checksum checksum;
 private long expectedChecksum = 1234567890L; // arbitrary value for testing

 @Before
 public void setUp() throws Exception {
 checksum = new Adler32(); // using Adler32 checksum for testing
 nullInputStream = new NullInputStream();
 checksumStream = new ChecksumCalculatingInputStream(checksum, new NullInputStream());
 outputStream = new ByteArrayOutputStream();
 }

 /**
 * Test reading a single byte
 */
 @Test
 public void testReadByte() throws IOException {
 testStream = new ChecksumCalculatingInputStream(checksum, outputStream);
 testStream.read();
 assertEquals(0, testStream.read());
 // checksum value is undefined as we don't know what byte was read
 }

 /**
 * Test reading a byte array
 */
 @Test
 public void testReadByteArray() throws IOException {
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 testStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
 final int bytesRead = testStream.read(testData, 0, testData.length);
 assertEquals(testData.length, bytesRead);
 // checksum value should match expected value
 assertEquals(expectedChecksum, checksum.getValue());
 }

 /**
 * Test reading a byte array with offset
 */
 @Test
 public void testReadByteArrayWithOffset() throws IOException {
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 final int offset = 2;
 testStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
 final int bytesRead = testStream.read(testData, offset, testData.length - offset);
 assertEquals(testData.length - offset, bytesRead);
 // checksum value should match expected value
 assertEquals(expectedChecksum, checksum.getValue());
 }

 /**
 * Test reading a byte array with negative offset
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testReadByteArrayWithNegativeOffset() throws IOException {
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 final int offset = -1;
 testStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
 testStream.read(testData, offset, testData.length);
 }

 /**
 * Test reading a byte array with length greater than the remaining data
 */
 @Test(expected = IOException.class)
 public void testReadByteArrayWithLengthGreaterThanRemainingData() throws IOException {
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 testStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(testData));
 testStream.read(testData, 0, testData.length + 1);
 }

 /**
 * Test reading from a NullInputStream
 */
 @Test(expected = IOException.class)
 public void testReadFromNullInputStream() throws IOException {
 testStream = new ChecksumCalculatingInputStream(checksum, nullInputStream);
 testStream.read();
 }

 /**
 * Test skipping bytes
 */
 @Test
 public void testSkipBytes() throws IOException {
 testStream = new ChecksumCalculatingInputStream(checksum, outputStream);
 final int bytesToSkip = testData.length;
 testStream.skip(bytesToSkip);
 // checksum value should be the same as if we read the data
 final int expectedBytesRead = testData.length;
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 final int bytesRead = testStream.read(testData, 0, testData.length);
 assertEquals(expectedBytesRead, bytesRead);
 assertEquals(expectedChecksum, checksum.getValue());
 }

 /**
 * Test getting checksum
 */
 @Test
 public void testGetChecksum() throws IOException {
 testStream = new ChecksumCalculatingInputStream(checksum, new StringBuilderOutputStream());
 final byte[] testData = new byte[] { 1, 2, 3, 4 };
 testStream.read(testData, 0, testData.length);
 assertEquals(expectedChecksum, testStream.getValue());
 }

 /**
 * Test getting checksum with NullOutputStream
 */
 @Test
 public void testGetChecksumWithNullOutputStream() throws IOException {
 testStream = new ChecksumCalculatingInputStream(checksum, new NullOutputStream());
 assertEquals(Long.MIN_VALUE, testStream.getValue());
 }
}