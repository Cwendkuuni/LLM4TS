import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.junit.Assert;
import org.junit.Test;

public class CaverphoneTest implements StringEncoder, StringDecoder {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String actual = null;
 String expected = "1111111111";

 try {
 actual = caverphone.encode(null);
 } catch (EncoderException e) {
 // expected when input is null
 }

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String actual = caverphone.encode("");
 String expected = "1111111111";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String actual = caverphone.encode("hello");
 String expected = "h3l2l2o2";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneRemoveNonLetters() {
 String actual = caverphone.encode("123CaV3PhoNe");
 String expected = "CvPn";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String actual = caverphone.encode("caverphone");
 String expected = "cvrphn3";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesStartOptions() {
 String actual = caverphone.encode("cough");
 String expected = "cou2f";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("rough");
 expected = "rou2f";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("tough");
 expected = "tou2f";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("enough");
 expected = "enou2f";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("trough");
 expected = "trou2f";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesReplacements() {
 String actual = caverphone.encode("cq");
 String expected = "2q";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("ci");
 expected = "si";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("ce");
 expected = "se";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("cy");
 expected = "sy";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("tch");
 expected = "2ch";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("c");
 expected = "k";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("q");
 expected = "k";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("x");
 expected = "k";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("v");
 expected = "f";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("dg");
 expected = "2g";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("tio");
 expected = "sio";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("tia");
 expected = "sia";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("d");
 expected = "t";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("ph");
 expected = "fh";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("b");
 expected = "p";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("sh");
 expected = "s2";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("z");
 expected = "s";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("^[aeiou]");
 expected = "A";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("[aeiou]");
 expected = "3";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("j");
 expected = "y";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("^y3");
 expected = "Y3";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("^y");
 expected = "A";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("y");
 expected = "3";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("3gh3");
 expected = "3kh3";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("gh");
 expected = "22";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("g");
 expected = "k";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesRemovals() {
 String actual = caverphone.encode("2");
 String expected = "";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("3$");
 expected = "A";

 Assert.assertEquals(expected, actual);

 actual = caverphone.encode("3");
 expected = "";

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneHandlesLength() {
 String actual = caverphone.encode("12345");
 String expected = "111111";

 Assert.assertEquals(expected, actual.substring(0, 10));
 }

 @Test
 public void testCaverphoneEncodeDecode() throws DecoderException {
 String encoded = caverphone.encode("hello");
 String decoded = decode(encoded);

 Assert.assertEquals("hello", decoded);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";

 boolean actual = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = true;

 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testCaverphoneIsCaverphoneDifferent() {
 String str1 = "hello";
 String str2 = "world";

 boolean actual = caverphone.isCaverphoneEqual(str1, str2);
 boolean expected = false;

 Assert.assertEquals(expected, actual);
 }

}