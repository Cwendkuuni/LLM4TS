import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte b = 0x61; // 'a' in ASCII
 byte[] encodedBytes = quotedPrintableCodec.encode(new byte[] { b });
 Assert.assertArrayEquals(new byte[] { ESCAPE_CHAR, '61' }, encodedBytes);
 }

 @Test
 public void testEncodeString() {
 String input = "Quoted Printable Encoding Test";
 String expected = "=65=75=6E=69=6C=6C=65=20=50=6F=73=74=65=64=65=64=20=45=6E=63=6F=64=65=78";
 byte[] inputBytes = null;
 try {
 inputBytes = input.getBytes("UTF-8");
 } catch (UnsupportedEncodingException e) {
 throw new RuntimeException(e);
 }
 byte[] encodedBytes = quotedPrintableCodec.encode(inputBytes);
 String actual = Hex.encodeHexString(encodedBytes);
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidByteSequence() {
 // This test checks that an EncoderException is thrown for invalid sequences of bytes
 byte[] input = new byte[] { 0x00, 0x02, 0x61 }; // Invalid sequence: 0x00 followed by 0x02
 byte[] expected = null;
 try {
 expected = quotedPrintableCodec.encode(input);
 } catch (EncoderException e) {
 // Expected
 }
 Assert.assertNull(expected);
 }

 @Test
 public void testDecodeByte() {
 byte[] encodedBytes = new byte[] { ESCAPE_CHAR, '61', '61' };
 byte decodedByte = quotedPrintableCodec.decode(encodedBytes)[0];
 Assert.assertEquals((byte) 'a', decodedByte);
 }

 @Test
 public void testDecodeString() {
 String encodedString = "=65=75=6E=69=6C=6C=65=20=50=6F=73=74=65=64=65=64=20=45=6E=63=6F=64=65=78";
 byte[] encodedBytes = null;
 try {
 encodedBytes = Hex.decodeHex(encodedString);
 } catch (IllegalArgumentException e) {
 throw new RuntimeException(e);
 }
 String decodedString = new String(quotedPrintableCodec.decode(encodedBytes));
 Assert.assertEquals("Quoted Printable Encoding Test", decodedString);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidEscapedSequence() {
 // This test checks that a DecoderException is thrown for invalid escaped sequences
 byte[] encodedBytes = new byte[] { 0x66, 0x6F, ESCAPE_CHAR, '73' }; // Invalid sequence: 'fo' followed by '=' and 's'
 byte[] decodedBytes = quotedPrintableCodec.decode(encodedBytes);
 Assert.assertArrayEquals(new byte[] {}, decodedBytes);
 }

}