import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final StringEncoder caverphoneEncoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String encoded = caverphoneEncoder.encode("");
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testNullString() {
 try {
 caverphoneEncoder.encode(null);
 fail("Expected EncoderException for null string");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSingleCharacter() {
 String encoded = caverphoneEncoder.encode("a");
 assertEquals("A", encoded);
 }

 @Test
 public void testSingleCharacterUppercase() {
 String encoded = caverphoneEncoder.encode("A");
 assertEquals("A", encoded);
 }

 @Test
 public void testMultipleCharacters() {
 String encoded = caverphoneEncoder.encode("abc");
 assertEquals("222", encoded);
 }

 @Test
 public void testMultipleCharactersDifferent() {
 String encoded = caverphoneEncoder.encode("abcd");
 assertEquals("2223", encoded);
 }

 @Test
 public void testLongString() {
 String longString = StringUtils.repeat("a", 100);
 String encoded = caverphoneEncoder.encode(longString);
 assertTrue(encoded.length() > 0 && encoded.length() <= 20);
 }

 @Test
 public void testCaverphoneStartsWithCough() {
 String encoded = caverphoneEncoder.encode("cough");
 assertEquals("cou2f", encoded);
 }

 @Test
 public void testCaverphoneStartsWithRough() {
 String encoded = caverphoneEncoder.encode("rough");
 assertEquals("rou2f", encoded);
 }

 @Test
 public void testCaverphoneStartsWithTough() {
 String encoded = caverphoneEncoder.encode("tough");
 assertEquals("tou2f", encoded);
 }

 @Test
 public void testCaverphoneStartsWithEnough() {
 String encoded = caverphoneEncoder.encode("enough");
 assertEquals("enou2f", encoded);
 }

 @Test
 public void testCaverphoneStartsWithTrough() {
 String encoded = caverphoneEncoder.encode("through");
 assertEquals("trou2f", encoded);
 }

 @Test
 public void testCaverphoneStartsWithGn() {
 String encoded = caverphoneEncoder.encode("gn");
 assertEquals("2n", encoded);
 }

 @Test
 public void testCaverphoneStartsWithMb() {
 String encoded = caverphoneEncoder.encode("mb");
 assertEquals("m2", encoded);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String encoded = caverphoneEncoder.encode("ceci");
 assertEquals("si", encoded);
 }

 @Test
 public void testCaverphoneHandlesReplacementsWithMultiple() {
 String encoded = caverphoneEncoder.encode("cecyce");
 assertEquals("sesi", encoded);
 }

 @Test
 public void testCaverphoneHandlesReplacementsWithDifferent() {
 String encoded = caverphoneEncoder.encode("ceciqcic");
 assertEquals("sisik", encoded);
 }

 @Test
 public void testCaverphoneHandlesReplacementsWithLongString() {
 String encoded = caverphoneEncoder.encode("ceciciqciceciqcic");
 assertEquals("sisiksesik", encoded);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneThrowsEncoderExceptionForInvalidInput() {
 caverphoneEncoder.encode("!");
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String encoded = caverphoneEncoder.encode("22");
 assertEquals("", encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovalsWithFinalE() {
 String encoded = caverphoneEncoder.encode("enough");
 assertEquals("enou2f", encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovalsWithMultiple() {
 String encoded = caverphoneEncoder.encode("twothree");
 assertEquals("223", encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovalsWithFinalA() {
 String encoded = caverphoneEncoder.encode("hello");
 assertEquals("helo", encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovalsWithFinal3() {
 String encoded = caverphoneEncoder.encode("three");
 assertEquals("3", encoded);
 }

 @Test
 public void testCaverphoneHandlesErrors() {
 try {
 caverphoneEncoder.encode("!");
 fail("Expected EncoderException for invalid input");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneHandlesErrorsWithNullString() {
 try {
 caverphoneEncoder.encode(null);
 fail("Expected EncoderException for null string");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneHandlesErrorsWithEmptyString() {
 try {
 caverphoneEncoder.encode("");
 fail("Expected EncoderException for empty string");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneEquals() {
 String str1 = "hello";
 String str2 = "hello";
 assertTrue(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testCaverphoneNotEquals() {
 String str1 = "hello";
 String str2 = "world";
 assertFalse(caverphoneEncoder.isCaverphoneEqual(str1, str2));
 }
}