import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testNullString() {
 try {
 caverphone.caverphone(null);
 } catch (EncoderException e) {
 // expected exception
 }
 }

 @Test
 public void testLowerCase() {
 String input = "hello world";
 String expected = "he2lo3 w2orl3";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "123hello456 world789";
 String expected = "hellow";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "enough";
 String expected = "enou2f";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStartOptions() {
 String[] inputs = { "cougH", "cough", "Tough", "tough", "enough", "trough", "trou2f" };
 String[] expecteds = { "cou2f", "cou2f", "tou2f", "tou2f", "enou2f", "trou2f", "trou2f" };
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String expected = expecteds[i];
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testReplacements() {
 String[] inputs = { "cq", "ci", "ce", "cy", "tch", "c", "q", "x", "v", "dg", "tio", "tia", "d", "ph", "b", "sh", "z", "j", "y", "w3", "why", "wh3", "w", "h", "r3", "r", "l3", "l", "3gh3", "gh", "g", "s+", "t+", "p+", "k+", "f+", "m+", "n+", "w3", "r3", "r", "l3", "l", "j", "y", "Y3", "y", "3" };
 String[] expecteds = { "2q", "si", "se", "sy", "2ch", "k", "k", "k", "f", "2g", "sio", "sia", "t", "fh", "p", "s2", "s", "y", "y3", "Wh3", "Wh3", "w", "A", "r3", "r", "L3", "L3", "2", "3gh3", "22", "g", "S", "T", "P", "K", "F", "M", "N", "W3", "R3", "R", "L3", "L", "y", "Y3", "y", "3" };
 for (int i = 0; i < inputs.length; i++) {
 String input = inputs[i];
 String expected = expecteds[i];
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }
 }

 @Test
 public void testHandleRemovals() {
 String input = "222222222kfkfkfkfg";
 String expected = "";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testHandleLength() {
 String input = "123456789012345678901234567890123";
 String expected = "1111111111111111111111111111111111";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result.substring(0, 22));
 }

 @Test
 public void testEncode() throws EncoderException {
 String input = "hello world";
 String expected = "he2lo3 w2orl3";
 String result = (String) caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "he2lo3 w2orl3";
 String str2 = "he2lo3 w2orl3";
 String str3 = "he2lo3 w2orl2";
 boolean result1 = caverphone.isCaverphoneEqual(str1, str1);
 assertEquals(true, result1);
 result1 = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(false, result1);
 result1 = caverphone.isCaverphoneEqual(str1, str3);
 assertEquals(false, result1);
 }
}