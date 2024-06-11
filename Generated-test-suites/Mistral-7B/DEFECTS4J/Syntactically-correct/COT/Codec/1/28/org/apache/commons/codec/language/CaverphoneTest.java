import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testLowercaseString() {
 String input = "hello world";
 String result = caverphone.caverphone(input);
 String expected = "h2l2o2w2o2r2l2d2";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabeticalCharacters() {
 String input = "123hello45world67";
 String result = caverphone.caverphone(input);
 String expected = "helloworld";
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = " Caversham Project at the University of Otago.";
 String result = caverphone.caverphone(input);
 String expected = "CavershamProjectattheUniversityofOtago";
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceStartOptions() {
 String input = "CoughCavershamProject";
 String result = caverphone.caverphone(input);
 String expected = "cou2fcou2fproject";
 assertEquals(expected, result);
 }

 @Test
 public void testReplaceLetters() {
 String input = "CavershamProjectatheUniversityofOtago";
 String result = caverphone.caverphone(input);
 String expected = "cau2fprojectatheuniversityofotago";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringParameter() {
 caverphone.encode(123);
 }

 @Test
 public void testEncodeNullString() {
 String result = caverphone.encode(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeEmptyString() {
 String result = caverphone.encode("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeString() {
 String input = "CavershamProjectatheUniversityofOtago";
 String result = caverphone.encode(input);
 String expected = "cau2fprojectatheuniversityofotago";
 assertEquals(expected, result);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "hello world";
 String str2 = "hello world1";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }
}