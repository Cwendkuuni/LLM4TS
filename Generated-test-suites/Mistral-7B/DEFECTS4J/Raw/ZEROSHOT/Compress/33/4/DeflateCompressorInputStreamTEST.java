import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Deflater;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream compressorInputStream;
 private ByteArrayOutputStream outputStream;
 private DeflateParameters parameters;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x5B, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF});
 parameters = new DeflateParameters();
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws IOException {
 compressorInputStream.close();
 inputStream.close();
 outputStream.close();
 }

 @Test
 public void testDecompressorInputStreamWithZlibHeader() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 byte[] buffer = new byte[10];
 int n = compressorInputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(n, buffer.length);
 InputStream zlibInputStream = new InflaterInputStream(new ByteArrayInputStream(inputStream.readAllBytes()), new Inflater(true));
 n = zlibInputStream.read(buffer, 0, buffer.length);
 Assert.assertEquals(n, buffer.length);
 Assert.assertArrayEquals(buffer, compressorInputStream.readAllBytes());
 }

 @Test(expected = IOException.class)
 public void testDecompressorInputStreamWithoutZlibHeader() throws IOException {
 parameters.setWithZlibHeader(false);
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 }

 @Test
 public void testReadWithDecompressorInputStream() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 byte[] buffer = new byte[1];
 int n = 0;
 do {
 n = compressorInputStream.read(buffer, 0, buffer.length);
 outputStream.write(buffer, 0, n);
 } while (n != -1);
 InputStream zlibInputStream = new InflaterInputStream(new ByteArrayInputStream(inputStream.readAllBytes()), new Inflater(parameters.isWithZlibHeader()));
 byte[] expectedOutput = new byte[outputStream.size()];
 zlibInputStream.read(expectedOutput);
 Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
 }

 @Test
 public void testReadAllBytesWithDecompressorInputStream() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 byte[] expectedOutput = new byte[inputStream.available()];
 compressorInputStream.readAllBytes();
 InputStream zlibInputStream = new InflaterInputStream(new ByteArrayInputStream(inputStream.readAllBytes()), new Inflater(parameters.isWithZlibHeader()));
 zlibInputStream.read(expectedOutput);
 Assert.assertArrayEquals(expectedOutput, compressorInputStream.readAllBytes());
 }

 @Test
 public void testAvailableWithDecompressorInputStream() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 int available = compressorInputStream.available();
 Assert.assertEquals(available, inputStream.available());
 }

 @Test
 public void testSkipWithDecompressorInputStream() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 long n = 10L;
 long skipped = compressorInputStream.skip(n);
 Assert.assertEquals(skipped, n);
 }

 @Test
 public void testCloseWithDecompressorInputStream() throws IOException {
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 compressorInputStream.close();
 Assert.assertFalse(compressorInputStream.isOpen());
 }

 @Test
 public void testCompressAndDecompressWithDeflateCompressorInputStream() throws IOException {
 ByteArrayOutputStream compressOutputStream = new ByteArrayOutputStream();
 Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
 deflater.setInput(outputStream.toByteArray());
 deflater.finish();
 byte[] compressedData = compressOutputStream.toByteArray();
 inputStream = new ByteArrayInputStream(compressedData);
 compressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
 byte[] buffer = new byte[10];
 int n = 0;
 do {
 n = compressorInputStream.read(buffer, 0, buffer.length);
 outputStream.write(buffer, 0, n);
 } while (n != -1);
 Assert.assertArrayEquals(outputStream.toByteArray(), compressOutputStream.toByteArray());
 }
}