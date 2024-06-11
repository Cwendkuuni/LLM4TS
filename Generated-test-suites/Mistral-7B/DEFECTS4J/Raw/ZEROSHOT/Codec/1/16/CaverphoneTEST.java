import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
     String encoded = caverphone.caverphone("");
     assertEquals("1111111111", encoded);
 }

 @Test
 public void testNullString() {
     try {
         caverphone.caverphone(null);
         fail("NullPointerException expected");
     } catch (NullPointerException e) {
         // Expected
     }
 }

 @Test
 public void testLowercaseString() {
     String input = "HelloWorld";
     String expected = "H2oW2oRl";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testRemoveNonAlphabets() {
     String input = "Hello1World!";
     String expected = "H2oW2oRl";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testRemoveFinalE() {
     String input = "enough";
     String expected = "enou2f";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testCoughHandling() {
     String input = "coughed";
     String expected = "cou2fed";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testRoughHandling() {
     String input = "roughly";
     String expected = "rou2fouly";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testToughHandling() {
     String input = "toughly";
     String expected = "tou2fouly";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testMultipleHandlings() {
     String input = "enough toughtoughly coughed";
     String expected = "enou2f tou2fouly cou2fed";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testJHandling() {
     String input = "java";
     String expected = "j3";
     String encoded = caverphone.caverphone(input);
     assertEquals(expected, encoded);
 }

 @Test
 public void testEncodeObject() {
     String input = "HelloWorld";
     String encoded = (String) caverphone.encode(input);
     String expected = "H2oW2oRl";
     assertEquals(expected, encoded);
 }

 @Test
 public void testIsCaverphoneEqual() {
     String str1 = "HelloWorld";
     String str2 = "HelloWorld";
     boolean expected = true;
     boolean result = caverphone.isCaverphoneEqual(str1, str2);
     assertEquals(expected, result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
     String str1 = "HelloWorld";
     String str2 = "HelloWorld1";
     boolean expected = false;
     boolean result = caverphone.isCaverphoneEqual(str1, str2);
     assertEquals(expected, result);
 }

}