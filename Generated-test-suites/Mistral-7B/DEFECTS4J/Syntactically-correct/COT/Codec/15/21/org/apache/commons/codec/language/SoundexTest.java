import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

 private Soundex soundex;

 @Before
 public void setUp() {
 soundex = new Soundex();
 }

 @Test
 public void testUSEnglishMapping() {
 String input = "The quick brown fox jumps over the lazy dog";
 String expected = "T3251";
 String result = soundex.soundex(input);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testDifference() throws EncoderException {
 String str1 = "Smith";
 String str2 = "Smithe";
 int difference = soundex.difference(str1, str2);
 Assert.assertEquals(3, difference);
 }

 @Test
 public void testEncodeString() {
 String str = "Test";
 String expected = "T123";
 String result = soundex.encode(str);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonStringObject() {
 Object obj = 42;
 soundex.encode(obj);
 }

 @Test
 public void testGetMappingCodeWithHWRule() {
 String str = "The quick brown fox jumps over the lazy dog";
 char mappingCode = soundex.getMappingCode(str, 3);
 Assert.assertEquals('2', mappingCode);
 }

 @Test
 public void testGetMappingCodeWithoutHWRule() {
 String str = "The quick brown fox jumps over the lazy dog";
 char mappingCode = soundex.getMappingCode(str, 5);
 Assert.assertNotEquals('0', mappingCode);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMappingCodeWithUnmappedCharacter() {
 String str = "The quick brown xFox jumps over the lazy dog";
 soundex.getMappingCode(str, 0);
 }

}