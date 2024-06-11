import org.apache.commons.codec.language.SoundexUtils;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A an Apple aDay keeps the Doctor away";
 String expected = "A A A A A A A A A A A";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 String input = null;
 SoundexUtils.clean(input);
 }

 @Test
 public void testCleanEmptyInput() {
 String input = "";
 String expected = "";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifference() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
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

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceNullInput1() {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = null;
 String s2 = "Apple";
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceNullInput2() {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = "Apple";
 String s2 = null;
 SoundexUtils.difference(encoder, s1, s2);
 }

 @Test
 public void testDifferenceEmptyInput1() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = "";
 String s2 = "Apple";
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEmptyInput2() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
 String s1 = "Apple";
 String s2 = "";
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncoded() {
 String es1 = "A123";
 String es2 = "A123";
 int expected = 4;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedDifferentLength() {
 String es1 = "A1B2C3";
 String es2 = "A1B2";
 int expected = 3;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedNullInput1() {
 String es1 = null;
 String es2 = "A1B2C3";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceEncodedNullInput2() {
 String es1 = "A1B2C3";
 String es2 = null;
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 assertEquals(expected, result);
 }
}