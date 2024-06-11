import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeBinaryData() {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
 byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05, 0x0D, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35};
 byte[] output = codec.encode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithDefaultCharset() {
 String input = "Hello World!";
 String expected = "=3HELLO=20World=%3D";
 String output = codec.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithSpecifiedCharset() throws UnsupportedEncodingException {
 String input = "Hello World!";
 String charset = "ISO-8859-1";
 String expected = "=3HELLO=20World=%3D";
 String output = codec.encode(input, charset);
 assertEquals(expected, output);
 }

 @Test
 public void testDecodeBinaryData() throws DecoderException {
 byte[] input = {0x01, 0x0D, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35};
 byte[] expected = {0x01, 0x02, 0x03, 0x04, 0x05};
 byte[] output = codec.decode(input);
 assertArrayEquals(expected, output);
 }

 @Test
 public void testDecodeStringWithDefaultCharset() throws DecoderException {
 String input = "=3HELLO=20World=%3D";
 String expected = "Hello World!";
 String output = codec.decode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testDecodeStringWithSpecifiedCharset() throws DecoderException, UnsupportedEncodingException {
 String input = "=3HELLO=20World=%3D";
 String charset = "ISO-8859-1";
 String expected = "Hello World!";
 String output = codec.decode(input, charset);
 assertEquals(expected, output);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testUnsupportedCharset() throws UnsupportedEncodingException {
 String charset = "X-Puppies";
 codec.decode("", charset);
 }

 @Test(expected = DecoderException.class)
 public void testInvalidQuotedPrintableData() throws DecoderException {
 byte[] input = {0x01, 0x0D, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x06};
 byte[] output = codec.decode(input);
 fail("No DecoderException thrown");
 }

 @Test
 public void testEncodeAndDecodeRoundtrip() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }
}