import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "HelloWorld";
 String expected = "H2l2o2w2r2l2o2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String input = "1HelloWorld23";
 String expected = "H2l2o2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "HelloWorlde";
 String expected = "H2l2o2w2r2l2o";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandleStartOptions() {
 String input = "CoughPotato";
 String expected = "cou2fpo2t2o2to2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandleReplacements() {
 String input = "Caterpillar";
 String expected = "cat3r3p2i2l2l2ar";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneSingleCharacter() {
 String input = "a";
 String expected = "A";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneMultipleCharacters() {
 String input = "abc";
 String expected = "a2b2c";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandleRemovals() {
 String input = "1234567890";
 String expected = "";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandleRemovals1() {
 String input = "123456";
 String expected = "23456";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEncodeObjectNull() {
 assertThrows(EncoderException.class, () -> caverphone.encode(null));
 }

 @Test
 public void testCaverphoneEncodeObjectNonString() {
 assertThrows(EncoderException.class, () -> caverphone.encode(new Object()));
 }

 @Test
 public void testCaverphoneEncodeObjectString() {
 String input = "TestString";
 String expected = "T2e2s2t2s2t2r2i2n2g";
 String result = (String) caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testCaverphoneIsCaverphoneNotEqual() {
 String str1 = "HelloWorld";
 String str2 = "HelloWorld";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(false, result);
 }

}