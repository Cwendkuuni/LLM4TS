import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEncodeEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeNull() {
 String input = null;
 String expectedOutput = "1111111111";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeSimpleString() {
 String input = "hello";
 String expectedOutput = "h2h2l2l2o2o";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeComplexString() {
 String input = "CaverphoneTestCaverphoneTestCaverphoneTest";
 String expectedOutput = "Ca2eph3on3TestCa2eph3on3TestCa2eph3on3Test";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeWithSpecialCharacters() {
 String input = "Caverphone!Test@Caverphone#";
 String expectedOutput = "Ca2eph3on3!Test@Caverphone#";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testEncodeWithNumbers() {
 String input = "1234Caverphone5678Test9012";
 String expectedOutput = "1234Ca2eph3on35678Test9012";
 try {
 String output = (String) caverphone.encode(input);
 assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // This should not happen as we are testing the encode method directly
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString() {
 int input = 123;
 String expectedOutput = null;
 try {
 String output = (String) caverphone.encode(input);
 } catch (EncoderException e) {
 assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
 expectedOutput = e.getMessage();
 } catch (AssertionError ae) {
 throw new AssertionError("Unexpected exception: " + ae.getMessage() + " instead of expected EncoderException: " + expectedOutput);
 }
 }

 @Test
 public void testIsCaverphoneEqualEmpty() {
 String str1 = "";
 String str2 = "";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualNull() {
 String str1 = null;
 String str2 = null;
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualSameString() {
 String str1 = "hello";
 String str2 = "hello";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualDifferentString() {
 String str1 = "hello";
 String str2 = "world";
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualWithSpecialCharacters() {
 String str1 = "Caverphone!Test@";
 String str2 = "Caverphone!Test@";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualWithNumbers() {
 String str1 = "1234Caverphone5678Test9012";
 String str2 = "1234Caverphone5678Test9012";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualDifferentLength() {
 String str1 = "hello";
 String str2 = "world";
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }
}