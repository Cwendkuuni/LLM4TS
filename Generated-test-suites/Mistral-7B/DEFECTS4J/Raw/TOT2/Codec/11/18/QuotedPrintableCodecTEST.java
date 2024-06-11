import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.ByteArrayOutputStream;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByteArray() {
 byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
 byte[] expectedOutput = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x30, 0x3D, 0x31, 0x35, 0x3A, 0x33, 0x31, 0x2E, 0x31, 0x30};
 byte[] output = codec.encode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String input = "Hello, World!";
 String expectedOutput = "Hello,%20World%21";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeObject() throws UnsupportedEncodingException {
 String input = "Hello, World!";
 Object expectedOutput = "Hello,%20World%21";
 Object output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeByteArray() throws DecoderException {
 byte[] input = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64, 0x3D, 0x31, 0x30, 0x30, 0x3D, 0x31, 0x35, 0x3A, 0x33, 0x31, 0x2E, 0x31, 0x30};
 byte[] expectedOutput = {0x68, 0x65, 0x61, 0x64, 0x65, 0x64};
 byte[] output = codec.decode(input);
 Assert.assertArrayEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeString() throws DecoderException, UnsupportedEncodingException {
 String input = "Hello,%20World%21";
 String expectedOutput = "Hello, World!";
 String output = (String) codec.decode(input.getBytes("US-ASCII"));
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeObject() throws DecoderException, UnsupportedEncodingException {
 String input = "Hello,%20World%21";
 Object expectedOutput = "Hello, World!";
 Object output = codec.decode(input.getBytes("US-ASCII"));
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testEncodeStringWithCRLF() throws UnsupportedEncodingException {
 String input = "Hello\nWorld!";
 String expectedOutput = "Hello%0D%0AWorld%21";
 String output = codec.encode(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testDecodeStringWithCRLF() throws DecoderException, UnsupportedEncodingException {
 String input = "Hello%0D%0AWorld%21";
 String expectedOutput = "Hello\nWorld!";
 String output = (String) codec.decode(input.getBytes("US-ASCII"));
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidString() throws UnsupportedEncodingException {
 String input = "Hello%0D%0AWorld%21%21";
 String output = (String) codec.decode(input.getBytes("US-ASCII"));
 // DecoderException is expected
 }
}