import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec codec;

 @Test
 public void testEncodeDecodeByteArray() throws EncoderException, DecoderException {
 codec = new QuotedPrintableCodec();
 byte[] input = {0x1A, 0x2B, 0x3C};
 byte[] expectedOutput = {0x3D, '1', 'A', '=', 0x3D, '2', 'B', '=', 0x3D, '3', 'C'};
 byte[] output = codec.encode(input);
 assertArrayEquals(expectedOutput, output);
 byte[] decodedOutput = codec.decode(output);
 assertArrayEquals(input, decodedOutput);
 }

 @Test
 public void testEncodeDecodeString() throws EncoderException, DecoderException, UnsupportedEncodingException {
 codec = new QuotedPrintableCodec();
 String input = "abc";
 String charset = "UTF-8";
 String expectedOutput = "=73=61=62=63=3D=";
 String output = codec.encode(input, charset);
 assertEquals(expectedOutput, output);
 String decodedOutput = codec.decode(output, charset);
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testEncodeDecodeObject() throws EncoderException, DecoderException {
 codec = new QuotedPrintableCodec();
 byte[] input = {0x1A, 0x2B, 0x3C};
 Object expectedOutput = new byte[] {0x3D, '1', 'A', '=', 0x3D, '2', 'B', '=', 0x3D, '3', 'C'};
 Object output = codec.encode(input);
 assertEquals(expectedOutput, output);
 byte[] decodedOutput = (byte[]) codec.decode(output);
 assertArrayEquals(input, decodedOutput);
 }

 @Test
 public void testEncodeDecodeStringWithDefaultCharset() throws EncoderException, DecoderException {
 codec = new QuotedPrintableCodec();
 String input = "abc";
 String expectedOutput = "=73=61=62=63=3D=";
 String output = codec.encode(input);
 assertEquals(expectedOutput, output);
 String decodedOutput = codec.decode(output);
 assertEquals(input, decodedOutput);
 }

 @Test
 public void testIsPrintableCharacter() {
 codec = new QuotedPrintableCodec();
 assertTrue(codec.isPrintableCharacter((byte) 'A'));
 assertTrue(codec.isPrintableCharacter((byte) 'a'));
 assertTrue(codec.isPrintableCharacter((byte) '0'));
 assertTrue(codec.isPrintableCharacter((byte) '!'));
 assertTrue(codec.isPrintableCharacter((byte) '*'));
 assertTrue(codec.isPrintableCharacter((byte) '\t'));
 assertTrue(codec.isPrintableCharacter((byte) ' '));
 assertFalse(codec.isPrintableCharacter((byte) '\r'));
 assertFalse(codec.isPrintableCharacter((byte) '\n'));
 assertFalse(codec.isPrintableCharacter((byte) '\f'));
 }

 @Test
 public void testEncodeCharacter() {
 codec = new QuotedPrintableCodec();
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 codec.encodeQuotedPrintable((byte) '!', out);
 assertEquals(2, out.size());
 char hex1 = Character.toUpperCase(Character.forDigit((0x21 >> 4) & 0xF, 16));
 char hex2 = Character.toUpperCase(Character.forDigit(0x21 & 0xF, 16));
 assertEquals(hex1, out.toString().charAt(0));
 assertEquals(hex2, out.toString().charAt(1));
 }

 @Test
 public void testDecodeCharacter() {
 codec = new QuotedPrintableCodec();
 byte[] input = {0x3D, '1', 'A', '=', 0x3D, '2', 'B', '='};
 int expectedOutput = 0x21;
 int output = codec.decodeQuotedPrintable(input, 0, input.length);
 assertEquals(expectedOutput, output);
 }

}