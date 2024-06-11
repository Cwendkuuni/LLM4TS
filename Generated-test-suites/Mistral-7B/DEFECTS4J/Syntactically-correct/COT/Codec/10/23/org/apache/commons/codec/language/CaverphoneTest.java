import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException when given null String");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphone.caverphone("hello world");
 assertEquals("h2oW2orl2el2l2o", result);
 }

 @Test
 public void testCaverphoneRemoveNonAlpha() {
 String result = caverphone.caverphone("123hello456 world789");
 assertEquals("h2l2l2oW2orl2el2l2o", result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String result = caverphone.caverphone("enough");
 assertEquals("enou2f", result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String result = caverphone.caverphone("coughenough");
 assertEquals("cou2fou2f", result);
 }

 @Test
 public void testCaverphoneStartOptions20() {
 String result = caverphone.caverphone("enoughtrough");
 assertEquals("enou2ftrou2f", result);
 }

 @Test
 public void testCaverphoneStartOptionsError() {
 String result = caverphone.caverphone("enoughtroughenough");
 assertEquals("enou2ftrou2fou2f", result);
 }

 @Test
 public void testCaverphoneReplace() {
 String result = caverphone.caverphone("quietcyqw");
 assertEquals("qi2qw", result);
 }

 @Test
 public void testCaverphoneReplace20() {
 String result = caverphone.caverphone("quitecyjw");
 assertEquals("qi2tjw", result);
 }

 @Test
 public void testCaverphoneReplaceError() {
 String result = caverphone.caverphone("quitecyjwy");
 assertEquals("qi2tjw3y", result);
 }

 @Test
 public void testCaverphoneRemoveDuplicate() {
 String result = caverphone.caverphone("aaabbbbccc");
 assertEquals("accc", result);
 }

 @Test
 public void testCaverphoneRemoveFinalDigit() {
 String result = caverphone.caverphone("hello123");
 assertEquals("h2oW2orl2el2l2o", result);
 }

 @Test
 public void testCaverphoneRemoveFinalDigit20() {
 String result = caverphone.caverphone("hello12345");
 assertEquals("h2oW2orl2el2l2oA", result);
 }

 @Test
 public void testCaverphoneSameString() {
 String result1 = caverphone.caverphone("hello world");
 String result2 = caverphone.caverphone("hello world");
 assertTrue(caverphone.isCaverphoneEqual(result1, result2));
 }

 @Test
 public void testCaverphoneDifferentStrings() {
 String result1 = caverphone.caverphone("hello world");
 String result2 = caverphone.caverphone("hello universe");
 assertFalse(caverphone.isCaverphoneEqual(result1, result2));
 }
}