import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullString() {
 String result = caverphone.caverphone(null);
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyString() {
 String result = caverphone.caverphone("");
 Assert.assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeString() {
 String input = "CaverphoneTest";
 String expected = "c2v3a2p4h5o6n7e";
 String result = caverphone.caverphone(input);
 Assert.assertEquals(expected, result);
 }

 @Test (expected = EncoderException.class)
 public void testNonStringInput() {
 caverphone.encode(42);
 }

 @Test
 public void testEncodeNullInput() {
 try {
 caverphone.encode(null);
 fail("Should have thrown EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertTrue(result);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
 String str1 = "hello world";
 String str2 = "hello there";
 boolean result = caverphone.isCaverphoneEqual(str1, str2);
 Assert.assertFalse(result);
 }

 @Test
 public void testEdgeCases() {
 String input1 = "aAeeiiooouu";
 String expected1 = "A333333333";
 String result1 = caverphone.caverphone(input1);
 Assert.assertEquals(expected1, result1);

 String input2 = "CaverphoneTest123";
 String expected2 = "c2v3a2p4h5o6n7e123";
 String result2 = caverphone.caverphone(input2);
 Assert.assertEquals(expected2, result2);
 }

 @Test
 public void testExceptions() {
 try {
 caverphone.encode(null);
 fail("Should have thrown EncoderException");
 } catch (EncoderException e) {
 // Expected
 }

 try {
 caverphone.encode(new Integer(42));
 fail("Should have thrown EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }

}