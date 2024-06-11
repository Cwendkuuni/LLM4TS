import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A short string with numbers and special characters!@#$%^&*()_+-=[]{}|;:,'.<>/? ";
 String expectedOutput = "A SHORT STRING";
 String output = SoundexUtils.clean(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceNullEncoder() throws EncoderException {
 String s1 = "Soundex Test";
 String s2 = "Refined Soundex Test";
 SoundexUtils.difference(null, s1, s2);
 }

 @Test
 public void testDifferenceEmptyStrings() {
 String s1 = "";
 String s2 = "";
 int expectedOutput = 4;
 int output = SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), SoundexUtils.clean(s2));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceIdenticalStrings() {
 String s1 = "Soundex Test";
 String s2 = "Soundex Test";
 int expectedOutput = 4;
 int output = SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), SoundexUtils.clean(s2));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceSoundexStrings() {
 String s1 = "Apple";
 String s2 = "Orange";
 int expectedOutput = 1;
 int output = SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), SoundexUtils.clean(s2));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceRefinedSoundexStrings() {
 String s1 = "Apple";
 String s2 = "Orang Utan";
 int expectedOutput = 3;
 int output = SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), SoundexUtils.clean(s2));
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testDifferenceNullString() {
 String s1 = "Soundex Test";
 String s2 = null;
 int expectedOutput = 0;
 int output = SoundexUtils.differenceEncoded(SoundexUtils.clean(s1), SoundexUtils.clean(s2));
 assertEquals(expectedOutput, output);
 }
}