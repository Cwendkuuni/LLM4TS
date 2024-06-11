import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte byteToEncode = 0x65;
 byte[] encoded = codec.encode(new byte[]{byteToEncode});
 assertEquals(1, encoded.length);
 assertEquals((byte) '65', encoded[0]);

 byte escapedByte = '=' + (char) (byteToEncode >> 4);
 assertEquals((byte) escapedByte, encoded[0]);
 }

 @Test
 public void testEncodeByteArray() {
 byte[] bytesToEncode = {0x65, 0x66, 0x67};
 byte[] encoded = codec.encode(bytesToEncode);
 assertEquals(3 + 6 * 2, encoded.length); // 3 bytes -> 6 pairs of bytes

 byte[] expected = new byte[3];
 for (int i = 0; i < 3; i++) {
 byte b = bytesToEncode[i];
 expected[i] = (byte) (b >> 4);
 }
 Hex.encode(expected, 0, 3, encoded, 0);
 }

 @Test
 public void testDecodeByteArray() {
 byte[] encoded = new byte[]{(byte) '68', (byte) '65', (byte) '78'};
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(new byte[]{0x65, 0x68, 0x78}, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidHexSequence() {
 byte[] encoded = new byte[]{(byte) '68', (byte) '65', (byte) '7C', (byte) '6F', (byte) '78'};
 codec.decode(encoded);
 }

 @Test
 public void testEncodeString() {
 String stringToEncode = "abc";
 String encoded = codec.encode(stringToEncode);
 assertEquals(encoded.length(), StringUtils.getByteCount(stringToEncode, codec.getDefaultCharset()) * 2 + 3);
 }

 @Test
 public void testDecodeString() {
 String encoded = "6865636B";
 String decoded = codec.decode(encoded.getBytes());
 assertEquals(encoded, decoded);
 }

 @Test
 public void testDecodeStringWithCrlf() {
 String encoded = "6865636B0D0A636F6D65";
 String decoded = codec.decode(encoded.getBytes());
 assertEquals("\r\nCrlf", decoded);
 }

 @Test
 public void testDecodeStringWithWhitespace() {
 String encoded = "6865636B202020636F6D65";
 String decoded = codec.decode(encoded.getBytes());
 assertEquals(" Crlf", decoded);
 }

}