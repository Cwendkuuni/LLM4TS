import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.Language.Soundex;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 @Test
 public void testEncode() {
 Soundex soundex = new Soundex();
 String input = "Apple, Banana, Cat, Dog, Elephant, Fish, Iguana, Jackal, Kangaroo";
 String[] expected = {"A133", "A261", "C321", "D223", "E111", "F351", "I131", "J151", "K112"};
 for (int i = 0; i < input.split(", ").length; i++) {
 String testString = input.split(", ")[i];
 String result = soundex.encode(testString);
 assertEquals(expected[i], result);
 }
 }

 @Test(expected = DecoderException.class)
 public void testInvalidInput() {
 Soundex soundex = new Soundex();
 soundex.encode(null);
 }

 @Test
 public void testDifference() {
 Soundex soundex = new Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int difference = soundex.difference(str1, str2);
 assertEquals(0, difference);
 str2 = "Apple";
 difference = soundex.difference(str1, str2);
 assertEquals(4, difference);
 }

 @Test
 public void testGetMappingCode() {
 Soundex soundex = new Soundex();
 String testString = "Apple, Banana, Cat, Dog, Elephant, Fish, Iguana, Jackal, Kangaroo";
 char last = '0';
 for (int i = 0; i < testString.length(); i++) {
 char mapped = soundex.getMappingCode(testString, i);
 if (mapped != 0) {
 if (mapped != last) {
 System.out.print(mapped);
 last = mapped;
 }
 }
 }
 System.out.println();
 assertEquals("A133", soundex.soundex(testString));
 }
}