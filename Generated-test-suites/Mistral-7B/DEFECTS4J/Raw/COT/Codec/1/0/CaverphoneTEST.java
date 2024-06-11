import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphoneInstance.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphoneInstance.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphoneInstance.caverphone("hello");
 Assert.assertEquals("h2h2l2o2l2o", result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabet() {
 String result = caverphoneInstance.caverphone("1Hello2World3");
 Assert.assertEquals("HlWrld", result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String result = caverphoneInstance.caverphone("enough");
 Assert.assertEquals("enou2f", result);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String result = caverphoneInstance.caverphone("coughtough");
 Assert.assertEquals("cou2fto2f", result);
 }

 @Test
 public void testCaverphoneReplacements() {
 String result = caverphoneInstance.caverphone("quality");
 Assert.assertEquals("q3lity", result);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String result = caverphoneInstance.caverphone("23232323");
 Assert.assertEquals("", result);
 }

 @Test
 public void testCaverphoneHandlesExceptions() {
 Assert.assertThrows(EncoderException.class, () -> {
 caverphoneInstance.encode(new Object() {});
 });
 }

 @Test
 public void testCaverphoneIsEqual() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
 Assert.assertTrue(result);
 }

 @Test
 public void testCaverphoneIsNotEqual() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
 Assert.assertFalse(result);
 }

}