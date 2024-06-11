import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String expected = "1111111111";
 String actual = caverphone.caverphone(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEmpty() {
 String expected = "1111111111";
 String actual = caverphone.caverphone("");
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "thisIsATest";
 String expected = "T3h1iS2iA2T3e2s";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneRemoveNonLetters() {
 String input = "123thisIsATest456";
 String expected = "T3h1iS2iA2T3e2s";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "thisIsATestE";
 String expected = "T3h1iS2iA2T3e";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandleStartOptions() {
 String input = "coughenough";
 String expected = "cou2fA";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tough";
 expected = "tou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "enough";
 expected = "enou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "trough";
 expected = "trou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "gn";
 expected = "2n";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandleReplacements() {
 String input = "cq";
 String expected = "2q";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ci";
 expected = "si";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ce";
 expected = "se";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "cy";
 expected = "sy";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tch";
 expected = "2ch";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "c";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "q";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "x";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "v";
 expected = "f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "dg";
 expected = "2g";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tio";
 expected = "sio";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tia";
 expected = "sia";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "d";
 expected = "t";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ph";
 expected = "fh";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "b";
 expected = "p";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "sh";
 expected = "s2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "z";
 expected = "s";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "^[aeiou]";
 expected = "A";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "[aeiou]";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "j";
 expected = "y";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "^y3";
 expected = "Y3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "^y";
 expected = "A";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "y";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3gh3";
 expected = "3kh3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "gh";
 expected = "22";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "g";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "s+";
 expected = "S";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "t+";
 expected = "T";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "p+";
 expected = "P";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "k+";
 expected = "K";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "f+";
 expected = "F";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "m+";
 expected = "M";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "n+";
 expected = "N";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "w3";
 expected = "W3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "wy"; // 1.0 only
 //expected = "Wy";
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "wh3";
 expected = "Wh3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "w$";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "why"; // 1.0 only
 //expected = "Why";
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "w";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "^h";
 expected = "A";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "h";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "r3";
 expected = "R3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "r$";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ry"; // 1.0 only
 //expected = "Ry";
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "r";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "l3";
 expected = "L3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "l$";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ly"; // 1.0 only
 //expected = "Ly";
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "l";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonString() {
 caverphone.encode(123);
 }

 @Test
 public void testEncodeEncode() {
 String input = "thisIsATest";
 String expected = "T3h1iS2iA2T3e2s";
 String actual = caverphone.encode(caverphone.encode(input));
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodeDecode() {
 String input = "thisIsATest";
 String expected = "thisIsATest";
 String encoded = caverphone.encode(input);
 String decoded = caverphone.decode(encoded);
 assertEquals(expected, decoded);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeNonString() {
 caverphone.decode(123);
 }
}