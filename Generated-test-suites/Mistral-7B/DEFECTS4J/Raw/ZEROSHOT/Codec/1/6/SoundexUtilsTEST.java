import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an Apple a day keeps the Doctor away!";
 String expected = "A A A A A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithSoundexEncoder() throws EncoderException {
 StringEncoder encoder = new SoundexEncoder();
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);

 s1 = "Apple";
 s2 = "Apple";
 expected = 4;
 result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncoded() {
 String es1 = "AP320";
 String es2 = "OR310";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);

 es1 = "AP320";
 es2 = "AP320";
 expected = 4;
 result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

}