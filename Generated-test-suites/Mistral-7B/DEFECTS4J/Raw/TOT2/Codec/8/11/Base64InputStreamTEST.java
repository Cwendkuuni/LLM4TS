import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

 private InputStream input;
 private Base64InputStream base64InputStream;
 private Base64 base64;

 @Test
 public void testBase64InputStream_DefaultConstruction() {
 input = new MockInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=");
 base64InputStream = new Base64InputStream(input);

 byte[] expected = new byte[]{'A', 'B', 'C'};
 byte[] actual = new byte[expected.length];
 int bytesRead = base64InputStream.read(actual, 0, expected.length);
 assertArrayEquals(expected, actual);
 assertEquals(expected.length, bytesRead);
 }

 @Test
 public void testBase64InputStream_Decoding() throws IOException {
 input = new MockInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=");
 base64 = new Base64();
 base64InputStream = new Base64InputStream(input, false);

 byte[] expected = new byte[]{'A', 'B', 'C'};
 byte[] actual = new byte[expected.length];
 int bytesRead = base64InputStream.read(actual, 0, expected.length);
 assertArrayEquals(expected, actual);
 assertEquals(expected.length, bytesRead);
 }

 @Test
 public void testBase64InputStream_Encoding() throws IOException {
 input = new MockInputStream("ABC".getBytes());
 base64 = new Base64(76, "\r\n".getBytes());
 base64InputStream = new Base64InputStream(input, true, 76, "\r\n".getBytes());

 byte[] expected = new byte[]{'T', 'Q', '==', '\r', '\n'};
 byte[] actual = new byte[expected.length];
 int bytesRead = base64InputStream.read(actual, 0, expected.length);
 assertArrayEquals(expected, actual);
 assertEquals(expected.length, bytesRead);
 }

 @Test(expected = IOException.class)
 public void testBase64InputStream_InvalidInput() throws IOException {
 input = new MockInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes(Charset.forName("UTF-16LE")));
 base64InputStream = new Base64InputStream(input);

 byte[] actual = new byte[1];
 int bytesRead = base64InputStream.read(actual, 0, 1);
 fail("Expected IOException was not thrown");
 }

 @Test(expected = IOException.class)
 public void testBase64InputStream_NoDataAvailable() throws IOException {
 input = new MockInputStream("");
 base64InputStream = new Base64InputStream(input, false);

 byte[] actual = new byte[1];
 int bytesRead = base64InputStream.read(actual, 0, 1);
 fail("Expected IOException was not thrown");
 }

 @Test
 public void testBase64InputStream_MarkNotSupported() {
 assertFalse(base64InputStream.markSupported());
 }
}