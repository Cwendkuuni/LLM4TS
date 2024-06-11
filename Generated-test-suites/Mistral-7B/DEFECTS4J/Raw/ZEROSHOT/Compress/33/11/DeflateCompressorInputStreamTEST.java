import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream compressorInputStream;
 private byte[] originalData = "This is a test string".getBytes();
 private byte[] expectedDecompressedData;
 private byte[] actualDecompressedData;
 private long adler32Checksum;

 @Before
 public void setUp() throws IOException {
 // Compress the original data using Deflate compression
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DeflateOutputStream deflateOutputStream = new DeflateOutputStream(outputStream);
 deflateOutputStream.write(originalData);
 deflateOutputStream.finish();
 deflateOutputStream.close();

 // Set the input stream for the compressorInputStream with the compressed data
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 compressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

 // Calculate the Adler-32 checksum for the original data
 Adler32 adler32 = new Adler32();
 adler32.update(originalData);
 adler32Checksum = adler32.value();
 }

 @Test
 public void testRead() throws IOException {
 int readByte;
 byte[] readBuffer = new byte[1];

 // Test reading a single byte
 readByte = compressorInputStream.read();
 Assert.assertNotEquals(-1, readByte);
 Assert.assertEquals(readByte, originalData[0]);

 // Test reading multiple bytes
 readBuffer = new byte[originalData.length];
 int bytesRead = compressorInputStream.read(readBuffer);
 Assert.assertEquals(bytesRead, originalData.length);
 Assert.assertArrayEquals(originalData, readBuffer);
 }

 @Test
 public void testReadPartial() throws IOException {
 byte[] readBuffer = new byte[originalData.length / 2];
 int bytesRead = compressorInputStream.read(readBuffer);
 Assert.assertNotEqual(bytesRead, -1);
 Assert.assertLessThan(bytesRead, originalData.length);
 }

 @Test
 public void testReadAll() throws IOException {
 byte[] readBuffer = new byte[originalData.length];
 int bytesRead = compressorInputStream.read(readBuffer);
 Assert.assertEquals(bytesRead, originalData.length);
 Assert.assertArrayEquals(originalData, readBuffer);
 }

 @Test
 public void testSkip() throws IOException {
 long bytesToSkip = originalData.length / 2;
 long bytesSkipped = compressorInputStream.skip(bytesToSkip);
 Assert.assertEquals(bytesSkipped, bytesToSkip);
 }

 @Test
 public void testAvailable() throws IOException {
 int bytesAvailable = compressorInputStream.available();
 Assert.assertEquals(bytesAvailable, originalData.length - compressorInputStream.getBytesRead());
 }

 @Test
 public void testClose() throws IOException {
 compressorInputStream.close();
 Assert.assertTrue(inputStream.available() > 0);
 Assert.assertFalse(compressorInputStream.isOpen());
 }

 @Test
 public void testDecompressionIntegrity() throws IOException {
 actualDecompressedData = new byte[originalData.length];
 int bytesRead = compressorInputStream.read(actualDecompressedData);
 Assert.assertEquals(bytesRead, originalData.length);
 Assert.assertArrayEquals(originalData, actualDecompressedData);

 // Check Adler-32 checksum for decompressed data
 Adler32 adler32 = new Adler32();
 adler32.update(actualDecompressedData);
 long actualDecompressedDataChecksum = adler32.value();
 Assert.assertEquals(adler32Checksum, actualDecompressedDataChecksum);
 }
}