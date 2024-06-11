import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 String expected = "1111111111";
 assertEquals(expected, result);
 }

 @Test
 public void testNullString() {
 try {
 String result = caverphone.caverphone(null);
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testLowerCaseString() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2o2o";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "hello123";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2";
 // 2.0 only
 assertEquals(expected, result);
 }

 @Test
 public void testHandleStartOptions() {
 String input = "CoughCaversham";
 String result = caverphone.caverphone(input);
 String expected = "cou2fcou2faversh2am";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleReplacements() {
 String input = "catChase";
 String result = caverphone.caverphone(input);
 String expected = "2q2t2chas5e";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleRepetitions() {
 String input = "aaabbbccc";
 String result = caverphone.caverphone(input);
 String expected = "A3B3C3";
 assertEquals(expected, result);
 }

 @Test
 public void testHandleRemovals() {
 String input = "12345";
 String result = caverphone.caverphone(input);
 String expected = "";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlePaddings() {
 String input = "cat";
 String result = caverphone.caverphone(input);
 String expected = "cat1111111111";
 assertEquals(expected, result);
 }

 @Test
 public void testStringComparison() {
 String str1 = "cat";
 String str2 = "bat";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = false;
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeObject() {
 String input = "cat";
 try {
 Object result = caverphone.encode(null);
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // expected
 }

 try {
 Object result = caverphone.encode(new Integer(1));
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // expected
 }

 String encoded = caverphone.encode("cat");
 String expectedEncoded = "cat1111111111";
 byte[] decoded = StringUtils.decodeBase32(encoded);
 String decodedString = new String(decoded, "US-ASCII");
 assertEquals(expectedEncoded, decodedString);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidObject() {
 caverphone.encode(new Integer(1));
 }
}