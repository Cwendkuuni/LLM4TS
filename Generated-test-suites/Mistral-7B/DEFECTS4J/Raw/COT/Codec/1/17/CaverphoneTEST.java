import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmpty() {
 String result = caverphone.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "he2llo";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "123heLl0o";
 String result = caverphone.caverphone(input);
 String expected = "heLlo";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "he2llo";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String input = "cough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String input = "cqci cecy cy tch c c q q x v dg tio tia d ph b sh z gs tpt pk fm nw3 w3 why wh3 w wh r3 r l3 ly l j y why wy wh wy j y 3gh3 gh g s+ t+ p+ k+ f+ m+ n+ w3 wy wh3 w w h h r3 r l3 ly l j y j y 3gh3 gh g s+ t+ p+ k+ f+ m+ n+ w3 wy wh3 w w h h r3 r l3 ly l j y j y 3gh3 gh g s+ t+ p+ k+ f+ m+ n+";
 String result = caverphone.caverphone(input);
 String expected = "2qsi se3 sy 2ch k k k k f k k f m n N N N N N M N";
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString() {
 caverphone.encode(new Object() {});
 }

 @Test
 public void testEncodeNull() {
 String result = (String) caverphone.encode(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString2() {
 caverphone.encode(123);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsCaverphoneUnequal() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertFalse(result);
 }

}