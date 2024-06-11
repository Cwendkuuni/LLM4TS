import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte input = 0x2A; // '*'
 byte[] output = codec.encode(new byte[]{input});
 assertEquals(2, output.length);
 assertEquals(Hex.encodeHex(new byte[]{ESCAPE_CHAR, '2', 'A'}), new String(output));
 }

 @Test
 public void testEncodeBytes() {
 byte[] input = new byte[]{'A', 'B', 'C'};
 byte[] output = codec.encode(input);
 assertArrayEquals(new byte[]{'A', 'B', 'C'}, output);

 byte[] encodedOutput = codec.encode(input);
 assertArrayEquals(new byte[]{(byte) 0x2E, 0x61, 0x6D, (byte) 0x65, 0x64, 0x76, (byte) 0x65, 'C'}, encodedOutput);
 }

 @Test
 public void testDecodeByte() {
 byte input = ESCAPE_CHAR;
 byte[] encodedInput = new byte[]{ESCAPE_CHAR, '2', 'A'};
 byte decodedOutput = codec.decode(encodedInput)[0];
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testDecodeBytes() {
 byte[] encodedInput = new byte[]{(byte) 0x2E, 0x61, 0x6D, (byte) 0x65, 0x64, 0x76, (byte) 0x65, 'C'};
 byte[] decodedOutput = codec.decode(encodedInput);
 assertArrayEquals(new byte[]{'A', 'B', 'C'}, decodedOutput);
 }

 @Test
 public void testEncodeAndDecodeStringUsAscii() {
 String input = "ABC";
 String encodedInput = codec.encode(input.getBytes()).toString();
 String decodedOutput = new String(codec.decode(Hex.decodeHex(encodedInput.toCharArray()))).trim();
 assertEquals(input, decodedOutput);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidHex() {
 String encodedInput = "4D%65%64"; // 'M%de'
 byte[] decodedOutput = codec.decode(Hex.decodeHex(encodedInput.toCharArray()));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeMissingHexDigit() {
 String encodedInput = "4D%"; // 'M%'
 byte[] decodedOutput = codec.decode(Hex.decodeHex(encodedInput.toCharArray()));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeTrailingCR() {
 String encodedInput = "4D%65%64\r"; // 'M%de\r'
 byte[] decodedOutput = codec.decode(Hex.decodeHex(encodedInput.toCharArray()));
 }
}