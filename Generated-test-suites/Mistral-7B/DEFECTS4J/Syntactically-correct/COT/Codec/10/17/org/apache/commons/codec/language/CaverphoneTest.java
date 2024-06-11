import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testLowerCase() {
 String input = "HelloWorld";
 String result = caverphone.caverphone(input);
 String expected = "h2l3o2w2r2d";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testRemovesNonAlphabets() {
 String input = "123HelloWorld456";
 String result = caverphone.caverphone(input);
 String expected = "h2l3o2w2r2d";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testRemovesFinalE() {
 String input = "enough";
 String result = caverphone.caverphone(input);
 String expected = "enou2f";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testHandlesStartOptions() {
 String input = "tough";
 String result = caverphone.caverphone(input);
 String expected = "tou2f";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testHandlesReplacements() {
 String input = "action";
 String result = caverphone.caverphone(input);
 String expected = "ac2ion";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testHandlesRemovals() {
 String input = "twoooo apples";
 String result = caverphone.caverphone(input);
 String expected = "2apples";
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertTrue(result);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "HelloWorld";
 String str2 = "WorldHello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertFalse(result);
 }

}