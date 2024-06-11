import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

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
 public void testLowerCase() {
 String input = "hello";
 String expected = "h2l3lo2";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "Hello1World!";
 String expected = "hllwrld";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "even";
 String expected = "even";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);

 String input2 = "evens";
 String expected2 = "evenS";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);
 }

 @Test
 public void testHandlesStartOptions() {
 String input = "CoughPotato";
 String expected = "cou2fpotatotou2f";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);

 String input2 = "RoughPotato";
 String expected2 = "rou2fpotatotou2f";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);
 }

 @Test
 public void testHandlesReplacements() {
 String input = "Cucumber";
 String expected = "2qum2b3r";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);

 String input2 = "Cucumbers";
 String expected2 = "2qum2bs2r";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);
 }

 @Test
 public void testHandlesSpecialCases() {
 String input = "CoughEnough";
 String expected = "cou2fenou2f";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);

 String input2 = "EnoughTrough";
 String expected2 = "enou2ftrou2f";
 String result2 = caverphone.caverphone(input2);
 assertEquals(expected2, result2);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInput() {
 String input = new String(new byte[0]);
 caverphone.encode(input);
 }

 @Test
 public void testEncode() {
 String input = "Cucumber";
 String expected = "2qum2b3r";
 String result = caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "Cucumber";
 String str2 = "Cucumber";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "Cucumber";
 String str2 = "CucumberS";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

}