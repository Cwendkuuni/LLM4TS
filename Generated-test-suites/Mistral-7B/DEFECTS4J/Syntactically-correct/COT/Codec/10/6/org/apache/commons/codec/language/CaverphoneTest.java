import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.language.Caverphone;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

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
 public void testCaverphoneLowercase() {
 String result = caverphone.caverphone("hello");
 assertEquals("h3l2o", result);
 }

 @Test
 public void testCaverphoneRemoveNonAZ() {
 String result = caverphone.caverphone("123abc456Def");
 assertEquals("a2f", result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNullInputThrowsEncoderException() {
 Caverphone caverphone = new Caverphone();
 caverphone.encode(null);
 }

 @Test
 public void testCaverphoneEdgeCases() {
 String result = caverphone.caverphone("Cough");
 assertEquals("cou2f", result);
 String result1 = caverphone.caverphone("Rough");
 assertEquals("rou2f", result1);
 String result2 = caverphone.caverphone("Tough");
 assertEquals("tou2f", result2);
 String result3 = caverphone.caverphone("Enough");
 assertEquals("enou2f", result3);
 String result4 = caverphone.caverphone("Trough");
 assertEquals("trou2f", result4);
 String result5 = caverphone.caverphone("Gn");
 assertEquals("2n", result5);
 String result6 = caverphone.caverphone("Mb");
 assertEquals("m2", result6);
 String result7 = caverphone.caverphone("Cq");
 assertEquals("2q", result7);
 String result8 = caverphone.caverphone("Ci");
 assertEquals("si", result8);
 String result9 = caverphone.caverphone("Ce");
 assertEquals("se", result9);
 String result10 = caverphone.caverphone("Cy");
 assertEquals("sy", result10);
 String result11 = caverphone.caverphone("Tch");
 assertEquals("2ch", result11);
 String result12 = caverphone.caverphone("C");
 assertEquals("k", result12);
 String result13 = caverphone.caverphone("Q");
 assertEquals("k", result13);
 String result14 = caverphone.caverphone("X");
 assertEquals("k", result14);
 String result15 = caverphone.caverphone("V");
 assertEquals("f", result15);
 String result16 = caverphone.caverphone("Dg");
 assertEquals("2g", result16);
 String result17 = caverphone.caverphone("Tio");
 assertEquals("sio", result17);
 String result18 = caverphone.caverphone("Tia");
 assertEquals("sia", result18);
 String result19 = caverphone.caverphone("D");
 assertEquals("t", result19);
 String result20 = caverphone.caverphone("Ph");
 assertEquals("fh", result20);
 String result21 = caverphone.caverphone("B");
 assertEquals("p", result21);
 String result22 = caverphone.caverphone("Sh");
 assertEquals("s2", result22);
 String result23 = caverphone.caverphone("Z");
 assertEquals("s", result23);
 String result24 = caverphone.caverphone("Wh3");
 assertEquals("Wh3", result24);
 String result25 = caverphone.caverphone("W");
 assertEquals("2", result25);
 String result26 = caverphone.caverphone("Y3");
 assertEquals("Y3", result26);
 String result27 = caverphone.caverphone("Y");
 assertEquals("3", result27);
 String result28 = caverphone.caverphone("3gh3");
 assertEquals("3kh3", result28);
 String result29 = caverphone.caverphone("Gh");
 assertEquals("22", result29);
 String result30 = caverphone.caverphone("G");
 assertEquals("k", result30);
 String result31 = caverphone.caverphone("S+");
 assertEquals("S", result31);
 String result32 = caverphone.caverphone("T+");
 assertEquals("T", result32);
 String result33 = caverphone.caverphone("P+");
 assertEquals("P", result33);
 String result34 = caverphone.caverphone("K+");
 assertEquals("K", result34);
 String result35 = caverphone.caverphone("F+");
 assertEquals("F", result35);
 String result36 = caverphone.caverphone("M+");
 assertEquals("M", result36);
 String result37 = caverphone.caverphone("N+");
 assertEquals("N", result37);
 String result38 = caverphone.caverphone("W3");
 assertEquals("W3", result38);
 //String result39 = caverphone.caverphone("Wy");
 //assertEquals("Wy", result39);
 String result40 = caverphone.caverphone("Wh3");
 assertEquals("Wh3", result40);
 //String result41 = caverphone.caverphone("Why");
 //assertEquals("Why", result41);
 String result42 = caverphone.caverphone("W");
 assertEquals("2", result42);
 String result43 = caverphone.caverphone("A");
 assertEquals("A", result43);
 String result44 = caverphone.caverphone("H");
 assertEquals("2", result44);
 String result45 = caverphone.caverphone("R3");
 assertEquals("R3", result45);
 String result46 = caverphone.caverphone("R");
 assertEquals("2", result46);
 String result47 = caverphone.caverphone("L3");
 assertEquals("L3", result47);
 //String result48 = caverphone.caverphone("Ly");
 //assertEquals("Ly", result48);
 String result49 = caverphone.caverphone("L");
 assertEquals("2", result49);
 String result50 = caverphone.caverphone("J");
 assertEquals("y", result50);
 String result51 = caverphone.caverphone("Y3");
 assertEquals("Y3", result51);
 String result52 = caverphone.caverphone("Y");
 assertEquals("3", result52);
 }

}