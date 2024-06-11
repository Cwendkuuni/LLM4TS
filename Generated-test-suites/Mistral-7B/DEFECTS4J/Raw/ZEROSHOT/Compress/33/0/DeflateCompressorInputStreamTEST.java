import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {

 private InputStream inputStream;
 private DeflateCompressorInputStream decompressor;
 private byte[] originalData = new byte[]{(byte) 0x78, 0x9C, 0x47, 0x1A, 0xBA, 0xDC, 0xEA, 0x7B, 0xF2, 0x4C, 0x69, 0x4E, 0x5A, 0x50, 0x52, 0x58, 0x51, 0x53, 0x59, 0x54, 0x55, 0x56, 0x57, 0x59};
 private byte[] compressedData;
 private byte[] decompressedData;

 @Before
 public void setUp() throws IOException {
 inputStream = new ByteArrayInputStream(originalData);
 compressData();
 decompressor = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedData), new DeflateParameters());
 }

 @After
 public void tearDown() throws IOException {
 if (decompressor != null) {
 decompressor.close();
 }
 if (inputStream != null) {
 inputStream.close();
 }
 }

 @Test
 public void testRead() throws IOException {
 byte[] result = new byte[1];
 int read;
 do {
 read = decompressor.read(result, 0, result.length);
 } while (read > 0);
 assertEquals(-1, decompressor.read());
 assertArrayEquals(new byte[]{(byte) 0x78}, result);
 }

 @Test
 public void testReadByte() throws IOException {
 byte[] result = new byte[1];
 int read;
 do {
 read = decompressor.read(result, 0, result.length);
 } while (read > 0);
 assertEquals(-1, decompressor.read());
 assertArrayEquals(new byte[]{(byte) 0x78}, result);
 }

 @Test
 public void testReadFully() throws IOException {
 byte[] result = new byte[originalData.length];
 decompressor.read(result);
 assertArrayEquals(originalData, result);
 }

 @Test
 public void testSkip() throws IOException {
 long skipped = decompressor.skip(originalData.length);
 assertEquals(originalData.length, skipped);
 }

 @Test
 public void testAvailable() throws IOException {
 assertEquals(originalData.length, decompressor.available());
 }

 @Test
 public void testClose() throws IOException {
 decompressor.close();
 assertEquals(0, decompressor.available());
 }

 private void compressData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DeflateCompressorOutputStream compressor = new DeflateCompressorOutputStream(outputStream);
 compressor.write(originalData);
 compressor.finish();
 compressor.close();
 compressedData = outputStream.toByteArray();
 }

}