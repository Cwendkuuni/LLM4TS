import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

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
 String result = caverphone.caverphone(input);
 String expected = "He2l2o2W2r2d";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabet() {
 String input = "1234Hello5678World90";
 String result = caverphone.caverphone(input);
 String expected = "He2l2o2W2r2d";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "ApacheCommonsCodec";
 String result = caverphone.caverphone(input);
 String expected = "ApacheCommonsCod";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "CoughCaverphone";
 String result = caverphone.caverphone(input);
 String expected = "cou2f2Caverphone";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringInput() {
 caverphone.encode(123);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNullInput() {
 caverphone.encode(null);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "Apple";
 String str2 = "Apple";
 Boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String str1 = "Apple";
 String str2 = "Banana";
 Boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test
 public void testCaverphoneHandlesDuplicateLetters() {
 String input = "Mississippi";
 String result = caverphone.caverphone(input);
 String expected = "MisMis2i2p2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesLongStrings() {
 String input = "The quick brown fox jumps over the lazy dog";
 String result = caverphone.caverphone(input);
 String expected = "Th3 q1ck br0wn fox j2m2p3s 0v3r th3 l4z1 d0g";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphone10CharacterLimit() {
 String input = "The quick brown fox jumps over the lazy dog The quick brown fox jumps over the lazy dog";
 String result = caverphone.caverphone(input);
 String expected = "Th3 q1ck br0wn fox j2m2p3s 0v3r th3 l4z1 d0g";
 assertEquals(expected, result.substring(0, 10));
 }
}