import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for Caverphone class
 */
public class CaverphoneTest {

 private final StringEncoder caverphone = new Caverphone();

 /**
  * Test encoding an empty String
  */
 @Test
 public void testEmptyString() {
 String encoded = caverphone.encode("");
 assertEquals("1111111111", encoded);
 }

 /**
  * Test encoding a String with only lowercase letters
  */
 @Test
 public void testLowercaseLetters() {
 String encoded = caverphone.encode("hello");
 assertEquals("h2h2l2o2l2o", encoded);
 }

 /**
  * Test encoding a String with uppercase letters
  */
 @Test
 public void testUppercaseLetters() {
 String encoded = caverphone.encode("HELLO");
 assertEquals("H2H2L2O2L2O", encoded);
 }

 /**
  * Test encoding a String with digits
  */
 @Test
 public void testDigits() {
 String encoded = caverphone.encode("12345");
 assertEquals("12345", encoded);
 }

 /**
  * Test encoding a String with special characters
  */
 @Test
 public void testSpecialCharacters() {
 String encoded = caverphone.encode("Hello, World!");
 assertThrows(EncoderException.class, () -> caverphone.encode("Hello, World!"));
 }

 /**
  * Test encoding a String with empty spaces
  */
 @Test
 public void testEmptySpace() {
 String encoded = caverphone.encode(" ");
 assertEquals("1", encoded);
 }

 /**
  * Test decoding an encoded String
  */
 @Test
 public void testDecode() throws DecoderException {
 String encoded = "h2h2l2o2l2o";
 String decoded = new StringDecoder().decode(encoded);
 assertEquals("hello", decoded);
 }

 /**
  * Test encoding a null String
  */
 @Test(expected = NullPointerException.class)
 public void testNullString() {
 caverphone.encode(null);
 }
}