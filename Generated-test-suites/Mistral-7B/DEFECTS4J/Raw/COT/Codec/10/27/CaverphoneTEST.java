import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException to be thrown");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneLowerCaseString() {
 String input = "hello world";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2l2o2ow2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "12345hello6world7";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o2w2r2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "enough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String input = "tough enough cough";
 String result = caverphone.caverphone(input);
 String expected = "tou2f enou2f cou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String input = "actionable application";
 String result = caverphone.caverphone(input);
 String expected = "ac2ionable ap2plication";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String input = "123caverphone123";
 String result = caverphone.caverphone(input);
 String expected = "";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonStringParameter() {
 caverphone.encode(new Object());
 }

 @Test
 public void testCaverphoneEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testCaverphoneUnequalStrings() {
 String str1 = "hello world";
 String str2 = "hello goodbye";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}