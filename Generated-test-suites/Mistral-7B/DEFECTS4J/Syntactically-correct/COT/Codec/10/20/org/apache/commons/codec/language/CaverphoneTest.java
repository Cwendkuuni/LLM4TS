import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmpty() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String txt = "hello world";
 String result = caverphone.caverphone(txt);
 String expected = "h2h2l2l2o2o2w2w2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabets() {
 String txt = "Hello123World456!";
 String result = caverphone.caverphone(txt);
 String expected = "HlWr";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneInvalidInput() {
 String txt = null;
 caverphone.encode(txt);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String txt = "CoughCakeRoughAppleToughEnoughTrough";
 String result = caverphone.caverphone(txt);
 String expected = "cou2fkak2frou2fappletou2fthou2fthou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplace() {
 String txt = "CuQuiCeCyTchCiCq";
 String result = caverphone.caverphone(txt);
 String expected = "2qsi2qse2q2ch2si";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesExceptionalCases() {
 String txt = "CavershamProjectAtTheUniversityOfOtago";
 String result = caverphone.caverphone(txt);
 String expected = "CavershAmPrj3ct3tU3s3t3tU3si3v3r3sh3";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneEncodeObject() {
 String input = "Test Input";
 try {
 String result = (String) caverphone.encode(input);
 String expected = caverphone.caverphone(input);
 assertEquals(expected, result);
 } catch (EncoderException e) {
 fail("Encode method should not throw exception for String input");
 }
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "Test1 Test2";
 String str2 = "Test1 Test2";
 assertTrue(caverphone.isCaverphoneEqual(str1, str2));
 }

 @Test
 public void testCaverphoneIsCaverphoneNotEqual() {
 String str1 = "Test1 Test2";
 String str2 = "Test1 Test3";
 assertFalse(caverphone.isCaverphoneEqual(str1, str2));
 }

}