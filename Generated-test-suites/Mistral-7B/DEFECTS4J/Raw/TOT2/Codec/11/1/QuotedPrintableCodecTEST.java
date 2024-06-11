import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte b = 0x20; // Space character
 byte[] encoded = codec.encode(new byte[] { b });
 Assert.assertEquals(new byte[] { ESCAPE_CHAR, '2', '0' }, encoded);
 }

 @Test
 public void testEncodeByteArray() {
 byte[] bytes = { 0x21, 0x22, 0x23 }; // !",#
 byte[] encoded = codec.encode(bytes);
 Assert.assertArrayEquals(new byte[] { ESCAPE_CHAR, '2', '1', '!' }, encoded);
 Assert.assertArrayEquals(new byte[] { ESCAPE_CHAR, '2', '2', ',' }, encoded, 1);
 Assert.assertArrayEquals(new byte[] { ESCAPE_CHAR, '2', '3', '#' }, encoded, 2);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
 String encoded = codec.encode(input, CharEncoding.US_ASCII.name());
 String expected = StringUtils.newStringUsAscii(codec.encode(input.getBytes(CharEncoding.US_ASCII.name())));
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testDecodeByteArray() throws DecoderException {
 byte[] encoded = { ESCAPE_CHAR, '2', '1', '!' };
 byte[] decoded = codec.decode(encoded);
 Assert.assertArrayEquals(new byte[] { 0x21 }, decoded);
 }

 @Test
 public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
 String encoded = "=21=22=23";
 String decoded = codec.decode(encoded, CharEncoding.US_ASCII.name());
 String input = "!\"#";
 Assert.assertEquals(input, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidSequence() throws DecoderException, UnsupportedEncodingException {
 String encoded = "=21=";
 String decoded = codec.decode(encoded, CharEncoding.US_ASCII.name());
 }

 @Test(expected = DecoderException.class)
 public void testDecodeMissingCharacter() throws DecoderException, UnsupportedEncodingException {
 String encoded = "=21=";
 String decoded = codec.decode(encoded, CharEncoding.ISO_8859_1.name());
 }

 @Test
 public void testDecodeWhitespace() throws DecoderException, UnsupportedEncodingException {
 String encoded = "=20";
 String decoded = codec.decode(encoded, CharEncoding.US_ASCII.name());
 String input = " ";
 Assert.assertEquals(input, decoded);
 }

 @Test
 public void testEncodeAndDecodeByteArrayRoundtrip() throws UnsupportedEncodingException, DecoderException {
 byte[] input = { 0x21, 0x22, 0x23 };
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 Assert.assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeAndDecodeStringRoundtrip() throws UnsupportedEncodingException, DecoderException {
 String input = "!\"#";
 String encoded = codec.encode(input, CharEncoding.US_ASCII.name());
 String decoded = codec.decode(encoded, CharEncoding.US_ASCII.name());
 Assert.assertEquals(input, decoded);
 }
}