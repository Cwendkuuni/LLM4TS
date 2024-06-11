import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String encoded = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", encoded);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String encoded = caverphone.caverphone("");
 Assert.assertEquals("1111111111", encoded);
 }

 @Test
 public void testCaverphoneLowercase() {
 String input = "hello world";
 String encoded = caverphone.caverphone(input);
 String expected = "he2llo2w2rld";
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneRemoveNonLetters() {
 String input = "123hello world123";
 String encoded = caverphone.caverphone(input);
 String expected = "he2llo2w2rld";
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "enough";
 String encoded = caverphone.caverphone(input);
 String expected = "enou2f";
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String input = "coughenough";
 String encoded = caverphone.caverphone(input);
 String expected = "cou2fenou2f";
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String input = "action tchat";
 String encoded = caverphone.caverphone(input);
 String expected = "act2ion2chat";
 Assert.assertEquals(expected, encoded);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String input = "two2threes";
 String encoded = caverphone.caverphone(input);
 String expected = "threes";
 Assert.assertEquals(expected, encoded);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonStringInput() {
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
 public void testCaverphoneNotEqualStrings() {
 String str1 = "hello world";
 String str2 = "hello universe";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}