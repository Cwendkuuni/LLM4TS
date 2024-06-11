import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testBasicCaverphone() {
 String txt = "hello world";
 String result = caverphone.caverphone(txt);
 String expected = "hllo2w0rld";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneWithStartOptions() {
 String txt = "CoughEnough";
 String result = caverphone.caverphone(txt);
 String expected = "cou2fenou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testReplacements() {
 String txt = "quality";
 String result = caverphone.caverphone(txt);
 String expected = "qali3ty";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesCapitalLetters() {
 String txt = "Hello World";
 String result = caverphone.caverphone(txt);
 String expected = "hllo2w0rld";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesRemovals() {
 String txt = "The Quick Brown Fox Jumps Over The Lazy Dog";
 String result = caverphone.caverphone(txt);
 String expected = "thqckbrwnfxjmpsvrthlzyd";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesMultipleReplaceAndRemoval() {
 String txt = "The Quick Brown Fox Jumps Over The Lazy Dog Said John Doe";
 String result = caverphone.caverphone(txt);
 String expected = "thqckbrwnfxjmpsvrthlzydjohndoe";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "world hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = true;
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneNotEqual() {
 String str1 = "hello world";
 String str2 = "world hello world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = false;
 assertEquals(expected, result);
 }

}