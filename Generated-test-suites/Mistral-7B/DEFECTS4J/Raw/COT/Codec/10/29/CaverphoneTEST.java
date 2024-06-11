import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String expectedResult = "1111111111";
 String result = caverphoneInstance.caverphone("");
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneNullString() {
 Assert.assertThrows(EncoderException.class, () -> caverphoneInstance.caverphone(null));
 }

 @Test
 public void testCaverphoneLowerCaseString() {
 String input = "hello world";
 String expectedResult = "h2l2o2w2d2";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "123hello4world5";
 String expectedResult = "h2l2o2w2d2";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "enough";
 String expectedResult = "enou2f";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneHandleStartOptions() {
 String input = "cough";
 String expectedResult = "cou2f";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneHandleReplacements() {
 String input = "queencity";
 String expectedResult = "qu3nci5y";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneHandleRemovals() {
 String input = "1a2b3c3d4e5f6g";
 String expectedResult = "";
 String result = caverphoneInstance.caverphone(input);
 Assert.assertEquals(expectedResult, result);
 }

 @Test (expected = EncoderException.class)
 public void testCaverphoneNonStringParameter() {
 caverphoneInstance.encode(123);
 }

 @Test
 public void testCaverphoneEquals() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean expectedResult = true;
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testCaverphoneNotEquals() {
 String str1 = "hello world";
 String str2 = "hello world!";
 boolean expectedResult = false;
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
 Assert.assertEquals(expectedResult, result);
 }

}