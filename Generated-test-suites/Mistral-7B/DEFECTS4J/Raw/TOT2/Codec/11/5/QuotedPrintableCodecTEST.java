import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() throws EncoderException {
 byte input = 65; // ASCII 'A'
 byte expectedOutput[] = {65};
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeByteUnsafe() throws EncoderException {
 byte input = 127; // ASCII DELETE
 byte expectedOutput[] = {ESCAPE_CHAR, '7F'};
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeByteWhitespace() throws EncoderException {
 byte input = 32; // ASCII SPACE
 byte expectedOutput[] = {ESCAPE_CHAR, '20'};
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeBytes() throws EncoderException {
 byte[] input = {65, 66, 67, 68}; // ASCII 'ABCD'
 byte[] expectedOutput = {65, 66, 67, 68}; // No encoding needed for printable characters
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeBytesUnsafe() throws EncoderException {
 byte[] input = {127, 127}; // ASCII DELETE DELETE
 byte[] expectedOutput = {ESCAPE_CHAR, '7F', ESCAPE_CHAR, '7F'};
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeBytesWhitespace() throws EncoderException {
 byte[] input = {32, 32, 32}; // ASCII SPACE SPACE SPACE
 byte[] expectedOutput = {ESCAPE_CHAR, '20', ESCAPE_CHAR, '20', ESCAPE_CHAR, '20'};
 byte output[] = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeByteOutOfBounds() throws EncoderException {
 byte input = -128; // out of the ASCII range
 byte output[] = codec.encode(input);
 }

 @Test
 public void testDecodeByte() throws DecoderException {
 byte input[] = {ESCAPE_CHAR, '41', '53'}; // '=' 'A' 'S'
 byte expectedOutput = 65; // ASCII 'A'
 byte output = codec.decode(input, 0, input.length);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeByteWhitespace() throws DecoderException {
 byte input[] = {ESCAPE_CHAR, '20'}; // '=' ' '
 byte expectedOutput = 32; // ASCII ' '
 byte output = codec.decode(input, 0, input.length);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeBytes() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '61', '63', ESCAPE_CHAR, '61', ESCAPE_CHAR, '63'}; // '=' 'a' 's' '=' 'a' 's'
 byte[] expectedOutput = {'a', 's', 'a', 's'};
 byte[] output = codec.decode(input, 0, input.length);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeBytesWithSoftLineBreak() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '62', ESCAPE_CHAR, '0D', ESCAPE_CHAR, '0A'}; // '=' '\r' '=' '\n'
 byte[] expectedOutput = {"\r\n"};
 byte[] output = codec.decode(input, 0, input.length);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeBytesWithMixed() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '63', '61', '63', '61', '63', ESCAPE_CHAR, '78', '0D', ESCAPE_CHAR, '0A'}; // '=' 'c' 'a' 'c' 'a' 'c' '=' 'N' '\r' '=' '\n'
 byte[] expectedOutput = {"caCaC", "N\r\n"};
 byte[] output = codec.decode(input, 0, input.length);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeByteOutOfBounds() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '61'}; // '=' 'a'
 int length = 3;
 byte output[] = codec.decode(input, 2, length);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeBytesNoStart() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '63'}; // '=' 'c'
 int offset = 1;
 byte output[] = codec.decode(input, offset, input.length);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeBytesNoEnd() throws DecoderException {
 byte[] input = {ESCAPE_CHAR, '61'}; // '=' 'a'
 int length = 1;
 byte output[] = codec.decode(input, 0, length);
 }
}