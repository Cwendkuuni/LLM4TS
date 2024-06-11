import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

 /**
 * Test for clean method.
 */
 @Test
 public void testClean() {
 String input = "aBc 123dEf456";
 String expected = "ABC";
 String result = SoundexUtils.clean(input);
 Assert.assertEquals(expected, result);
 }

 /**
 * Test for difference method with Soundex encoder.
 */
 @Test
 public void testDifferenceSoundex() throws EncoderException {
 String encoder = new Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int result = SoundexUtils.difference(encoder, str1, str2);
 int expected = 1;
 Assert.assertEquals(expected, result);
 }

 /**
 * Test for differenceEncoded method with Soundex encoded strings.
 */
 @Test
 public void testDifferenceEncodedSoundex() {
 String es1 = "A223";
 String es2 = "A130";
 int result = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 1;
 Assert.assertEquals(expected, result);
 }

 /**
 * Test for difference method with RefinedSoundex encoder.
 */
 @Test
 public void testDifferenceRefinedSoundex() throws EncoderException, DecoderException {
 String encoder = new RefinedSoundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int result = SoundexUtils.difference(encoder, str1, str2);
 int expected = 2;
 Assert.assertEquals(expected, result);
 }

 /**
 * Test for differenceEncoded method with RefinedSoundex encoded strings.
 */
 @Test
 public void testDifferenceEncodedRefinedSoundex() {
 String es1 = "A223000";
 String es2 = "A131110";
 int result = SoundexUtils.differenceEncoded(es1, es2);
 int expected = 2;
 Assert.assertEquals(expected, result);
 }
}