import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Soundex class
 */
public class SoundexTest {

 private Soundex soundex;

 @BeforeClass
 public static void setupBeforeClass() {
 soundex = Soundex.US_ENGLISH; // using default US_ENGLISH instance
 }

 @Test
 public void testUSEnglishMapping() {
 char[] usEnglishMapping = soundex.getSoundexMapping();
 Assert.assertEquals("01230120022455012623010202", new String(usEnglishMapping));
 }

 @Test
 public void testEncodeString() throws EncoderException {
 String str1 = "apple";
 String expected = "K122";
 String result = soundex.encode(str1);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
 Object obj1 = "banana";
 String expected = "B120";
 String result = (String) soundex.encode(obj1);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() throws EncoderException {
 Object obj1 = 123;
 String result = (String) soundex.encode(obj1);
 Assert.fail("EncoderException should be thrown as the object is not of type String.");
 }

 @Test
 public void testDifference() throws EncoderException {
 String str1 = "apple";
 String str2 = "appel";
 int difference = soundex.difference(str1, str2);
 Assert.assertEquals(3, difference); // 3 different characters
 }

 @Test
 public void testEncodeEmptyString() {
 String str = "";
 String result = soundex.encode(str);
 Assert.assertEquals(str, result);
 }

 @Test
 public void testEncodeSingleCharacterString() {
 String str = "A";
 String result = soundex.encode(str);
 Assert.assertEquals("A000", result);
 }

 @Test
 public void testEncodeWhString() throws EncoderException {
 String str = "what";
 String result = soundex.encode(str);
 String expected = "W000";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDigits() throws EncoderException {
 String str = "number";
 String result = soundex.encode(str);
 String expected = "N260";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDoubleLetters() throws EncoderException {
 String str = "string";
 String result = soundex.encode(str);
 String expected = "S251";
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDifferentCases() throws EncoderException {
 String str = "Apple";
 String expected = "K122";
 String result = soundex.encode(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithDiacriticalMarks() throws EncoderException {
 String str = "öÄü";
 String expected = "O110";
 String result = soundex.encode(str);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeStringWithMultipleEncodingMappings() throws EncoderException {
 Soundex soundexFrench = new Soundex(new String("12345678901234").toCharArray());
 String str1 = "école";
 String expected = "S152";
 String result = soundexFrench.encode(str1);
 Assert.assertEquals(expected, result);
 }

}