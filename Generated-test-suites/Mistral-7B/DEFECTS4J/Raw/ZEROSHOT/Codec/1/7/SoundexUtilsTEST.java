import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "The quick Brown fox jumps over the lazy Dog";
 String expected = "The quick Brown Fox Jumps over the lazy Dog";
 String result = SoundexUtils.clean(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithSoundexEncoder() throws EncoderException {
 StringEncoder encoder = new SoundexEncoder();
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 Assert.assertEquals(expected, result);

 s1 = "Apple";
 s2 = "Apple";
 expected = 4;
 result = SoundexUtils.difference(encoder, s1, s2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncoded() {
 String es1 = "A123";
 String es2 = "A132";
 int expected = 1;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 Assert.assertEquals(expected, result);

 es1 = "A123";
 es2 = "A123";
 expected = 4;
 result = SoundexUtils.differenceEncoded(es1, es2);
 Assert.assertEquals(expected, result);
 }

}