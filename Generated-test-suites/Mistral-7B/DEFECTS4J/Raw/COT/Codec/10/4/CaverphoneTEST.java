import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testCaverphoneNullInput() {
 String result = caverphoneInstance.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEmptyInput() {
 String result = caverphoneInstance.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneEncodeString() {
 String input = "example";
 String expected = "exampl2e2mpl2e";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneEncodeNonString() {
 caverphoneInstance.encode(123);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqual() {
 String str1 = "example";
 String str2 = "example2";
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str2);
 assertFalse(result);
 }

 @Test
 public void testCaverphoneIsCaverphoneEqualSameString() {
 String str1 = "example";
 boolean result = caverphoneInstance.isCaverphoneEqual(str1, str1);
 assertTrue(result);
 }

 @Test
 public void testCaverphoneReplaceAllEdgeCases() {
 String input = "CoughCqCiCeCyTch123CtCeCqCiCeCyTchCtCeCqCiCeCyTchCtCe";
 String expected = "cou2f2qsi3se3sy2ch2k2ch2k2ch2k2ch2k2ch2k";
 String result = caverphoneInstance.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplaceAllEdgeCasesWithException() {
 String input = new StringBuffer("").append(Character.MIN_VALUE).toString();
 caverphoneInstance.caverphone(input); // Should throw EncoderException
 }

}