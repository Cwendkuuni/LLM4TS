import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmpty() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphone.caverphone("hello");
 assertEquals("h3l3o", result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabetical() {
 String result = caverphone.caverphone("1234Hello5!");
 assertEquals("h3l3o", result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String result = caverphone.caverphone("testing");
 assertEquals("tesing", result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String result = caverphone.caverphone("CoughCough");
 assertEquals("cou2fcou2f", result);
 String result2 = caverphone.caverphone("Rough");
 assertEquals("rou2f", result2);
 String result3 = caverphone.caverphone("Tough");
 assertEquals("tou2f", result3);
 String result4 = caverphone.caverphone("EnoughEnough");
 assertEquals("enou2fenou2f", result4);
 String result5 = caverphone.caverphone("Trough");
 assertEquals("trou2f", result5);
 String result6 = caverphone.caverphone("gn");
 assertEquals("2n", result6);
 }

 @Test
 public void testCaverphoneReplacements() {
 String result = caverphone.caverphone("Question");
 assertEquals("q2uestion", result);
 String result2 = caverphone.caverphone("Ceiling");
 assertEquals("seiling", result2);
 String result3 = caverphone.caverphone("Cybernetics");
 assertEquals("sybernetics", result3);
 String result4 = caverphone.caverphone("Technotronic");
 assertEquals("2chnotronic", result4);
 String result5 = caverphone.caverphone("CoughCough");
 assertEquals("cou2fcou2f", result5);
 String result6 = caverphone.caverphone("C");
 assertEquals("k", result6);
 String result7 = caverphone.caverphone("Q");
 assertEquals("k", result7);
 String result8 = caverphone.caverphone("X");
 assertEquals("k", result8);
 String result9 = caverphone.caverphone("V");
 assertEquals("f", result9);
 String result10 = caverphone.caverphone("Dg");
 assertEquals("2g", result10);
 String result11 = caverphone.caverphone("Tio");
 assertEquals("sio", result11);
 String result12 = caverphone.caverphone("Tia");
 assertEquals("sia", result12);
 String result13 = caverphone.caverphone("D");
 assertEquals("t", result13);
 String result14 = caverphone.caverphone("Ph");
 assertEquals("fh", result14);
 String result15 = caverphone.caverphone("B");
 assertEquals("p", result15);
 String result16 = caverphone.caverphone("Sh");
 assertEquals("s2", result16);
 String result17 = caverphone.caverphone("Z");
 assertEquals("s", result17);
 String result18 = caverphone.caverphone("AeioUaeiou");
 assertEquals("A1A3A1A3A1A3", result18);
 String result19 = caverphone.caverphone("Why");
 assertEquals("Why", result19); // Edge case, should not be changed
 String result20 = caverphone.caverphone("Why"); // Edge case, should not be changed
 }

 @Test
 public void testCaverphoneRemoveFinalY() {
 String result = caverphone.caverphone("Joyful");
 assertEquals("jo2ful", result);
 }

 @Test
 public void testCaverphoneRemoveFinalY3() {
 String result = caverphone.caverphone("JoyfulY3");
 assertEquals("jo2fulA", result);
 }

 @Test
 public void testCaverphoneRemoveY() {
 String result = caverphone.caverphone("CyberneticsY");
 assertEquals("sybernetics", result);
 }

 @Test
 public void testCaverphoneRemoveY3() {
 String result = caverphone.caverphone("CyberneticsY3");
 assertEquals("sybernetics", result);
 }

 @Test
 public void testCaverphoneRemoveY2() {
 String result = caverphone.caverphone("AbercrombieAndFitch");
 assertEquals("abercrombieandfitch", result);
 }

 @Test
 public void testCaverphoneRemove1() {
 String result = caverphone.caverphone("12345");
 assertEquals("2345", result);
 }

 @Test
 public void testCaverphoneRemove3() {
 String result = caverphone.caverphone("12345");
 assertEquals("2345", result);
 }

 @Test
 public void testCaverphoneRemove10() {
 String result = caverphone.caverphone("12345678910");
 assertEquals("23456789", result);
 }

 @Test
 public void testCaverphoneRemove3gh3() {
 String result = caverphone.caverphone("ThirdThird");
 assertEquals("3kh3", result);
 }

 @Test
 public void testCaverphoneGh() {
 String result = caverphone.caverphone("Ghana");
 assertEquals("22", result);
 }

 @Test
 public void testCaverphoneG() {
 String result = caverphone.caverphone("G");
 assertEquals("k", result);
 }

 @Test
 public void testCaverphoneS() {
 String result = caverphone.caverphone("S");
 assertEquals("S", result);
 }

 @Test
 public void testCaverphoneT() {
 String result = caverphone.caverphone("T");
 assertEquals("T", result);
 }

 @Test
 public void testCaverphoneP() {
 String result = caverphone.caverphone("P");
 assertEquals("P", result);
 }

 @Test
 public void testCaverphoneK() {
 String result = caverphone.caverphone("K");
 assertEquals("K", result);
 }

 @Test
 public void testCaverphoneF() {
 String result = caverphone.caverphone("F");
 assertEquals("F", result);
 }

 @Test
 public void testCaverphoneM() {
 String result = caverphone.caverphone("M");
 assertEquals("M", result);
 }

 @Test
 public void testCaverphoneN() {
 String result = caverphone.caverphone("N");
 assertEquals("N", result);
 }

 @Test
 public void testCaverphoneW3() {
 String result = caverphone.caverphone("WhyWhyWhy");
 assertEquals("Wh3Wh3Wh3", result);
 }

 @Test
 public void testCaverphoneW() {
 String result = caverphone.caverphone("WhyWhyWhy");
 assertEquals("Wh3Wh3Wh3", result);
 }

 @Test
 public void testCaverphoneWh3() {
 String result = caverphone.caverphone("WhyWhyWhy");
 assertEquals("Wh3Wh3Wh3", result);
 }

 @Test
 public void testCaverphoneWh() {
 String result = caverphone.caverphone("WhyWhyWhy");
 assertEquals("Wh3Wh3Wh3", result);
 }

 @Test
 public void testCaverphoneR3() {
 String result = caverphone.caverphone("RoughRough");
 assertEquals("rou2fr3", result);
 }

 @Test
 public void testCaverphoneR() {
 String result = caverphone.caverphone("RoughRough");
 assertEquals("rou2f", result);
 }

 @Test
 public void testCaverphoneL3() {
 String result = caverphone.caverphone("SweetLemon");
 assertEquals("sweetlem3", result);
 }

 @Test
 public void testCaverphoneL() {
 String result = caverphone.caverphone("SweetLemon");
 assertEquals("sweetlem2", result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(true, result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqualDifferentStrings() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(false, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneEncodeInvalidType() {
 Caverphone caverphone = new Caverphone();
 caverphone.encode(123);
 }
}