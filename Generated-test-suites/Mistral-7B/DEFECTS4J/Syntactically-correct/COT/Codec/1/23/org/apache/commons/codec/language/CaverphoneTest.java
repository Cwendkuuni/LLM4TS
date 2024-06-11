import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String actual = caverphone.caverphone(null);
 String expected = "1111111111";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEmpty() {
 String actual = caverphone.caverphone("");
 String expected = "1111111111";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneLowercase() {
 String actual = caverphone.caverphone("test");
 String expected = "t2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneNoLetters() {
 String actual = caverphone.caverphone("12345");
 String expected = "111111";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String actual = caverphone.caverphone("coughenough");
 String expected = "cou2feno2f";
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneInvalidInput() {
 String actual = caverphone.caverphone(null);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String actual = caverphone.caverphone("tchtqecycxv");
 String expected = "2chq3yc3v";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String actual = caverphone.caverphone("121212223333");
 String expected = "111111";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesHyphen() {
 String actual = caverphone.caverphone("test-test");
 String expected = "t2e2s2t2-t2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesWhitespace() {
 String actual = caverphone.caverphone("test test test");
 String expected = "t2e2s2t2 2e2s2t2 2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesMultipleWords() {
 String actual = caverphone.caverphone("test test test test test test test test test test");
 String expected = "t2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2 2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEncodeString() {
 String actual = caverphone.encode("test");
 String expected = "t2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testEncodeObject() {
 String testString = "test";
 String actual = (String) caverphone.encode(testString);
 String expected = "t2e2s2t2";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "test";
 String str2 = "test";
 boolean actual = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = true;
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "test";
 String str2 = "test1";
 boolean actual = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = false;
 Assert.assertEquals(expected, actual);
 }

}