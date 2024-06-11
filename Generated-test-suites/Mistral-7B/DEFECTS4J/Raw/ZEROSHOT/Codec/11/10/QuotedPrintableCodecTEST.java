import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeDecodeByteArray() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeDecodeString() throws UnsupportedEncodingException, DecoderException {
 String input = "abcdefghijklmnopqrstuvwxyz";
 String encoded = codec.encode(input);
 String decoded = codec.decode(encoded);
 assertEquals(input, decoded);
 }

 @Test
 public void testEncodeCR() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x0D};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeLF() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x0A};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeCRLF() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {0x0D, 0x0A};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalid() throws UnsupportedEncodingException, DecoderException {
 byte[] input = {(byte) 0xFF};
 byte[] encoded = {(byte) 0x61, (byte) 0x62};
 codec.decode(encoded);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalid() throws UnsupportedEncodingException, EncoderException {
 byte[] input = {(byte) 0xFF};
 byte[] encoded = codec.encode(input);
 }
}