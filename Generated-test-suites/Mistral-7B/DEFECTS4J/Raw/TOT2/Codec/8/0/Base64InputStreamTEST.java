import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private static final String TEST_DATA = "This is a test.";
 private static final byte[] TEST_DATA_BYTES = TEST_DATA.getBytes();

 @Test
 public void testBase64InputStream_DefaultConstructor() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA_BYTES));
 byte[] testDataBytes = new byte[TEST_DATA.length()];
 int length = inputStream.read(testDataBytes);
 assertEquals(TEST_DATA.length(), length);
 assertEquals(TEST_DATA, new String(testDataBytes, 0, length));
 }

 @Test
 public void testBase64InputStream_EncodeConstructor() throws IOException {
 Base64 base64 = new Base64();
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA_BYTES), true, 76, new byte[]{(byte) 13, (byte) 10});
 String encodedData = base64.encodeAsString(TEST_DATA_BYTES);
 byte[] expectedEncodedDataBytes = encodedData.getBytes();
 byte[] testEncodedDataBytes = new byte[expectedEncodedDataBytes.length];
 int length = inputStream.read(testEncodedDataBytes);
 assertArrayEquals(expectedEncodedDataBytes, testEncodedDataBytes);
 }

 @Test
 public void testBase64InputStream_DecodeConstructor() throws IOException {
 Base64 base64 = new Base64();
 String encodedData = base64.encodeAsString(TEST_DATA_BYTES);
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(encodedData.getBytes()), false);
 byte[] testDataBytes = new byte[TEST_DATA.length()];
 int length = inputStream.read(testDataBytes);
 assertEquals(TEST_DATA.length(), length);
 assertEquals(TEST_DATA, new String(testDataBytes, 0, length));
 }

 @Test(expected = IllegalStateException.class)
 public void testBase64InputStream_EncodeConstructor_NoData() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
 inputStream.read(new byte[1]);
 fail("Should have thrown an IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBase64InputStream_DecodeConstructor_InvalidInput() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x01}), false);
 inputStream.read(new byte[1]);
 fail("Should have thrown an IllegalArgumentException");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStream_ReadPastEndOfStream() throws IOException {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA_BYTES), false);
 byte[] testDataBytes = new byte[TEST_DATA.length() + 1];
 int length = inputStream.read(testDataBytes);
 assertEquals(-1, length);
 }
}