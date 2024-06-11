import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for DeflateCompressorInputStream
 */
public class DeflateCompressorInputStreamTest {

 private InputStream sourceStream;
 private InputStream deflateStream;
 private ByteArrayOutputStream targetStream;

 @Before
 public void setUp() throws IOException {
 // Create some source data
 byte[] sourceData = {0x42, 0x5A, 0x68, 0x69, 0x6C, 0x65, 0x6E, 0x74, 0x54, 0x79, 0x70, 0x65, 0x31, 0x32, 0x33, 0x34, 0x35};

 // Create a source stream
 sourceStream = new ByteArrayInputStream(sourceData);

 // Create deflate parameters
 DeflateParameters params = new DeflateParameters();
 params.setCompressionLevel(DeflateCompressionLevel.COMPRESS_MAX);

 // Create a deflate stream
 deflateStream = new DeflateCompressorInputStream(sourceStream, params);

 // Create a target stream
 targetStream = new ByteArrayOutputStream();
 }

 /**
 * Test read method
 *
 * @throws IOException
 */
 @Test
 public void testRead() throws IOException {
 byte[] targetData = new byte[sourceData.length + 1]; // one extra byte for reading -1
 int bytesRead = 0;
 byte readByte;

 while ((readByte = (byte) deflateStream.read()) != -1) {
 targetData[bytesRead++] = readByte;
 }

 Assert.assertArrayEquals("Data read from deflate stream does not match source data", sourceData, targetData, 0, sourceData.length);
 }

 /**
 * Test read method with buffer
 *
 * @throws IOException
 */
 @Test
 public void testReadWithBuffer() throws IOException {
 byte[] targetData = new byte[sourceData.length];
 int bytesRead = deflateStream.read(targetData);

 Assert.assertEquals("Number of bytes read does not match expected", sourceData.length, bytesRead);
 Assert.assertArrayEquals("Data read from deflate stream does not match source data", sourceData, targetData, 0, sourceData.length);
 }

 /**
 * Test available method
 *
 * @throws IOException
 */
 @Test
 public void testAvailable() throws IOException {
 int available = deflateStream.available();
 int expectedAvailable = sourceData.length - deflateStream.getBytesRead() + 1;

 Assert.assertEquals("Available bytes in input stream does not match expected", expectedAvailable, available);
 }

 /**
 * Test skip method
 *
 * @throws IOException
 */
 @Test
 public void testSkip() throws IOException {
 long bytesToSkip = sourceData.length / 2;
 long skippedBytes = deflateStream.skip(bytesToSkip);

 Assert.assertEquals("Number of bytes skipped does not match expected", bytesToSkip, skippedBytes);
 }

 /**
 * Test close method
 *
 * @throws IOException
 */
 @Test
 public void testClose() throws IOException {
 deflateStream.close();

 // Check if source stream is closed
 Assert.assertFalse("Source stream should not be open after deflate stream is closed", sourceStream.isOpen());
 }

 /**
 * Test compression with zlib header
 *
 * @throws IOException
 */
 @Test
 public void testCompressionWithZlibHeader() throws IOException {
 // Create source data with zlib header
 byte[] sourceDataWithZlibHeader = new byte[]{0x78, 0x5E, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00};
 byte[] sourceDataWithoutZlibHeader = new byte[sourceData.length];
 System.arraycopy(sourceData, 0, sourceDataWithoutZlibHeader, 0, sourceData.length);

 // Create source stream with zlib header
 InputStream zlibSourceStream = new ByteArrayInputStream(sourceDataWithZlibHeader);
 DeflateParameters zlibParams = new DeflateParameters();
 zlibParams.setWithZlibHeader(true);
 InputStream zlibDeflateStream = new DeflateCompressorInputStream(zlibSourceStream, zlibParams);

 // Read data from zlib deflate stream
 byte[] targetData = new byte[sourceData.length];
 int bytesRead = zlibDeflateStream.read(targetData);

 // Check if data read from zlib deflate stream matches data without zlib header
 Assert.assertArrayEquals("Data read from zlib deflate stream does not match data without zlib header", sourceDataWithoutZlibHeader, targetData, 0, sourceData.length);

 // Check if stream position is correct
 Assert.assertEquals("Stream position should be equal to data length after reading all data", sourceData.length, zlibDeflateStream.getBytesRead());
 }
}