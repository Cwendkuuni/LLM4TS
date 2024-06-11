import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoder.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Caverphone encoder and decoder
 */
public class CaverphoneTest {

 private StringEncoder caverphoneEncoder = new Caverphone();
 private StringDecoder caverphoneDecoder = new StringDecoder();

 @Test
 public void testEncodeAndDecode() {
 String original = "hello world";
 String encoded = caverphoneEncoder.encode(original);
 String decoded = caverphoneDecoder.decode(encoded);
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeNull() {
 assertThrows(NullPointerException.class, () -> caverphoneEncoder.encode(null));
 }

 @Test
 public void testEncodeEmpty() {
 assertEquals("1111111111", caverphoneEncoder.encode(""));
 }

 @Test
 public void testEncodeLowercase() {
 assertEquals("h3l3o2w0rld", caverphoneEncoder.encode("hello world"));
 }

 @Test
 public void testEncodeUppercase() {
 assertEquals("H3L3O2W0Rld", caverphoneEncoder.encode("HELLO WORLD"));
 }

 @Test
 public void testEncodeSpecialChars() {
 assertEquals("h3l3o2w0rld$#%&/()=!?@", caverphoneEncoder.encode("hello world$#%&/()=!?@"));
 }

 @Test
 public void testEncodeDigits() {
 assertEquals("h3l3o2w012345", caverphoneEncoder.encode("hello 12345"));
 }

 @Test
 public void testEncodeMixed() {
 assertEquals("h3l3o2w0r1d$#%&/()=!?@5", caverphoneEncoder.encode("hello 12345$#%&/()=!?@5"));
 }

 @Test
 public void testDecodeNull() {
 assertThrows(NullPointerException.class, () -> caverphoneDecoder.decode(null));
 }

 @Test
 public void testDecodeEmpty() {
 assertNull(caverphoneDecoder.decode(""));
 }

 @Test
 public void testDecodeInvalid() {
 assertThrows(DecoderException.class, () -> caverphoneDecoder.decode("11111111111")); // invalid length
 }

 @Test
 public void testDecodeShort() {
 assertNull(caverphoneDecoder.decode("111111111")); // too short
 }

 @Test
 public void testDecodeLong() {
 assertNull(caverphoneDecoder.decode("111111111111111111")); // too long
 }

 @Test
 public void testDecodeInvalidCharacter() {
 assertThrows(DecoderException.class, () -> caverphoneDecoder.decode("1111111111A"));
 }

 @Test
 public void testDecodeInvalidLength() {
 assertThrows(DecoderException.class, () -> caverphoneDecoder.decode("111111111"));
 }

 @Test
 public void testEncodeAndDecodeWithPreciseLength() {
 String original = "0123456789012345";
 String encoded = caverphoneEncoder.encode(original);
 assertEquals(12, encoded.length());
 String decoded = caverphoneDecoder.decode(encoded);
 assertEquals(original, decoded);
 }
}