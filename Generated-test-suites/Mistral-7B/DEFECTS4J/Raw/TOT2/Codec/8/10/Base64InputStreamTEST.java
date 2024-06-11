import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = new byte[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

 @Test
 public void testBase64InputStream_DefaultConstruction() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(TEST_DATA));
 byte[] result = new byte[12];
 int read = inputStream.read(result, 0, result.length);
 assertEquals(read, result.length);
 assertArrayEquals(Base64.decodeBase64(new String(TEST_DATA).getBytes()), result);
 }

 @Test
 public void testBase64InputStream_EncodingConstruction() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(TEST_DATA), true);
 byte[] result = new byte[16];
 int read = inputStream.read(result, 0, result.length);
 assertEquals(read, result.length);
 String encoded = new String(Base64.encodeBase64(TEST_DATA));
 assertArrayEquals(encoded.getBytes(), result);
 }

 @Test
 public void testBase64InputStream_CustomLineLengthAndLineSeparator() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(TEST_DATA), true, 50, new byte[]{'A', 'B'});
 byte[] result = new byte[20];
 int read = inputStream.read(result, 0, result.length);
 assertEquals(read, result.length);
 String encoded = new String(Base64.encodeBase64(TEST_DATA, 50, (byte) 0xf0));
 assertArrayEquals(encoded.substring(0, encoded.length() - 2).getBytes(), result);
 assertEquals(new String(new byte[]{'A', 'B'}), new String(result, result.length - 2, 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_NegativeLineLength() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(TEST_DATA), true, -1, new byte[]{'A', 'B'});
 }

 @Test(expected = NullPointerException.class)
 public void testBase64InputStream_NullInput() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(null, false);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStream_EmptyInput() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(new byte[]{}), false);
 int read = inputStream.read();
 assertEquals(-1, read);
 }

 @Test
 public void testBase64InputStream_ClosedInput() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new MockInputStream(TEST_DATA), false);
 inputStream.close();
 int read = inputStream.read();
 assertEquals(-1, read);
 }
}

class MockInputStream extends InputStream {
 private byte[] data;
 private int index;

 public MockInputStream(byte[] data) {
 this.data = data;
 }

 @Override
 public int read() throws IOException {
 if (index < data.length) {
 return data[index++];
 }
 return -1;
 }

 @Override
 public int read(byte[] b, int off, int len) throws IOException {
 if (len <= 0) {
 return 0;
 }
 if (index + len > data.length) {
 len -= index;
 System.arraycopy(data, index, b, off, data.length - index);
 index = data.length;
 return len;
 }
 System.arraycopy(data, index, b, off, len);
 index += len;
 return len;
 }
}