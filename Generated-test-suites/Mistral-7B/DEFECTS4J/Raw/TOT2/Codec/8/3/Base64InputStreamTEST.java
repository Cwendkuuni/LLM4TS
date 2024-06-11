import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private static final String TEST_DATA = "This is a test for Base64InputStream.";

 @Test
 public void testBase64InputStreamDefaultConstructor() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[TEST_DATA.length()];
 int read = inputStream.read(buffer, 0, TEST_DATA.length());
 assertEquals(TEST_DATA.length(), read);
 String decodedData = new String(buffer, 0, read);
 assertEquals(TEST_DATA, decodedData);
 }

 @Test
 public void testBase64InputStreamEncodeConstructor() throws Exception {
 byte[] encodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes();
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(encodedData), true);
 byte[] buffer = new byte[encodedData.length];
 int read = inputStream.read(buffer, 0, encodedData.length);
 assertEquals(encodedData.length, read);
 String decodedData = new String(buffer, 0, read);
 assertEquals(TEST_DATA, decodedData);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamNegativeLineLength() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()), true, -1, null);
 assertNull(inputStream);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamNullLineSeparator() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()), true, 76, null);
 assertNull(inputStream);
 }

 @Test
 public void testBase64InputStreamReadByte() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[1];
 int read = -1;
 while (read != -1) {
 read = inputStream.read(buffer, 0, 1);
 assertTrue(read != -1);
 }
 }

 @Test
 public void testBase64InputStreamReadByteArray() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[TEST_DATA.length()];
 int read = inputStream.read(buffer, 0, TEST_DATA.length());
 assertEquals(TEST_DATA.length(), read);
 String decodedData = new String(buffer, 0, read);
 assertEquals(TEST_DATA, decodedData);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBase64InputStreamReadByteArrayNegativeOffset() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[TEST_DATA.length() + 1];
 int read = inputStream.read(buffer, -1, TEST_DATA.length());
 fail();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBase64InputStreamReadByteArrayNegativeLength() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[TEST_DATA.length() + 1];
 int read = inputStream.read(buffer, 0, -1);
 fail();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBase64InputStreamReadByteArrayNegativeBufferSize() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[-1];
 int read = inputStream.read(buffer, 0, TEST_DATA.length());
 fail();
 }

 @Test(expected = IOException.class)
 public void testBase64InputStreamReadByteArrayZeroLength() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 byte[] buffer = new byte[0];
 int read = inputStream.read(buffer, 0, TEST_DATA.length());
 fail();
 }

 @Test
 public void testBase64InputStreamMarkSupported() throws Exception {
 Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
 assertFalse(inputStream.markSupported());
 }
}