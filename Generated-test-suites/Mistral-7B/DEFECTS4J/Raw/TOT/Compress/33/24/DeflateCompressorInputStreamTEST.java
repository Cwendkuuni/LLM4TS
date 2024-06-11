import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class DeflateCompressorInputStreamTest {

 @Test
 public void testDeflateCompressorInputStreamWithValidData() throws Exception {
 byte[] testData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x2e, (byte) 0x02, (byte) 0x4c, (byte) 0x18, (byte) 0x0a, (byte) 0x00, (byte) 0x00};

 ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
 DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
 byte[] testDataRead = new byte[testData.length];
 int length = deflateStream.read(testDataRead);
 assertArrayEquals("Data read from stream is not equal to original data", testData, testDataRead, 0, testData.length);
 assertEquals("Wrong number of bytes read from stream", testData.length, length);
 deflateStream.close();
 }

 @Test
 public void testDeflateCompressorInputStreamWithZlibHeader() throws Exception {
 byte[] testData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x2e, (byte) 0x02, (byte) 0x4c, (byte) 0x18, (byte) 0x0a, (byte) 0x00, (byte) 0x00};

 ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
 DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters(true));
 byte[] testDataRead = new byte[testData.length - 8]; // Exclude zlib header
 int length = deflateStream.read(testDataRead);
 assertArrayEquals("Data read from stream is not equal to original data", Arrays.copyOfRange(testData, 8, testData.length), testDataRead, 0, testData.length - 8);
 assertEquals("Wrong number of bytes read from stream", testData.length - 8, length);
 deflateStream.close();
 }

 @Test(expected = IOException.class)
 public void testDeflateCompressorInputStreamWithInvalidData() throws Exception {
 byte[] testData = new byte[]{(byte) 0x78, (byte) 0x9c, (byte) 0xba, (byte) 0xdc, (byte) 0x2e, (byte) 0x02, (byte) 0x4c, (byte) 0x18};

 ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
 DeflateCompressorInputStream deflateStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
 int length = deflateStream.read();
 deflateStream.close();
 }
}