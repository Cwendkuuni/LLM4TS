import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneEmptyString() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneNullString() {
 try {
 caverphone.caverphone(null);
 fail("Expected EncoderException not thrown");
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testCaverphoneLowerCase() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2o2l2o";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveNonAlphabetical() {
 String input = "hello123";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2o2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String input = "hello";
 String result = caverphone.caverphone(input);
 String expected = "h2h2l2o2l";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneStartOptions() {
 String input = "cough";
 String result = caverphone.caverphone(input);
 String expected = "cou2f";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneReplacements() {
 String input = "quality";
 String result = caverphone.caverphone(input);
 String expected = "q3l3i3t3y3a3l";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesSpecificLetters() {
 String input = "philosophy";
 String result = caverphone.caverphone(input);
 String expected = "p3h3i3l3o3p3h3i3s3p4h5y";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesDuplicateLetters() {
 String input = "baabbaabbaa";
 String result = caverphone.caverphone(input);
 String expected = "b2a2b2a2";
 assertEquals(expected, result);
 }

 @Test
 public void testCaverphoneHandlesLongStrings() {
 String input = "thisisatestforthecaverphonealgorithm";
 String result = caverphone.caverphone(input);
 String expected = "t3h3i3s3i3s3a3t3e3r3t3h3e3c4a3v3e3p4h5a3l3g3o3r3t3h3e3a3l3g3o3r3t";
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testCaverphoneNonStringInput() {
 caverphone.caverphone(new Object());
 }

}