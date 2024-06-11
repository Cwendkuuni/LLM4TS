import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

 private final Soundex soundex = new Soundex();

 @Test
 public void testEncodeString() throws EncoderException {
 String input = "apple";
 String expected = "K111";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expected = "";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeNullString() {
 String input = null;
 String expected = null;
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() {
 Object input = 123;
 String expected = null;
 String output = soundex.encode(input);
 assertNull(output);
 }

 @Test
 public void testEncodeStringWithMultipleWAndH() {
 String input = "waterwhale";
 String expected = "W220";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithMultipleConsonantsInASingleGroup() {
 String input = "bubble";
 String expected = "B222";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithMultipleGroups() {
 String input = "moonman";
 String expected = "M110N110";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithSpecialCharacters() {
 String input = "àäéèëïìïôöùü";
 String expected = "A200E111I111O111U111";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test
 public void testEncodeStringWithDigits() {
 String input = "12345";
 String expected = "N111";
 String output = soundex.encode(input);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapUnmappedCharacter() {
 char ch = 'Z';
 soundex.map(ch);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMapNegativeCharacter() {
 char ch = '\u00AD';
 soundex.map(ch);
 }
}