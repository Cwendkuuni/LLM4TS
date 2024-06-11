import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

 private final Metaphone metaphone = new Metaphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 // Empty string is a valid input, so no exception should be thrown
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSingleCharacter() {
 char input = 'A';
 String expectedOutput = "A";
 try {
 String output = metaphone.encode(Character.toString(input));
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSimpleWord() {
 String input = "apple";
 String expectedOutput = "APEL";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testComplexWord() {
 String input = "indistinguishable";
 String expectedOutput = "I_ND_ST_G_BL";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testMultipleWords() {
 String input = "the quick brown fox jumps over the lazy dog";
 String expectedOutput = "T_H_K_B_R_O_W_N_F_O_X_J_M_P_S_V_G_D";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInput() {
 String input = "1234abcd";
 try {
 metaphone.encode(input);
 Assert.fail("Expected an EncoderException, but none was thrown");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test(expected = DecoderException.class)
 public void testInvalidOutput() {
 String input = "apple";
 String invalidOutput = "APPLE";
 try {
 Metaphone decoder = new Metaphone();
 String output = decoder.decode(invalidOutput);
 Assert.fail("Expected a DecoderException, but none was thrown");
 } catch (DecoderException e) {
 // Expected
 }
 }

 @Test
 public void testSetMaxCodeLen() {
 Metaphone metaphone = new Metaphone();
 int maxCodeLen = 6;
 metaphone.setMaxCodeLen(maxCodeLen);
 String input = "indistinguishable";
 String expectedOutput = "I_ND_ST_G_BL_SH";
 try {
 String output = metaphone.encode(input);
 Assert.assertEquals(expectedOutput, output);
 } catch (EncoderException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

}