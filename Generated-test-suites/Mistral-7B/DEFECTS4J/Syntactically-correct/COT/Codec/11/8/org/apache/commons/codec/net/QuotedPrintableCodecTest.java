import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testDefaultConstructor() {
 QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec();
 assertNotNull(quotedPrintableCodec);
 }

 @Test
 public void testConstructorWithCharset() {
 QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec("UTF-8");
 assertNotNull(quotedPrintableCodec);
 assertEquals("UTF-8", quotedPrintableCodec.getDefaultCharset());
 }

 @Test
 public void testEncodeByte() throws UnsupportedEncodingException {
 byte[] input = {0x2A, 0x69, 0x6E, 0x66, 0x6F, 0x72};
 byte[] output = codec.encode(input);
 assertArrayEquals(new byte[] {0x3D, 0x2A, 0x3D, 0x69, 0x3D, 0x6E, 0x3D, 0x66, 0x3D, 0x6F, 0x3D, 0x72}, output);
 }

 @Test
 public void testEncodeBytePrintable() throws UnsupportedEncodingException {
 byte[] input = {0x41, 0x42, 0x43};
 byte[] output = codec.encode(input);
 assertArrayEquals(input, output);
 }

 @Test
 public void testEncodeByteWhitespace() throws UnsupportedEncodingException {
 byte[] input = {0x20, 0x20, 0x20};
 byte[] output = codec.encode(input);
 assertEquals(3, output.length);
 for (int i = 0; i < output.length; i++) {
 assertEquals('=', output[i]);
 }
 }

 @Test
 public void testEncodeByteCrlf() throws UnsupportedEncodingException {
 byte[] input = {0x0D, 0x0A};
 byte[] output = codec.encode(input);
 assertEquals(3, output.length);
 assertEquals('=', output[0]);
 assertEquals('D', output[1]);
 assertEquals('C', output[2]);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testEncodeByteUnsupportedCharset() throws UnsupportedEncodingException {
 QuotedPrintableCodec quotedPrintableCodec = new QuotedPrintableCodec("ISO-8859-1");
 byte[] input = {0xC3};
 byte[] output = quotedPrintableCodec.encode(input);
 fail("Should have thrown UnsupportedEncodingException");
 }

 @Test
 public void testDecodeByte() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x3D, 0x2A, 0x3D, 0x69, 0x3D, 0x6E, 0x3D, 0x66, 0x3D, 0x6F, 0x3D, 0x72};
 byte[] output = codec.decode(input);
 assertArrayEquals(new byte[] {0x2A, 0x69, 0x6E, 0x66, 0x6F, 0x72}, output);
 }

 @Test
 public void testDecodeBytePrintable() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x41, 0x42, 0x43};
 byte[] output = codec.decode(input);
 assertArrayEquals(input, output);
 }

 @Test
 public void testDecodeByteEscapedWhitespace() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x3D, 0x20, 0x3D, 0x20, 0x3D, 0x20};
 byte[] output = codec.decode(input);
 assertArrayEquals(new byte[] {0x20, 0x20, 0x20}, output);
 }

 @Test
 public void testDecodeByteCrlf() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x3D, 'D', 0x3D, 'C'};
 byte[] output = codec.decode(input);
 assertArrayEquals(new byte[] {0x0D, 0x0A}, output);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "Test\r\f";
 String output = codec.encode(input);
 assertEquals("=54=65=73=74=3D=0D=0A=0C", output);
 }

 @Test
 public void testDecodeString() throws UnsupportedEncodingException, DecoderException {
 String input = "=54=65=73=74=3D=0D=0A=0C";
 String output = codec.decode(input.getBytes(codec.getDefaultCharset()));
 assertEquals("Test\r\f", output);
 }

 @Test
 public void testEncodeStream() throws UnsupportedEncodingException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 codec.encode(new byte[] {0x31, 0x32, 0x33}, outputStream);
 byte[] output = outputStream.toByteArray();
 assertArrayEquals(new byte[] {0x3D, 0x31, 0x3D, 0x32, 0x3D, 0x33}, output);
 }

 @Test
 public void testDecodeStream() throws UnsupportedEncodingException, DecoderException {
 ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
 inputStream.write(0x3D);
 inputStream.write('3');
 inputStream.write(0x3D);
 inputStream.write('3');
 byte[] input = inputStream.toByteArray();
 byte[] output = codec.decode(input);
 assertArrayEquals(new byte[] {0x31, 0x32, 0x33}, output);
 }

 @Test
 public void testEncodeBitmap() throws UnsupportedEncodingException {
 byte[] input = new byte[] {0x00, 0x01, 0x02, 0x03, 0xFF};
 byte[] output = codec.encode(input);
 byte[] expected = new byte[] {0x3D, 0x00, 0x3D, 0x01, 0x3D, 0x02, 0x3D, 0x03, 0x3D, 0xFF};
 assertArrayEquals(expected, output);
 }

 @Test
 public void testDecodeBitmap() throws UnsupportedEncodingException, DecoderException {
 byte[] input = new byte[] {0x3D, 0x00, 0x3D, 0x01, 0x3D, 0x02, 0x3D, 0x03, 0x3D, 0xFF};
 byte[] output = codec.decode(input);
 assertArrayEquals(input, output);
 }

 @Test
 public void testDecodeBitmapWithInvalidData() throws UnsupportedEncodingException, DecoderException {
 byte[] input = new byte[] {0x3D, 0x00, 0x3D, 0x01, 0x3D, 0x02, 0x3D, 0xFF, 0x3D};
 try {
 codec.decode(input);
 fail();
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeInvalidDataWithInvalidByteSequence() throws UnsupportedEncodingException, DecoderException {
 byte[] input = new byte[] {0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D, 0x3D};
 try {
 codec.decode(input);
 fail();
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeInvalidDataWithMissingDelimiter() throws UnsupportedEncodingException, DecoderException {
 byte[] input = new byte[] {0x3D, 0x00, 0x3D, 0x01, 0x3D, 0x02, 0x3D, 0x03};
 try {
 codec.decode(input);
 fail();
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeInvalidDataWithMissingByte() throws UnsupportedEncodingException, DecoderException {
 byte[] input = new byte[] {0x3D, 0x00, 0x3D, 0x01, 0x3D, 0x02};
 try {
 codec.decode(input);
 fail();
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeStringWithNullInput() throws UnsupportedEncodingException, DecoderException {
 try {
 codec.decode(null);
 fail();
 } catch (DecoderException e) {
 // expected
 }
 }

 @Test
 public void testDecodeStringWithEmptyInput() throws UnsupportedEncodingException, DecoderException {
 String output = codec.decode("", codec.getDefaultCharset());
 assertEquals("", output);
 }
}