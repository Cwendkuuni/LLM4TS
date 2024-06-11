import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone encoder = new Caverphone();

 @Test
 public void testEmptyString() {
 String encoded = encoder.encode("");
 assertEquals("1111111111", encoded);
 }

 @Test
 public void testNullInput() {
 try {
 encoder.encode(null);
 fail("Should have thrown an exception");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testSimpleLowerCaseInput() {
 String encoded = encoder.encode("apple");
 assertEquals("A2pl3e", encoded);
 }

 @Test
 public void testSimpleUpperCaseInput() {
 String encoded = encoder.encode("APPLE");
 assertEquals("A2pl3e", encoded);
 }

 @Test
 public void testInputWithDigits() {
 String encoded = encoder.encode("apple123");
 assertEquals("A2pl3e11111", encoded);
 }

 @Test
 public void testInputWithSpecialCharacters() {
 String encoded = encoder.encode("apple#!");
 String expected = "A2pl3e!#!11111";
 assertEquals(expected, encoded);
 }

 @Test
 public void testInputWithStartOptions() {
 String encoded = encoder.encode("CoughPotato");
 assertEquals("Cou2fPot3to", encoded);
 String encoded2 = encoder.encode("RoughPotato");
 assertEquals("Rou2fPot3to", encoded2);
 String encoded3 = encoder.encode("ToughPotato");
 assertEquals("Tou2fPot3to", encoded3);
 String encoded4 = encoder.encode("EnoughPotato");
 assertEquals("Enou2fPot3to", encoded4);
 String encoded5 = encoder.encode("TroughPotato");
 assertEquals("Trou2fPot3to", encoded5); // Note the spec says "enough" here instead of "trough"
 }

 @Test
 public void testInputWithReplacements() {
 String encoded = encoder.encode("Cucumber");
 assertEquals("2Cuk3mb3r", encoded);
 String encoded2 = encoder.encode("Ciucumber");
 assertEquals("Siu2Cmb3r", encoded2);
 String encoded3 = encoder.encode("Ceucumber");
 assertEquals("Seiu2Cmb3r", encoded3);
 String encoded4 = encoder.encode("Cyucumber");
 assertEquals("Syiu2Cmb3r", encoded4);
 String encoded5 = encoder.encode("Tchucumber");
 assertEquals("2Chuc2mber", encoded5);
 String encoded6 = encoder.encode("Cucumber");
 assertEquals("2Cuk3mb3r", encoded6);
 String encoded7 = encoder.encode("C");
 assertEquals("K", encoded7);
 String encoded8 = encoder.encode("Q");
 assertEquals("K", encoded8);
 String encoded9 = encoder.encode("X");
 assertEquals("K", encoded9);
 String encoded10 = encoder.encode("V");
 assertEquals("F", encoded10);
 String encoded11 = encoder.encode("Dg");
 assertEquals("2g", encoded11);
 String encoded12 = encoder.encode("Tio");
 assertEquals("Sio", encoded12);
 String encoded13 = encoder.encode("Tia");
 assertEquals("Sia", encoded13);
 String encoded14 = encoder.encode("D");
 assertEquals("t", encoded14);
 String encoded15 = encoder.encode("Ph");
 assertEquals("fh", encoded15);
 String encoded16 = encoder.encode("B");
 assertEquals("p", encoded16);
 String encoded17 = encoder.encode("Sh");
 assertEquals("s2", encoded17);
 String encoded18 = encoder.encode("Z");
 assertEquals("s", encoded18);
 String encoded19 = encoder.encode("Wh3");
 assertEquals("Wh3", encoded19);
 String encoded20 = encoder.encode("W");
 assertEquals("2", encoded20);
 String encoded21 = encoder.encode("wy");
 // fail("1.0 only"); // Uncomment to fail test
 String encoded22 = encoder.encode("Why");
 // fail("1.0 only"); // Uncomment to fail test
 String encoded23 = encoder.encode("wh3");
 assertEquals("Wh3", encoded23);
 String encoded24 = encoder.encode("w");
 assertEquals("2", encoded24);
 String encoded25 = encoder.encode("Y3");
 assertEquals("Y3", encoded25);
 String encoded26 = encoder.encode("y3");
 assertEquals("3kh3", encoded26);
 String encoded27 = encoder.encode("Gh");
 assertEquals("22", encoded27);
 String encoded28 = encoder.encode("G");
 assertEquals("k", encoded28);
 String encoded29 = encoder.encode("S+");
 assertEquals("S", encoded29);
 String encoded30 = encoder.encode("T+");
 assertEquals("T", encoded30);
 String encoded31 = encoder.encode("P+");
 assertEquals("P", encoded31);
 String encoded32 = encoder.encode("K+");
 assertEquals("K", encoded32);
 String encoded33 = encoder.encode("F+");
 assertEquals("F", encoded33);
 String encoded34 = encoder.encode("M+");
 assertEquals("M", encoded34);
 String encoded35 = encoder.encode("N+");
 assertEquals("N", encoded35);
 String encoded36 = encoder.encode("W3");
 assertEquals("W3", encoded36);
 // String encoded37 = encoder.encode("Wy"); // 1.0 only
 // fail("1.0 only"); // Uncomment to fail test
 String encoded38 = encoder.encode("Wh3");
 assertEquals("Wh3", encoded38);
 String encoded39 = encoder.encode("W");
 assertEquals("3", encoded39);
 String encoded40 = encoder.encode("R3");
 assertEquals("R3", encoded40);
 String encoded41 = encoder.encode("r3");
 assertEquals("r", encoded41);
 String encoded42 = encoder.encode("R$");
 assertEquals("3", encoded42); // 2.0 only
 // String encoded43 = encoder.encode("Ry"); // 1.0 only
 // fail("1.0 only"); // Uncomment to fail test
 String encoded44 = encoder.encode("r");
 assertEquals("2", encoded44);
 String encoded45 = encoder.encode("L3");
 assertEquals("L3", encoded45);
 String encoded46 = encoder.encode("l3");
 assertEquals("l", encoded46);
 String encoded47 = encoder.encode("L$");
 assertEquals("3", encoded47); // 2.0 only
 // String encoded48 = encoder.encode("Ly"); // 1.0 only
 // fail("1.0 only"); // Uncomment to fail test
 String encoded49 = encoder.encode("l");
 assertEquals("2", encoded49);
 String encoded50 = encoder.encode("J");
 assertEquals("y", encoded50); // 2.0 only
 String encoded51 = encoder.encode("Y3");
 assertEquals("Y3", encoded51); // 2.0 only
 String encoded52 = encoder.encode("Y");
 assertEquals("3", encoded52); // 2.0 only
 String encoded53 = encoder.encode("y");
 assertEquals("3", encoded53); // 2.0 only
 }

 @Test
 public void testInputWithRemovals() {
 String encoded = encoder.encode("The quick brown fox jumps over the lazy dog");
 assertEquals("T3h3 q1ck br3wn f3x j3mp3s 0v3r th3 l3zy d3g", encoded);
 }

 @Test
 public void testInputWithHandlingOfFinalE() {
 String encoded = encoder.encode("EnoughSaid");
 assertEquals("Enou2fSaid", encoded); // 2.0 only
 }

 @Test
 public void testInputWithHandlingOfStartOptionsAndReplacements() {
 String encoded = encoder.encode("EnoughSaidCoughPotato");
 assertEquals("Enou2fSaidCou2fPot3to", encoded);
 }

 @Test
 public void testInputWithHandlingOfStartOptionsAndReplacementsAndRemovalOfFinalE() {
 String encoded = encoder.encode("EnoughSaidCoughPotatoEnough");
 assertEquals("Enou2fSaidCou2fPot3toEnou2f", encoded); // 2.0 only
 }

 @Test
 public void testInputWithHandlingOfStartOptionsAndReplacementsAndRemovals() {
 String encoded = encoder.encode("EnoughSaidCoughPotatoEnoughSaid");
 assertEquals("Enou2fSaidCou2fPot3toEnou2fSaid", encoded); // 2.0 only
 }

}