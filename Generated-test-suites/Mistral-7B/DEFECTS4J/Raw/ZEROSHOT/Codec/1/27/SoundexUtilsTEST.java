import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testCleanMethod() {
 String str = "A short string with numbers: 12345 and special characters: @#$%^&*()_+-={}[]|\\:;\"'<>,.?/~`";
 String expected = "A SHORT STRING WITH NUMBERS: 12345 AND SPECIAL CHARACTERS: ";
 String result = SoundexUtils.clean(str);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 String str = null;
 SoundexUtils.clean(str);
 }

 @Test
 public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
 String encoder1 = "S234";
 String encoder2 = "S342";
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 1;
 int result = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceMethodWithRefinedSoundexEncoder() throws EncoderException {
 String encoder1 = "S234310";
 String encoder2 = "S234312";
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 2;
 int result = SoundexUtils.difference(SoundexUtils.REFINED_SOUNDEX, s1, s2);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceNullInput() throws EncoderException {
 String encoder = null;
 String s1 = "Apple";
 String s2 = "Orange";
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceNullInput1() {
 String encoder = SoundexUtils.REFINED_SOUNDEX;
 String s1 = null;
 String s2 = "Orange";
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceNullInput2() {
 String encoder = SoundexUtils.REFINED_SOUNDEX;
 String s1 = "Apple";
 String s2 = null;
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test
 public void testDifferenceEncodedMethod() {
 String es1 = "S234";
 String es2 = "S342";
 int expected = 1;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithLongerEncodedStrings() {
 String es1 = "S234310";
 String es2 = "S234312";
 int expected = 2;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithDifferentLengths() {
 String es1 = "S234";
 String es2 = "S234312";
 int expected = 1;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedMethodWithEmptyStrings() {
 String es1 = "";
 String es2 = "";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }
}