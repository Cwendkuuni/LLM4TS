import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

 private BaseNCodec baseNCodec = new BaseNCodec();
 private BaseNCodecInputStream inputStream;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputTest;

 @Test
 public void testBaseNCodecInputStream_Constructor() {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);
 Assert.assertNotNull(inputStream);
 }

 @Test
 public void testBaseNCodecInputStream_Constructor_EncodeMode() {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, true);
 Assert.assertNotNull(inputStream);
 }

 @Test(expected = NullPointerException.class)
 public void testBaseNCodecInputStream_Constructor_NullBaseNCodec() {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
 }

 @Test(expected = NullPointerException.class)
 public void testBaseNCodecInputStream_Constructor_NullInputStream() {
 inputStream = new BaseNCodecInputStream(null, baseNCodec, false);
 }

 @Test
 public void testRead_ReadOneByte() throws IOException {
 inputTest = new ByteArrayInputStream(new byte[]{0x01});
 inputStream = new BaseNCodecInputStream(inputTest, baseNCodec, false);
 Assert.assertEquals(0x01, inputStream.read());
 }

 @Test
 public void testRead_ReadEOF() throws IOException {
 inputTest = new ByteArrayInputStream(new byte[0]);
 inputStream = new BaseNCodecInputStream(inputTest, baseNCodec, false);
 Assert.assertEquals(-1, inputStream.read());
 }

 @Test
 public void testRead_ReadMultipleBytes() throws IOException {
 byte[] testData = new byte[]{0x01, 0x02, 0x03};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 byte[] readData = new byte[testData.length];
 Assert.assertEquals(testData.length, inputStream.read(readData));
 Assert.assertArrayEquals(testData, readData);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRead_ReadNegativeLength() throws IOException {
 byte[] testData = new byte[]{0x01};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 inputStream.read(new byte[testData.length + 1]);
 }

 @Test(expected = NullPointerException.class)
 public void testRead_NullByteArray() throws IOException {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x01}), baseNCodec, false);
 inputStream.read(null);
 }

 @Test
 public void testRead_ReadNullByteArrayLength() throws IOException {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x01}), baseNCodec, false);
 byte[] readData = null;
 Assert.assertEquals(0, inputStream.read(readData));
 }

 @Test
 public void testMarkSupported() throws IOException {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x01}), baseNCodec, false);
 Assert.assertFalse(inputStream.markSupported());
 }

 @Test
 public void testSkippable() throws IOException {
 byte[] testData = new byte[512];
 byte[] skipData = new byte[256];
 for (int i = 0; i < testData.length; i++) {
 testData[i] = (byte) (i % 256);
 }
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 int skipped = 0;
 while (inputStream.available() > 0 && skipped < testData.length) {
 skipped += inputStream.skip(512);
 }
 Assert.assertEquals(testData.length, inputStream.available());
 Assert.assertEquals(testData.length, skipped);

 byte[] expectedSkipData = new byte[testData.length];
 System.arraycopy(testData, 0, expectedSkipData, 0, testData.length);
 BaseNCodecOutputStream outputStream = new BaseNCodecOutputStream(new ByteArrayOutputStream());
 outputStream.write(testData);
 inputTest = new ByteArrayInputStream(outputStream.toByteArray());
 byte[] readData = new byte[512];
 int read = 0;
 while (read < testData.length && inputTest.available() > 0) {
 read += inputTest.read(readData, 0, readData.length);
 System.arraycopy(readData, 0, skipData, read - readData.length, read);
 }
 Assert.assertArrayEquals(skipData, testData);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSkippable_NegativeChunkSize() throws IOException {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x01}), baseNCodec, false);
 inputStream.skip(-1);
 }

 @Test
 public void testAvailable() throws IOException {
 byte[] testData = new byte[]{0x01, 0x02, 0x03};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 Assert.assertEquals(testData.length, inputStream.available());
 }

 @Test
 public void testAvailable_EOF() throws IOException {
 inputTest = new ByteArrayInputStream(new byte[0]);
 inputStream = new BaseNCodecInputStream(inputTest, baseNCodec, false);
 Assert.assertEquals(0, inputStream.available());
 }

 @Test
 public void testAvailable_DataAdded() throws IOException {
 byte[] testData = new byte[]{0x01, 0x02, 0x03};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 byte[] newData = new byte[]{0x04, 0x05};
 byte[] readData = new byte[testData.length + newData.length];
 inputStream.read(readData);
 Assert.assertEquals(testData.length + newData.length, inputStream.available());
 }

 @Test
 public void testAvailable_DataRead() throws IOException {
 byte[] testData = new byte[]{0x01, 0x02, 0x03};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(testData), baseNCodec, false);
 byte[] readData = new byte[testData.length];
 inputStream.read(readData);
 Assert.assertEquals(0, inputStream.available());
 }

 @Test(expected = IllegalStateException.class)
 public void testClose() throws IOException {
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x01}), baseNCodec, false);
 inputStream.close();
 inputStream.read();
 }

 @Test
 public void testDecode_EncodeMode() throws IOException {
 byte[] testData = new byte[]{0x01};
 byte[] decoded = new byte[3];
 BaseNCodecOutputStream outputStream = new BaseNCodecOutputStream(new ByteArrayOutputStream());
 outputStream.write(testData);
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, true);
 inputStream.read(decoded);
 Assert.assertEquals(Base64.decodeBase64(new String(new String(new sun.misc.BASE64Encoder().encode(testData)).getBytes())), decoded);
 }

 @Test
 public void testDecode_DecodeMode() throws IOException {
 byte[] testData = new byte[]{0x01};
 byte[] decoded = new byte[3];
 BaseNCodecOutputStream outputStream = new BaseNCodecOutputStream(new ByteArrayOutputStream());
 outputStream.write(testData);
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);
 inputStream.read(decoded);
 Assert.assertEquals(testData, decoded);
 }

 @Test(expected = IOException.class)
 public void testDecode_InvalidData() throws IOException {
 byte[] testData = new byte[]{0x01};
 inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{0x02}), baseNCodec, false);
 byte[] decoded = new byte[3];
 inputStream.read(decoded);
 }
}