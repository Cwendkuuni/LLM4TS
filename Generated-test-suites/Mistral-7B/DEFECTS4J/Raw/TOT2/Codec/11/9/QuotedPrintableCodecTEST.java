import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte byteToEncode = 65; // ASCII 'A'
 byte[] encodedBytes = codec.encode(new byte[] { byteToEncode });
 Assert.assertEquals("Encoded byte ['A'] should equal '25 41'", "25 41", StringUtils.newStringUsAscii(encodedBytes));
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "Hello, World!";
 String encoded = codec.encode(input.getBytes("UTF-8"));
 String expected = "25 68 65 6C 6C 6F 2C 20 57 6F 72 6C 64 21";
 Assert.assertEquals("Encoded string '[Hello, World!]' should equal '25 68 65 6C 6C 6F 2C 20 57 6F 72 6C 64 21'", expected, encoded);
 }

 @Test
 public void testDecodeByte() {
 byte[] encodedBytes = new byte[] { '25', '74' }; // '25' is the escape character, '74' is the hex value of 't'
 byte decodedByte = codec.decode(encodedBytes)[0];
 Assert.assertEquals("Decoded byte '25 74' should equal 't'", (byte) 9, decodedByte);
 }

 @Test
 public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
 String encodedString = "25 78 53 53 41 4D 4C 4C 4F 2C 20 42 4F 53 53 53 41 4D 4C 4C 4F 53 53 53";
 byte[] decodedBytes = codec.decode(StringUtils.getBytesUsAscii(encodedString));
 String decodedString = new String(decodedBytes, "UTF-8");
 Assert.assertEquals("Decoded string '25 78 53 53 41 4D 4C 4C 4F 2C 20 42 4F 53 53 53 41 4D 4C 4C 4F 53 53 53' should equal 'xRSADDCCLDOGHSASDCCLDOGHSASDCCL'", decodedString);
 }

 @Test
 public void testEncodeWhitespace() {
 byte[] input = new byte[] { 9, 32 }; // tab and space
 byte[] encoded = codec.encode(input);
 String encodedString = new String(encoded, "UTF-8");
 String expected = "25 09 25 20 25 20";
 Assert.assertEquals("Encoded whitespace ['\\t ', ' '] should equal '25 09 25 20 25 20'", expected, encodedString);
 }

 @Test
 public void testDecodeWhitespace() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = new byte[] { '25', '09', '25', '20', '25', '20' };
 byte[] decodedBytes = codec.decode(encodedBytes);
 String decodedString = new String(decodedBytes, "UTF-8");
 Assert.assertEquals("Decoded whitespace '25 09 25 20 25 20' should equal ['\t ', ' ']", new String(new byte[] { 9, 32 }), decodedString);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidSequence() throws UnsupportedEncodingException, DecoderException {
 byte[] encodedBytes = new byte[] { '25', '54' }; // '54' is not a valid hex sequence
 byte[] decodedBytes = codec.decode(encodedBytes);
 }
}