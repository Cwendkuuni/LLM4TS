import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneConstructor() {
 // Test the constructor
 Caverphone caverphoneInstance = new Caverphone();
 Assert.assertNotNull(caverphoneInstance);
 }

 @Test
 public void testCaverphoneNullInput() {
 // Test caverphone method with null input
 String input = null;
 String expectedOutput = "1111111111";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 // Test caverphone method with empty input
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneLowerCase() {
 // Test handling of lowercase input
 String input = "helloWorld";
 String expectedOutput = "h2e2l2o2w2o2r2d";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabeticalCharacters() {
 // Test handling of input with non-alphabetical characters
 String input = "Hello1World!";
 String expectedOutput = "h2e2l2o2r2d";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 // Test handling of input with final 'e'
 String input = "HelloWorlde";
 String expectedOutput = "h2e2l2o2w2o2r2d";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneHandleStartOptions() {
 // Test handling of input with certain start options
 String input = "CoughPotato";
 String expectedOutput = "cou2fp2o2t2a2t2o2t2o";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneHandleReplacements() {
 // Test handling of input with certain replacements
 String input = "CuplleQuiCe";
 String expectedOutput = "c2u2p2l2l2p2p2l2l2e";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneHandleGh() {
 // Test handling of 'gh' replacement
 String input = "Bighouse";
 String expectedOutput = "b2i2gh2o2u2s2e";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneHandleMultipleReplacements() {
 // Test handling of input with multiple replacements
 String input = "CeasarCypher";
 String expectedOutput = "c2e2a2s2a2r2c2y2p2h2e2r";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneHandleRemovals() {
 // Test handling of input with certain removals
 String input = "OneTwoThree";
 String expectedOutput = "123";
 String output = caverphone.caverphone(input);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneInvalidInput() {
 // Test handling of invalid input (non-String)
 Object invalidInput = 123;
 caverphone.encode(invalidInput);
 }

 @Test
 public void testCaverphoneIdenticalStrings() {
 // Test isCaverphoneEqual method with identical strings
 String str1 = "abcd";
 String str2 = "abcd";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertEquals(expectedOutput, output);
 }

 @Test
 public void testCaverphoneNonIdenticalStrings() {
 // Test isCaverphoneEqual method with non-identical strings
 String str1 = "abcd";
 String str2 = "efgh";
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertEquals(expectedOutput, output);
 }

}