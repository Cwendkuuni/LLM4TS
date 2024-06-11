import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

 @Test
 public void testClean() {
 String input = "A bc dEf 123!";
 String expected = "ABC";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCleanNull() {
 String input = null;
 String result = SoundexUtils.clean(input);
 fail("Should have thrown an IllegalArgumentException for null input");
 }

 @Test
 public void testCleanEmpty() {
 String input = "";
 String expected = "";
 String result = SoundexUtils.clean(input);
 assertEquals(expected, result);
 }

 @Test
 public void testDifferenceNull() throws EncoderException {
 String s1 = null;
 String s2 = "test";
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 assertEquals(0, difference);
 }

 @Test(expected = NullPointerException.class)
 public void testDifferenceNullEncoder() throws EncoderException {
 String s1 = "test";
 String s2 = "test2";
 int difference = SoundexUtils.difference(null, s1, s2);
 fail("Should have thrown a NullPointerException for null encoder");
 }

 @Test
 public void testDifferenceEmpty() throws EncoderException {
 String s1 = "";
 String s2 = "";
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 assertEquals(4, difference);
 }

 @Test
 public void testDifferenceSoundex() throws EncoderException {
 String s1 = "Apple";
 String s2 = "Orange";
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 assertEquals(1, difference);
 }

 @Test
 public void testDifferenceRefinedSoundex() throws EncoderException {
 String s1 = "Apple";
 String s2 = "Orang Utan";
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 assertEquals(3, difference);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceNullString() throws EncoderException {
 String s1 = null;
 String s2 = "test";
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 fail("Should have thrown an IllegalArgumentException for null string s1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceNullString2() throws EncoderException {
 String s1 = "test";
 String s2 = null;
 int difference = SoundexUtils.difference(SoundexEncoder.INSTANCE, s1, s2);
 fail("Should have thrown an IllegalArgumentException for null string s2");
 }

 @Test
 public void testEncode() throws EncoderException {
 String input = "Apple";
 String expected = "K320";
 String result = SoundexUtils.encode(SoundexEncoder.INSTANCE, input);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeException() throws EncoderException {
 String input = "Apple";
 SoundexEncoder encoder = new SoundexEncoder() {
 @Override
 public String encode(CharSequence cs) {
 throw new EncoderException("Test Exception");
 }
 };
 String result = SoundexUtils.encode(encoder, input);
 fail("Should have thrown an EncoderException");
 }

 @Test
 public void testEncodeEmpty() throws EncoderException {
 String input = "";
 String expected = "";
 String result = SoundexUtils.encode(SoundexEncoder.INSTANCE, input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeNull() throws EncoderException {
 String input = null;
 String expected = "";
 String result = SoundexUtils.encode(SoundexEncoder.INSTANCE, input);
 assertEquals(expected, result);
 }

}