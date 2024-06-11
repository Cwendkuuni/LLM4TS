import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private Soundex soundex = Soundex.US_ENGLISH;

 @Test
 public void testEncodeString() {
 String input = "apple";
 String expected = "K133";
 try {
 String result = soundex.encode(input);
 Assert.assertEquals(expected, result);
 } catch (EncoderException e) {
 e.printStackTrace();
 FailureException.fail("Unexpected EncoderException: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeNullString() {
 String input = null;
 try {
 String result = soundex.encode(input);
 Assert.assertNull(result);
 } catch (EncoderException e) {
 // Expected since input is null
 }
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expected = "";
 String result = soundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testEncodeSingleCharacter() {
 String input = "A";
 String expected = "A000";
 String result = soundex.encode(input);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeNonAlphabetCharacter() {
 String input = "1";
 String result = "";
 try {
 result = soundex.encode(input);
 } catch (EncoderException e) {
 // Expected since input is not a string
 }
 Assert.assertEquals("", result);
 FailureException.fail("Unexpected exception: " + e.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEncodeUpperCaseCharacter() {
 String input = "Z";
 String result = soundex.encode(input.toLowerCase());
 Assert.assertEquals("Z000", result);
 }

 @Test
 public void testDifference() {
 String str1 = "Apple";
 String str2 = "Apple";
 int expected = 4;
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 e.printStackTrace();
 FailureException.fail("Unexpected EncoderException: " + e.getMessage());
 }
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifferenceDifferentLength() {
 String str1 = "Apple";
 String str2 = "Banana";
 int expected = 0;
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 e.printStackTrace();
 FailureException.fail("Unexpected EncoderException: " + e.getMessage());
 }
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifferenceCaseInsensitive() {
 String str1 = "Apple";
 String str2 = "apple";
 int expected = 4;
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 e.printStackTrace();
 FailureException.fail("Unexpected EncoderException: " + e.getMessage());
 }
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifferenceWithDuplicates() {
 String str1 = "Mississippi";
 String str2 = "Mississippie";
 int expected = 3;
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 e.printStackTrace();
 FailureException.fail("Unexpected EncoderException: " + e.getMessage());
 }
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceNullString1() {
 String str1 = null;
 String str2 = "Apple";
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 // Expected since str1 is null
 }
 // Expected since result should be 0, but JUnit complaining about unexpected null
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceNullString2() {
 String str1 = "Apple";
 String str2 = null;
 int result;
 try {
 result = soundex.difference(str1, str2);
 } catch (EncoderException e) {
 // Expected since str2 is null
 }
 // Expected since result should be 0, but JUnit complaining about unexpected null
 }
}