import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an ApplE aPple";
 String expected = "A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedNullStrings() {
 String es1 = null;
 String es2 = null;
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedEmptyStrings() {
 String es1 = "";
 String es2 = "";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedDifferentLengthStrings() {
 String es1 = "apple";
 String es2 = "banana";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedSameStrings() {
 String es1 = "apple";
 String es2 = "apple";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedIdenticalSubstrings() {
 String es1 = "apple";
 String es2 = "appEl";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedSimilarStrings() {
 String es1 = "apple";
 String es2 = "apricot";
 int expected = 3;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedDifferentEncodings() {
 String s1 = "Apple";
 String s2 = " apple ";
 StringEncoder encoder = new SoundexEncoder();
 String es1 = encoder.encode(s1);
 String es2 = SoundexUtils.clean(s2);
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceEncodedThrowsExceptionOnNullEncoder() {
 String es1 = "apple";
 String es2 = "banana";
 StringEncoder encoder = null;
 int expectedDifference = 0;
 try {
 int result = SoundexUtils.differenceEncoded(es1, es2, encoder);
 fail("EncoderException expected");
 } catch (EncoderException e) {
 // Expected
 }
 assertEquals(expectedDifference, 0);
 }
}