import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphone.caverphone("");
 String expected = "1111111111";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneNullString() {
 try {
 caverphone.caverphone(null);
 fail("NullPointerException expected");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphone.caverphone("Hello world!");
 String expected = "H2L3O3W2R2L2D2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithoutVowels() {
 String result = caverphone.caverphone("Thismustwork");
 String expected = "Th3smustw3rk";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String result = caverphone.caverphone("CoughCoughRoughToughEnoughTough"); // replace all cases
 String expected = "Cou2fRou2fTou2fEnou2fTou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String result = caverphone.caverphone("CipherCyprusCvc");
 String expected = "C2ph3rCyprusCvc";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String result = caverphone.caverphone("TwoThreesTwo");
 String expected = "Th3s";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesDifferentLengthStrings() {
 String shorterString = "ShortString";
 String longerString = "ThisIsAShorterStringThanTheOtherString";
 String shorterCaverphone = caverphone.caverphone(shorterString);
 String longerCaverphone = caverphone.caverphone(longerString);
 StringBuilder builder = new StringBuilder(shorterCaverphone);
 for (int i = 0; i < longerCaverphone.length() - shorterCaverphone.length(); i++) {
 builder.append("1");
 }
 String expected = builder.toString();
 Assert.assertEquals(expected, longerCaverphone.substring(0, shorterCaverphone.length()));
 }

 @Test (expected = EncoderException.class)
 public void testCaverphoneNonStringParameter() {
 caverphone.encode(new Object());
 }

 @Test
 public void testIsCaverphoneEqualTrue() {
 String str1 = "ThisIsATestString";
 String str2 = "ThisIsATestString";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String str1 = "ThisIsATestString";
 String str2 = "ThisIsAnotherTestString";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

}