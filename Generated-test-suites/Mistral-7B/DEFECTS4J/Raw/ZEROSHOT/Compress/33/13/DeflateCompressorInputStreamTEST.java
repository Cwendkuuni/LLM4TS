import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream deflateCompressorInputStream;
 private ByteArrayOutputStream outputStream;
 private byte[] inputData;
 private byte[] expectedOutput;

 @Before
 public void setUp() throws IOException {
 inputData = new byte[]{(byte) 0x78, (byte) 0x4e, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x0, (byte) 0x14, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws IOException {
 if (deflateCompressorInputStream != null) {
 deflateCompressorInputStream.close();
 }
 }

 @Test
 public void testDeflateCompressorInputStreamWithZlibHeader() throws IOException {
 // Given
 inputStream = new ByteArrayInputStream(inputData);
 DeflateParameters deflateParameters = new DeflateParameters();
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);

 // When
 int read = deflateCompressorInputStream.read();
 byte[] outputData = new byte[inputData.length + 2];
 int bytesRead = deflateCompressorInputStream.read(outputData, 0, outputData.length);

 // Then
 Assert.assertEquals(inputData[0], outputData[0]);
 Assert.assertEquals(inputData[1], outputData[1]);
 // Check zlib header
 Assert.assertEquals((byte) 0x78, outputData[2]);
 Assert.assertEquals((byte) 0x4e, outputData[3]);
 Assert.assertEquals((byte) 0x65, outputData[4]);
 Assert.assertEquals((byte) 0x6c, outputData[5]);
 Assert.assertEquals((byte) 0x6c, outputData[6]);
 Assert.assertEquals((byte) 0x6f, outputData[7]);
 // The rest of the data should be the same
 for (int i = 8; i < inputData.length + 2; i++) {
 Assert.assertEquals(inputData[i - 8], outputData[i]);
 }
 // Cleanup
 deflateCompressorInputStream.close();
 }

 @Test
 public void testDeflateCompressorInputStreamWithoutZlibHeader() throws IOException {
 // Given
 byte[] dataWithoutZlibHeader = new byte[]{(byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 inputStream = new ByteArrayInputStream(dataWithoutZlibHeader);
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(true));

 // When
 expectedOutput = new byte[]{(byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8};
 byte[] outputData = new byte[expectedOutput.length];
 int bytesRead = deflateCompressorInputStream.read(outputData, 0, outputData.length);

 // Then
 Assert.assertArrayEquals(expectedOutput, outputData);
 // Cleanup
 deflateCompressorInputStream.close();
 }

 @Test(expected = IOException.class)
 public void testDeflateCompressorInputStreamWithInvalidZlibHeader() throws IOException {
 // Given
 byte[] dataWithInvalidZlibHeader = new byte[]{(byte) 0x78, (byte) 0x4e, (byte) 0x65, (byte) 0x6c, (byte) 0x6c, (byte) 0x6f, (byte) 0x0, (byte) 0x14, (byte) 0x0, (byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6, (byte) 0x7, (byte) 0x8, (byte) 0x9};
 inputStream = new ByteArrayInputStream(dataWithInvalidZlibHeader);
 deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

 // Then
 // IOException should be thrown
 }
}