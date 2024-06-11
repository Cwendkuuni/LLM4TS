import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 private Caverphone caverphoneInstance;

 @Before
 public void setUp() {
 caverphoneInstance = new Caverphone();
 }

 // Test method for caverphone(String txt)
 @Test
 public void testCaverphone() {
 String input = "ThisIsATestString";
 String expectedOutput = "T2h1sT1iS1tA1tS2i1g1sT1r1n1g";
 String output = caverphoneInstance.caverphone(input);
 assertTrue(expectedOutput.equals(output));
 }

 // Test method for encode(Object pObject)
 @Test(expected = EncoderException.class)
 public void testEncodeInvalidInput() {
 caverphoneInstance.encode(new Integer(10));
 }

 @Test
 public void testEncodeString() {
 String input = "Hello World";
 String expectedOutput = "H2e2l2o2W2o1r2l2d";
 String output = caverphoneInstance.encode(input);
 assertEquals(expectedOutput, output);
 }

 // Test method for isCaverphoneEqual(String str1, String str2)
 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "ThisIsATestString";
 String str2 = "ThisIsAnotherTestString";
 String str3 = "ThisIsATestString";
 assertFalse(caverphoneInstance.isCaverphoneEqual(str1, str2));
 assertTrue(caverphoneInstance.isCaverphoneEqual(str1, str3));
 }

 // Edge cases and exception handling test cases

 // Test for null input
 @Test(expected = EncoderException.class)
 public void testEncodeNullInput() {
 caverphoneInstance.encode(null);
 }

 // Test for empty string input
 @Test
 public void testEncodeEmptyStringInput() {
 String output = caverphoneInstance.caverphone("");
 assertEquals("1111111111", output);
 }

 // Test for input containing only lowercase letters
 @Test
 public void testCaverphoneLowerCase() {
 String input = "thisisteststring";
 String expectedOutput = "t2h1s1t1i1s1t1a1t1s2i1g1s1t1r1n1g";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 // Test for input containing only uppercase letters
 @Test
 public void testCaverphoneUpperCase() {
 String input = "THISISTESTSTRING";
 String expectedOutput = "T2h1s1t1i1s1t1a1t1s2i1g1s1t1r1n1g";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 // Test for input containing digits
 @Test
 public void testCaverphoneDigits() {
 String input = "ThisIs123TestString";
 String expectedOutput = "T2h1s1t1i1s1t1a1t1s2i1g1s1t1r1n1g111";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 // Test for input containing special characters
 @Test
 public void testCaverphoneSpecialCharacters() {
 String input = "ThisIs@TestString!";
 String expectedOutput = "T2h1s1t1i1s1t1a1t1s2i1g1s1t1r1n1g!";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

}