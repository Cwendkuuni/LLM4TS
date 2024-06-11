import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an Apple a day keeps the Doctor away";
 String expected = "A A A A A A A A A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNull() {
 String input = null;
 String result = SoundexUtils.clean(input);
 assertNull(result);
 }

 @Test
 public void testDifferenceEncodedNull() {
 String es1 = null;
 String es2 = "SoundexTest";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedEmpty() {
 String es1 = "";
 String es2 = "SoundexTest";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedSame() {
 String es1 = "P5231";
 String es2 = "P5231";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedDifferent() {
 String es1 = "P5231";
 String es2 = "Q1356";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedRefined() {
 String es1 = "P52310";
 String es2 = "K13562";
 int expected = 2;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceEncodedException() {
 Soundex soundex = new Soundex();
 String s1 = " apple";
 String s2 = "banana";
 String es1 = null;
 String es2 = null;
 try {
 es1 = soundex.encode(s1);
 es2 = soundex.encode(s2);
 } catch (EncoderException e) {
 //expected
 }
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

}