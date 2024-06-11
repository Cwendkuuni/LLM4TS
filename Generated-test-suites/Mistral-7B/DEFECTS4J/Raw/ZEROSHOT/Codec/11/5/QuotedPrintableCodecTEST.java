import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec codec;

 @Before
 public void setUp() {
 codec = new QuotedPrintableCodec();
 }

 @Test
 public void testEncodeDecodeByteArray() throws DecoderException, EncoderException {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
 byte[] encoded = codec.encode(input);
 byte[] decoded = codec.decode(encoded);
 assertArrayEquals(input, decoded);
 }

 @Test
 public void testEncodeDecodeString() throws DecoderException, EncoderException, UnsupportedEncodingException {
 String input = "abcdefg";
 String charset = "UTF-8";
 String encoded = codec.encode(input, charset);
 String decoded = codec.decode(encoded, charset);
 assertEquals(input, decoded);
 }

 @Test(expected = UnsupportedEncodingException.class)
 public void testDecodeStringWithUnsupportedCharset() throws DecoderException {
 String input = "abcd";
 String charset = "X-PONY";
 String decoded = codec.decode(input, charset);
 }

 @Test
 public void testEncodeDecodeObject() throws DecoderException, EncoderException {
 byte[] input = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
 Object encoded = codec.encode(input);
 Object decoded = codec.decode(encoded);
 assertArrayEquals(input, (byte[]) decoded);
 }

 @Test
 public void testEncodeDecodeStringObject() throws DecoderException, EncoderException, UnsupportedEncodingException {
 String input = "abcdefg";
 String charset = "UTF-8";
 Object encoded = codec.encode(input);
 Object decoded = codec.decode(encoded);
 assertEquals(input, decoded);
 }

 @Test(expected = ClassCastException.class)
 public void testDecodeObjectWithIncorrectType() throws DecoderException {
 String input = "abcd";
 Object encoded = input;
 Object decoded = codec.decode(encoded);
 }
}