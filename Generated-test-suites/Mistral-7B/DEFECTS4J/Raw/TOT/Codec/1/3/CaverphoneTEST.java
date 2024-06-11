import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoding.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder encoder = new Caverphone();
 private final StringDecoder decoder = new Caverphone();

 @Test
 public void testEncodeAndDecodeEmptyString() {
     String encoded = encoder.encode("");
     String decoded = decoder.decode(encoded);
     assertEquals("1111111111", decoded);
 }

 @Test
 public void testEncodeAndDecodeValidString() {
     String input = "The quick brown fox jumps over the lazy dog";
     String encoded = encoder.encode(input);
     String decoded = decoder.decode(encoded);
     assertTrue(input.equals(decoder.decode(encoded)));
 }

 @Test
 public void testEncodeAndDecodeNullString() {
     try {
         encoder.encode(null);
         fail("Expected EncoderException");
     } catch (EncoderException e) {
         // expected
     }

     try {
         decoder.decode(null);
         fail("Expected DecoderException");
     } catch (DecoderException e) {
         // expected
     }
 }

 @Test
 public void testEncodeAndDecodeUpperCaseString() {
     String input = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
     String encoded = encoder.encode(input);
     String decoded = decoder.decode(encoded);
     assertTrue(input.equals(decoder.decode(encoded)));
 }

 @Test
 public void testEncodeAndDecodeSpecialCharsString() {
     String input = "The quick brown fox! jumps over the lazy dog#";
     String encoded = encoder.encode(input);
     String decoded = decoder.decode(encoded);
     assertTrue(input.equals(decoder.decode(encoded)));
 }

 @Test
 public void testEncodeAndDecodeEmptyCode() {
     String encoded = "1111111111";
     String decoded = decoder.decode(encoded);
     assertEquals(encoded, decoder.decode(decoder.encode(decoded)));
 }

 @Test
 public void testEncodeAndDecodeInvalidCode() {
     String encoded = "11111111111";
     String decoded = decoder.decode(encoded);
     assertFalse(encoder.isCaverphoneEqual(decoder.decode(encoder.encode(" invalid")), decoded));
 }

 @Test
 public void testIsCaverphoneEqual() {
     String str1 = "The quick brown fox jumps over the lazy dog";
     String str2 = "The quick brown fox jumps over the lazy dog";
     String str3 = "The quick brown fox jumps over the lazy cat";
     assertTrue(encoder.isCaverphoneEqual(str1, str2));
     assertFalse(encoder.isCaverphoneEqual(str1, str3));
 }

}