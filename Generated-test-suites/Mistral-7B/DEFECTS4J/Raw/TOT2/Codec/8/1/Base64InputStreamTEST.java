import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

 private static final byte[] TEST_DATA = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

 /**
 * Test Base64InputStream constructor with default decoding
 */
 @Test
 public void testDefaultDecoder() throws Exception {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(Base64.decodeBase64(TEST_DATA)));
 int b;
 for (int i = 0; i < TEST_DATA.length; i++) {
 b = is.read();
 assertEquals(TEST_DATA[i], b);
 }
 assertNull(is.in); // InputStream should be closed
 }

 /**
 * Test Base64InputStream constructor with encoding
 */
 @Test
 public void testEncoder() throws Exception {
 byte[] encoded = new byte[Base64.encodeBase64(TEST_DATA).length];
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
 int b;
 for (int i = 0; i < encoded.length; i++) {
 b = is.read();
 encoded[i] = (byte) b;
 }
 assertArrayEquals(Base64.decodeBase64(encoded), TEST_DATA);
 }

 /**
 * Test Base64InputStream constructor with custom lineLength and lineSeparator
 */
 @Test
 public void testCustomDecoder() throws Exception {
 byte[] lineSeparator = {(byte) 0x0D, (byte) 0x0A};
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(Base64.decodeBase64(TEST_DATA)), false, 76, lineSeparator);
 int b;
 for (int i = 0; i < TEST_DATA.length; i += 76) {
 int len = Math.min(TEST_DATA.length - i, 76);
 byte[] line = new byte[len];
 for (int j = 0; j < len; j++) {
 b = is.read();
 line[j] = (byte) b;
 }
 assertEquals(Base64.decodeBase64(line), TEST_DATA, i, len);
 }
 assertNull(is.in); // InputStream should be closed
 }

 /**
 * Test null InputStream
 */
 @Test(expected = NullPointerException.class)
 public void testNullInputStream() {
 new Base64InputStream(null, false);
 }

 /**
 * Test negative InputStream length
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInputStreamLength() throws IOException {
 ByteArrayInputStream in = new ByteArrayInputStream(TEST_DATA);
 in.mark(0);
 in.reset();
 Base64InputStream base64InputStream = new Base64InputStream(in, false);
 int read = base64InputStream.read();
 in.mark(0);
 in.reset();
 assertThrows(IOException.class, () -> base64InputStream.read(new byte[0], 0, 0));
 }

 /**
 * Test read beyond InputStream length
 */
 @Test
 public void testReadBeyondInputStreamLength() throws IOException {
 ByteArrayInputStream in = new ByteArrayInputStream(TEST_DATA);
 Base64InputStream base64InputStream = new Base64InputStream(in, false);
 int read = base64InputStream.read();
 assertEquals(TEST_DATA[0], read);
 int[] results = new int[TEST_DATA.length + 1];
 int totalRead = 0;
 while (read >= 0) {
 results[totalRead++] = read;
 read = base64InputStream.read();
 }
 assertArrayEquals(new int[]{-1, -1, -1}, new int[]{results[TEST_DATA.length], results[TEST_DATA.length + 1], results[TEST_DATA.length + 2]});
 }

 /**
 * Test markSupported method
 */
 @Test
 public void testMarkSupported() {
 Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(Base64.decodeBase64(TEST_DATA)));
 assertFalse(is.markSupported());
 }
}