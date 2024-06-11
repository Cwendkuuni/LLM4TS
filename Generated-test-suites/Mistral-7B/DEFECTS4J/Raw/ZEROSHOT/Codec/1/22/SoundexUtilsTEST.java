import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A quick Brown fox jumps over the lazy dog";
 String expected = "A E B F X P S O T H E L A Z Y D";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result.toUpperCase());
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWhenNullEncoder() {
 SoundexUtils.difference(null, "test1", "test2");
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWhenNullString1() {
 StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
 int result = SoundexUtils.difference(encoder, null, "test2");
 assertEquals(0, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceThrowsExceptionWhenNullString2() {
 StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
 int result = SoundexUtils.difference(encoder, "test1", null);
 assertEquals(0, result);
 }

 @Test
 public void testDifferenceSoundex() {
 StringEncoder encoder = new org.apache.commons.codec.net.URLCodec();
 String s1 = "Apple";
 String s2 = "Orange";
 int difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(1, difference);

 s1 = "cat";
 s2 = "bat";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(3, difference);

 s1 = "kitten";
 s2 = "sitting";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(0, difference);

 s1 = "Soundex";
 s2 = "Soundex";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(4, difference);
 }

 @Test
 public void testDifferenceRefinedSoundex() {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = "Apple";
 String s2 = "Orange";
 int difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(1, difference);

 s1 = "cat";
 s2 = "bat";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(3, difference);

 s1 = "kitten";
 s2 = "sitting";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(4, difference);

 s1 = "Soundex";
 s2 = "Soundex";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(4, difference);

 s1 = "Apple";
 s2 = "Apfel";
 difference = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(3, difference);
 }

}