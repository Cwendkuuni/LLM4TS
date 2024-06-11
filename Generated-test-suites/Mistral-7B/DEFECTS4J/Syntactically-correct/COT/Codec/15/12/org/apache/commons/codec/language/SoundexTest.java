import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

 private Soundex soundexInstance;

 @Test
 public void testDefaultConstructor() {
 soundexInstance = new Soundex();
 }

 @Test
 public void testUS_ENGLISH_MAPPING() {
 soundexInstance = new Soundex();
 char[] mapping = soundexInstance.getSoundexMapping();
 assertArrayEquals("US_ENGLISH_MAPPING".toCharArray(), mapping);
 }

 @Test
 public void testEncodeString() {
 soundexInstance = new Soundex();
 String str = "Apple";
 String expected = "K220";
 String actual = soundexInstance.encode(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
 soundexInstance = new Soundex();
 Object obj = "Orange";
 String expected = "Q111";
 String actual = (String) soundexInstance.encode(obj);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() throws EncoderException {
 soundexInstance = new Soundex();
 Object obj = 42;
 soundexInstance.encode(obj);
 }

 @Test
 public void testDifference() throws EncoderException {
 soundexInstance = new Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 int expected = 2;
 int actual = soundexInstance.difference(str1, str2);
 assertEquals(expected, actual);
 }

 @Test
 public void testClean() {
 soundexInstance = new Soundex();
 String str = "App1e";
 String expected = "Apple";
 String actual = soundexInstance.clean(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_SingleCharacter() {
 soundexInstance = new Soundex();
 String str = "A";
 char expected = '1';
 char actual = soundexInstance.getMappingCode(str, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_SameCode() {
 soundexInstance = new Soundex();
 String str = "Apple";
 char expected = 'P';
 char actual = soundexInstance.getMappingCode(str, 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_DifferentCode() {
 soundexInstance = new Soundex();
 String str = "Apple";
 char expected = 'L';
 char actual = soundexInstance.getMappingCode(str, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'S';
 char actual = soundexInstance.getMappingCode(str, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_FirstCharacter() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'A';
 char actual = soundexInstance.getMappingCode(str, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_LastCharacter() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'N';
 char actual = soundexInstance.getMappingCode(str, str.length() - 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleInBetween() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'P';
 char actual = soundexInstance.getMappingCode(str, 4);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleAtBeginning() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'S';
 char actual = soundexInstance.getMappingCode(str, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleAtEnd() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'P';
 char actual = soundexInstance.getMappingCode(str, str.length() - 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleInBetweenAndBeginning() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'S';
 char actual = soundexInstance.getMappingCode(str, 1);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleInBetweenAndEnd() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'P';
 char actual = soundexInstance.getMappingCode(str, str.length() - 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_HWRule_MultipleInBetweenBeginningAndEnd() {
 soundexInstance = new Soundex();
 String str = "Application";
 char expected = 'S';
 char actual = soundexInstance.getMappingCode(str, 2);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetMappingCode_MultipleCharacters_DifferentCodes() {
 soundexInstance = new Soundex();
 String str1 = "Apple";
 String str2 = "Orange";
 char expected1 = 'P';
 char expected2 = 'N';
 char actual1 = soundexInstance.getMappingCode(str1, 1);
 char actual2 = soundexInstance.getMappingCode(str2, 1);
 assertEquals(expected1, actual1);
 assertEquals(expected2, actual2);
 assertNotEquals(expected1, expected2);
 }

 @Test
 public void testSoundex() {
 soundexInstance = new Soundex();
 String str = "Apple";
 String expected = "P220";
 String actual = soundexInstance.soundex(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testSoundex_CustomMapping() {
 soundexInstance = new Soundex("D01210123010202".toCharArray());
 String str = "Apple";
 String expected = "P221";
 String actual = soundexInstance.soundex(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testSoundex_EmptyString() {
 soundexInstance = new Soundex();
 String str = "";
 String expected = "";
 String actual = soundexInstance.soundex(str);
 assertEquals(expected, actual);
 }

 @Test
 public void testSoundex_NullString() {
 soundexInstance = new Soundex();
 String str = null;
 String expected = null;
 String actual = soundexInstance.soundex(str);
 assertNull(actual);
 }

}