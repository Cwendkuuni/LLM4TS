import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

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
 public void testCaverphoneLowerCase() {
 String input = "hello world";
 String result = caverphone.caverphone(input);
 String expected = "H2l3o2w1r2d1";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceAll() {
 String input = "cougH rough tiough enough trough gn mb cq ci ce cy tch c k q x v dg tio tia d ph b sh z g s t p k f m n w3 w h r3 r ly l y 3gh3 gh g s+ t+ p+ k+ f+ m+ n+ w3 wh w r 3l3 l y j y3 y y 3kh3 k g s+ t+ p+ k+ f+ m+ n+ w3 w W3 r r 2l3 l y 2";
 String result = caverphone.caverphone(input);
 String expected = "cou2f rou2f tio2ugh enou2f trou2f m2 g2 m2 2q si se sy 2ch k k k f k f m n N N w3 W3 r R3 l3 l y 2";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidObject() {
 caverphone.encode(42);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidString() {
 caverphone.encode(null);
 }

 @Test
 public void testIsCaverphoneEqualTrue() {
 String str1 = "hello";
 String str2 = "hello";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertTrue(result);
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String str1 = "hello";
 String str2 = "world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test(expected = DecoderException.class)
 public void testDecodeInvalidString() {
 caverphone.decode("11111111111111111111");
 }

}