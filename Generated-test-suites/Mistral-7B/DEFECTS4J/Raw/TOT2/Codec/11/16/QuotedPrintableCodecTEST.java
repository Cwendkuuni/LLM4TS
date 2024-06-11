import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte b = 0x2a; // '*'
 byte[] encodedBytes = codec.encode(new byte[] { b });
 String encodedString = new String(encodedBytes);
 String expected = "2a=0d"; // CR is added at the end of the encoded string
 Assert.assertEquals(expected, encodedString);
 }

 @Test
 public void testEncodeByteArray() {
 byte[] input = { 0x41, 0x42, 0x43 }; // 'A', 'B', 'C'
 byte[] encodedBytes = codec.encode(input);
 String encodedString = new String(encodedBytes);
 String expected = "41=41 42=42 43=43=\r";
 Assert.assertEquals(expected, encodedString);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "Hello, World!";
 byte[] encodedBytes = codec.encode(input.getBytes("UTF-8"));
 String encodedString = new String(encodedBytes, "UTF-8");
 String expected = "48-65-6c-6c-6f-2c-20-57-6f-72-6c-64-21=\r"; // hex representation of 'Hello, World!'
 Assert.assertEquals(expected, encodedString);
 }

 @Test
 public void testDecodeByte() {
 byte[] encodedBytes = { 0x2a, 0x3d, 0x0d }; // '=', '*', CR
 byte decodedByte = codec.decode(encodedBytes)[0];
 Assert.assertEquals((byte) 0x2a, decodedByte); // '*'
 }

 @Test
 public void testDecodeByteArray() {
 byte[] encodedBytes = { 0x41, 0x3d, 0x34, 0x31, 0x3d, 0x34, 0x31, 0x3d, 0x34, 0x33, 0x3d, 0x0d, 0x0d }; // '=', 'A', '=41', '=41', '=41', '=', '=3'
 byte[] decodedBytes = codec.decode(encodedBytes);
 String decodedString = new String(decodedBytes);
 String expected = "AAA=";
 Assert.assertEquals(expected, decodedString);
 }

 @Test
 public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = StringUtils.getBytesUsAscii("41-41-41-3d-34-31-3d-34-31-3d-34-33-3d-0d-0d"); // '=', 'A', '=41', '=41', '=41', '=', '='
 String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
 String expected = "AAA=";
 Assert.assertEquals(expected, decodedString);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDecodeStringUnsupportedCharset() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = StringUtils.getBytesUsAscii("41-41-41-3d-34-31-3d-34-31-3d-34-33-3d-0d-0d"); // '=', 'A', '=41', '=41', '=41', '=', '='
 String decodedString = new String(codec.decode(encodedBytes), "UTF-16"); // unsupported charset
 }

 @Test
 public void testDecodeStringInvalidEncoding() {
 byte[] encodedBytes = StringUtils.getBytesUsAscii("41-41-41-3d-34-31-3d-34-31-3d-34-33-3d-0d-0d"); // '=', 'A', '=41', '=41', '=41', '=', '='
 String decodedString = null;
 try {
 decodedString = new String(codec.decode(encodedBytes), "UTF-8");
 } catch (DecoderException e) {
 // expected
 }
 Assert.assertNull(decodedString);
 }

 @Test
 public void testDecodeStringEmpty() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = {};
 String decodedString = new String(codec.decode(encodedBytes), "UTF-8");
 Assert.assertEquals("", decodedString);
 }

 @Test
 public void testDecodeStringNull() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = null;
 String decodedString = null;
 try {
 decodedString = new String(codec.decode(encodedBytes), "UTF-8");
 } catch (DecoderException e) {
 // expected
 }
 Assert.assertNull(decodedString);
 }
}