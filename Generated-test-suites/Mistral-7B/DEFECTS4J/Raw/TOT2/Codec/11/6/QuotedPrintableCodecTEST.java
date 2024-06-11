import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuotedPrintableCodecTest {

 private final QuotedPrintableCodec codec = new QuotedPrintableCodec();

 @Test
 public void testEncodeByte() {
 byte b = 65; // ASCII 'A'
 byte[] encoded = codec.encode(new byte[] { b });
 String hexEncoded = Hex.encodeHexString(encoded);
 assertEquals("41=", hexEncoded);
 }

 @Test
 public void testEncodeString() throws UnsupportedEncodingException {
 String original = "Hello World!";
 String encoded = codec.encode(original.getBytes("UTF-8"));
 String decoded = new String(codec.decode(encoded.getBytes()), "UTF-8");
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeArray() throws UnsupportedEncodingException {
 String original = "Hello World!";
 byte[] bytes = original.getBytes("UTF-8");
 byte[] encoded = codec.encode(bytes);
 String hexEncoded = Hex.encodeHexString(encoded);
 String decoded = new String(codec.decode(Hex.decodeHex(hexEncoded)), "UTF-8");
 assertEquals(original, decoded);
 }

 @Test
 public void testDecodeByte() {
 byte[] encoded = Hex.decodeHex("41=61=6C=6C=6F=20=57 =6F=72 =6C=64=21");
 byte decoded = codec.decode(encoded)[0];
 assertEquals('A', decoded);
 }

 @Test
 public void testDecodeString() throws UnsupportedEncodingException {
 String encoded = "41=61=6C=6C=6F=20=57=6F=72=6C=64=21";
 byte[] decodedBytes = codec.decode(encoded.getBytes());
 String decoded = new String(decodedBytes, "UTF-8");
 assertEquals("Hello World!", decoded);
 }

 @Test
 public void testDecodeArray() throws UnsupportedEncodingException {
 String encoded = "41=61=6C=6C=6F=20=57=6F=72=6C=64=21";
 byte[] decodedBytes = codec.decode(Hex.decodeHex(encoded));
 String decoded = new String(decodedBytes, "UTF-8");
 assertEquals("Hello World!", decoded);
 }

 @Test
 public void testDecodeInvalid() {
 assertThrows(DecoderException.class, () -> codec.decode("InvalidQuotedPrintableString".getBytes()));
 }

 @Test
 public void testDecodeWhitespaceAtEnd() throws UnsupportedEncodingException {
 String encoded = "76=65=20=6C=6F=63=61=6C=6C=6F=20=57=6F=72=6C=64=21\r\n";
 byte[] decodedBytes = codec.decode(Hex.decodeHex(encoded));
 String decoded = new String(decodedBytes, "UTF-8");
 assertEquals("Versus old world.\r\n", decoded);
 }

 @Test
 public void testEncodeWhitespaceAtStart() throws UnsupportedEncodingException {
 String original = "\r\nHello World!";
 byte[] encoded = codec.encode(original.getBytes("UTF-8"));
 String decoded = new String(codec.decode(encoded), "UTF-8");
 assertEquals(original, decoded);
 }

 @Test
 public void testEncodeControlCharacters() throws UnsupportedEncodingException {
 String original = "\r\n\tHello World!\r\n";
 byte[] encoded = codec.encode(original.getBytes("UTF-8"));
 String decoded = new String(codec.decode(encoded), "UTF-8");
 assertEquals(original, decoded);
 }

 @Test
 public void testDecodeControlCharacters() throws UnsupportedEncodingException {
 String encoded = "\r\n\r\n=0D=0D=09=48=65=6C=6C=6F=20=57=6F=72=6C=64=21\r\n";
 byte[] decodedBytes = codec.decode(Hex.decodeHex(encoded));
 String decoded = new String(decodedBytes, "UTF-8");
 assertEquals(original = "\r\n\tHello World!\r\n", decoded);
 }
}