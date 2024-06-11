import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeString() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "he2l2o2";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(123);
 }

 @Test
 public void testCaseSensitive() {
 String input = "Hello";
 String result = caverphone.caverphone(input);
 String expected = "he2L2o2";
 assertEquals(expected, result);
 }

 @Test
 public void testEdgeCases() {
 String input = "CaverphoneAlgorithm";
 String result = caverphone.caverphone(input);
 String expected = "Ca2v3p3h3o3n3A2l3g3o2r3t3h3A2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesReplacements() {
 String input = "CoughCiCeCyTchCcQqCiCeCyTchC";
 String result = caverphone.caverphone(input);
 String expected = "cou2fsi2sych2ch2k2q2ksi2sych2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesRemovals() {
 String input = "Three1Two1Five1Six1Seven1";
 String result = caverphone.caverphone(input);
 String expected = "Th3e3F5v3S6v3";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesDigits() {
 String input = "One2Two3Four5Six7Seven8Nine";
 String result = caverphone.caverphone(input);
 String expected = "On3t3w0w3F4u3r5x6v3S7v3N9";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesStartOptions() {
 String input = "CoughCoughCoughCoughCoughCough";
 String result = caverphone.caverphone(input);
 String expected = "cou2fcou2fcou2fcou2fcou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesDoubleLetters() {
 String input = "BitterSweet";
 String result = caverphone.caverphone(input);
 String expected = "Bi2t2Sw3t2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesConsecutiveVowels() {
 String input = "Europe";
 String result = caverphone.caverphone(input);
 String expected = "Eu2r3p3v3A2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesConsecutiveConsonants() {
 String input = "Pepper";
 String result = caverphone.caverphone(input);
 String expected = "Pe2p3r";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesMultipleVowelsAndConsonants() {
 String input = "PepperyEurope";
 String result = caverphone.caverphone(input);
 String expected = "Pe2p3r2yEu2r3p3v3A2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithStartOptions() {
 String input = "CoughCoughCoughCoughCoughCoughEnough";
 String result = caverphone.caverphone(input);
 String expected = "cou2fcou2fcou2fcou2fcou2feno2uf";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithReplacements() { 
 String input = "CoughCiCeCyTchCcQqCiCeCyTchCQqCiCeCyTchC";
 String result = caverphone.caverphone(input);
 String expected = "cou2fsi2sych2ch2k2q2ksi2sych2q2ksi2sych2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithDoubleLetters() {
 String input = "BitterSweetBitterSweet";
 String result = caverphone.caverphone(input);
 String expected = "Bi2t2Sw3t2Bi2t2Sw3t2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithDigits() {
 String input = "One2Two3Four5Six1Seven8Nine";
 String result = caverphone.caverphone(input);
 String expected = "On3t3w0w3F4u3r5x6v3S7v3N9";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithConsecutiveVowelsAndConsonants() {
 String input = "EuropePepper";
 String result = caverphone.caverphone(input);
 String expected = "Eu2r3p3v3A2Pe2p3r";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithMultipleConsecutiveVowelsAndConsonants() {
 String input = "EuropePepperyEurope";
 String result = caverphone.caverphone(input);
 String expected = "Eu2r3p3v3A2Pe2p3r2yEu2r3p3v3A2";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithConsecutiveDigits() {
 String input = "One2Two3Four5Six1Seven8Nine";
 String result = caverphone.caverphone(input);
 String expected = "On3t3w0w3F4u3r5x6v3S7v3N9";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithConsecutiveDigitsAtStart() {
 String input = "1Two3Four5Six7Seven8Nine";
 String result = caverphone.caverphone(input);
 String expected = "1T2w3F4u3r5x6v3S7v3N9";
 assertEquals(expected, result);
 }

 @Test
 public void testHandlesEdgeCasesWithConsecutiveDigitsAtEnd() {
 String input = "One2Two3Four5Six1Seven8Nine1";
 String result = caverphone.caverphone(input);
 String expected = "On3t3w0w3F4u3r5x6v3S7v3N91";
 assertEquals(expected, result);
 }
}