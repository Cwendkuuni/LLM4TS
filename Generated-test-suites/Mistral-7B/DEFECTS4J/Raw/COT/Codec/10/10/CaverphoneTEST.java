import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String expected = "1111111111";
 String actual = caverphone.caverphone(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String expected = "1111111111";
 String actual = caverphone.caverphone("");
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "hello world";
 String expected = "h2h3l2l2o2o2w2w";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneMiddleCase() {
 String input = "HeLlO wOrLd";
 String expected = "h2h3l2l2o2o2w2w";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "Cough";
 String expected = "cou2f";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "Rough";
 expected = "rou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "Tough";
 expected = "tou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "Enough";
 expected = "enou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "Trough";
 expected = "trou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual); // 2.0 only, see the specification
 }

 @Test
 public void testCaverphoneStartLetters() {
 String input = "gn";
 String expected = "2n";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "mb";
 expected = "m2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonAlphabeticInput() {
 String input = "12345!@#$%^&*()_+=";
 caverphone.caverphone(input);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "enough";
 String expected = "enou2f";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual); // 2.0 only, see the specification

 input = "enough2";
 expected = "enou2f2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual); // 2.0 only, see the specification
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String input = "CqUi";
 String expected = "si2q";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "CeYo";
 expected = "se2y";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "CyTo";
 expected = "sy2t";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "TchIng";
 expected = "2chIng";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "C";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "Q";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "X";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "V";
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

 input = "why";
 expected = "Why"; // 1.0 only, see the specification
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "wh3";
 expected = "Wh3"; // 1.0 only, see the specification
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String input = "2";
 String expected = "";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3$";
 expected = "A"; // 2.0 only, see the specification
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3";
 expected = "";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphonePutTen1sOnEnd() {
 String input = "test";
 String expected = "test1111111111";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual.substring(0, 10)); // 1.0 truncates to 6

 input = "test12345";
 expected = "test111111111112345";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual.substring(0, 16));
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String input1 = "test";
 String input2 = "test";
 boolean expected = true;
 boolean actual = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, actual);

 input1 = "test";
 input2 = "tesT";
 expected = false;
 actual = caverphone.isCaverphoneEqual(input1, input2);
 assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEncodeObject() throws EncoderException {
 Object obj = new String("test");
 String expected = "test1111111111";
 String actual = (String) caverphone.encode(obj);
 assertEquals(expected, actual.substring(0, 10)); // 1.0 truncates to 6
 }

 @Test
 public void testCaverphoneEncodeString() {
 String input = "test";
 String expected = "test1111111111";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual.substring(0, 10)); // 1.0 truncates to 6
 }
}